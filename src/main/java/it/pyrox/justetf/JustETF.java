package it.pyrox.justetf;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLEncoder;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.nio.charset.StandardCharsets;
import java.util.Currency;
import java.util.Locale;
import java.util.Map;
import java.util.stream.Collectors;

import com.fasterxml.jackson.databind.DeserializationFeature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.fasterxml.jackson.databind.ObjectMapper;

import it.pyrox.justetf.model.ETF;
import it.pyrox.justetf.model.Quote;
import it.pyrox.justetf.model.SearchResponse;

public class JustETF {
	
	private static final String BASE_URL = "https://www.justetf.com/";
	private static final String SEARCH_PATH = "servlet/etfs-table";
	private static final String QUOTE_PATH_1 = "api/etfs/";
	private static final String QUOTE_PATH_2 = "/quote";
	private static final Integer DEFAULT_START = 0;
	private static final Integer DEFAULT_LENGTH = 25;
	private static final String PARAM_DRAW = "draw";
	private static final String PARAM_START = "start";
	private static final String PARAM_LENGTH = "length";
	private static final String PARAM_LANG = "lang";
	private static final String PARAM_COUNTRY = "country";
	private static final String PARAM_UNIVERSE_TYPE = "universeType";
	private static final String PARAM_ETFS_PARAMS = "etfsParams";

	private ObjectMapper objectMapper;

	private static final Logger logger = LoggerFactory.getLogger(JustETF.class);

	public JustETF() {
		this.objectMapper = new ObjectMapper().configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
	}

	public Quote getQuote(String isin, Locale locale) {
		return getQuote(isin, locale, objectMapper);
	}

	public SearchResponse search(Locale locale, String query, int start, int length) {
		return search(locale, query, start, length, objectMapper);
	}
	
	public SearchResponse search(Locale locale, String query) {
		return search(locale, query, DEFAULT_START, DEFAULT_LENGTH);
	}
	
	public SearchResponse searchWithQuote(Locale locale, String query, int start, int length) {
		SearchResponse response = search(locale, query, start, length);
		
		if (response != null && response.getData() != null && !response.getData().isEmpty()) {
			for (ETF etfData : response.getData()) {
				Quote quote = getQuote(etfData.getIsin(), locale);
				etfData.setQuote(quote);
			}
		}
		
		return response;
	}
	
	public SearchResponse searchWithQuote(Locale locale, String query) {
		return searchWithQuote(locale, query, DEFAULT_START, DEFAULT_LENGTH);
	}

	// Use the protected method in tests to pass an ObjectMapper that throws error if a property isn't mapped
	// The public method in this class, instead, uses an ObjectMapper that ignores unkown properties
	protected Quote getQuote(String isin, Locale locale, ObjectMapper objectMapper) {
		Quote quote = null;
		Currency currency = Currency.getInstance(locale);

		if (isin == null || isin.isEmpty() || locale == null || currency == null) {
			return quote;
		}

		try {
			HttpRequest httpRequest = HttpRequest.newBuilder(new URI(BASE_URL + QUOTE_PATH_1 + isin + QUOTE_PATH_2 + "?locale=" + locale.getLanguage() + "&currency=" + currency.getCurrencyCode()))
					.GET()
					.build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			quote = objectMapper.readValue(httpResponse.body(), Quote.class);
		} catch (URISyntaxException | IOException e) {
			logger.debug(e.getMessage());
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
			Thread.currentThread().interrupt();
		}

		return quote;
	}

	// Use the protected method in tests to pass an ObjectMapper that throws error if a property isn't mapped
	// The public method in this class, instead, uses an ObjectMapper that ignores unkown properties
	protected SearchResponse search(Locale locale, String query, int start, int length, ObjectMapper objectMapper) {
		SearchResponse response = null;

		try {
			HttpRequest httpRequest = HttpRequest.newBuilder(new URI(BASE_URL + SEARCH_PATH))
					.headers("Content-Type", "application/x-www-form-urlencoded")
					.POST(HttpRequest.BodyPublishers.ofString(getUrlEncodedParams(locale, start, length, query)))
					.build();

			HttpClient client = HttpClient.newHttpClient();

			HttpResponse<String> httpResponse = client.send(httpRequest, HttpResponse.BodyHandlers.ofString());

			response = objectMapper.readValue(httpResponse.body(), SearchResponse.class);

		} catch (URISyntaxException | IOException e) {
			logger.debug(e.getMessage());
		} catch (InterruptedException e) {
			logger.debug(e.getMessage());
			Thread.currentThread().interrupt();
		}

		return response;
	}

	private String getUrlEncodedParams(Locale locale, int start, int length, String query) {
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
