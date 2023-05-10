package it.pyrox.justetf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.text.NumberFormat;
import java.text.ParseException;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.pyrox.justetf.model.ETF;
import it.pyrox.justetf.model.Price;
import it.pyrox.justetf.model.SearchResponse;

public class JustETF {
	
	private static final String BASE_URL = "https://www.justetf.com/";
	private static final String SEARCH_PATH = "servlet/etfs-table";
	private static final String DETAILS_PATH = "etf-profile.html";
	private static final String CLASS_INFOBOX = "infobox";
	private static final String CLASS_VAL = "val";
	private static final String TAG_SPAN = "span";
	private static final Integer DEFAULT_START = 0;
	private static final Integer DEFAULT_LENGTH = 25;
	private static final String PARAM_DRAW = "draw";
	private static final String PARAM_START = "start";
	private static final String PARAM_LENGTH = "length";
	private static final String PARAM_LANG = "lang";
	private static final String PARAM_COUNTRY = "country";
	private static final String PARAM_UNIVERSE_TYPE = "universeType";
	private static final String PARAM_ETFS_PARAMS = "etfsParams";
	
	private static final Logger logger = LoggerFactory.getLogger(JustETF.class);
	
	private JustETF() {}
	
	public static Price getPrice(String isin, Locale locale) {
		String lang = locale.getLanguage();
		String url = BASE_URL + lang + "/" + DETAILS_PATH + "?isin=" + isin;
		Price result = null;
		Price price = null;
		
		try {
			Document doc = Jsoup.connect(url).cookies(getCookies(locale)).get();
			Elements infoboxes = doc.getElementsByClass(CLASS_INFOBOX);
			if (infoboxes.isEmpty()) {
				return result;
			}
			for (Element infobox : infoboxes) {
				Elements vals = infobox.getElementsByClass(CLASS_VAL);
				if (vals.isEmpty()) {
					return result;
				}
				for (Element val : vals) {
					Elements spans = val.getElementsByTag(TAG_SPAN);
					if (spans.isEmpty()) {
						return result;
					}					
					price = getPrice(spans, locale);
					if (price != null && price.getValue() != null) {
						result = price;
						return result;
					}
				}
			}
		} catch (IOException e) {
			logger.debug(e.getMessage());
		}
		
		return result;
	}
	
	private static Map<String, String> getCookies(Locale locale) {
		return Map.of("language_", locale.getLanguage(), 
					  "universeCountry_", locale.getCountry());
	}
	
	private static Price getPrice(Elements spans, Locale locale) {
		Price result = null;
				
		boolean foundCurr = false;
		boolean foundPrice = false;
		Double tmpDouble = null;
		Currency currency = Currency.getInstance(locale);
		for (Element span : spans) {
			String text = span.text();
			if (currency.getCurrencyCode().equals(text)) {
				foundCurr = true;
			}
			else {
				try {
					NumberFormat format = NumberFormat.getInstance(locale);
					Number num = format.parse(text);
					tmpDouble = num.doubleValue();
					foundPrice = true;
				} catch (ParseException e) {
					foundPrice = false;
				}
			}
		}
		if (foundCurr && foundPrice) {
			result = new Price(tmpDouble, currency);
		}
		
		return result;
	}
	
	public static SearchResponse search(Locale locale, String query, int start, int length) {
		SearchResponse response = null;
		
		try {
			HttpRequest httpRequest = HttpRequest.newBuilder(new URI(BASE_URL + SEARCH_PATH))
												 .headers("Content-Type", "application/x-www-form-urlencoded")
										         .POST(HttpRequest.BodyPublishers.ofString(getUrlEncodedParams(locale, start, length, query)))
										         .build();
			
			HttpClient client = HttpClient.newHttpClient();
			
			HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());
			
			response = new ObjectMapper().readValue(httpResponse.body(), SearchResponse.class);		
			
		} catch (URISyntaxException | IOException e) {
			logger.debug(e.getMessage());
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
			Thread.currentThread().interrupt();
		}
		
		return response;
	}
	
	public static SearchResponse search(Locale locale, String query) {
		return search(locale, query, DEFAULT_START, DEFAULT_LENGTH);
	}
	
	public static SearchResponse searchWithPrice(Locale locale, String query, int start, int length) {
		SearchResponse response = search(locale, query, start, length);
		
		if (response != null && response.getData() != null && !response.getData().isEmpty()) {
			for (ETF etfData : response.getData()) {
				Price price = getPrice(etfData.getIsin(), locale);
				etfData.setPrice(price);
			}
		}
		
		return response;
	}
	
	public static SearchResponse searchWithPrice(Locale locale, String query) {
		return searchWithPrice(locale, query, DEFAULT_START, DEFAULT_LENGTH);
	}
	
	private static String getUrlEncodedParams(Locale locale, int start, int length, String query) {
		return Map.of(PARAM_DRAW, "0", // ???
					  PARAM_START, String.valueOf(start),
					  PARAM_LENGTH, String.valueOf(length),
					  PARAM_LANG, locale.getLanguage(),
					  PARAM_COUNTRY, locale.getCountry(),
					  PARAM_UNIVERSE_TYPE, "private", // ???
					  PARAM_ETFS_PARAMS, "query=" + (query != null ? query : ""))
				  .entrySet()
				  .stream()
				  .map(e -> e.getKey() + "=" + URLEncoder.encode(e.getValue(), StandardCharsets.UTF_8))
				  .collect(Collectors.joining("&"));
	}
}
