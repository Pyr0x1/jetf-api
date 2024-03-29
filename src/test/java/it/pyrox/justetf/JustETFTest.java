package it.pyrox.justetf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Locale;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;

import it.pyrox.justetf.model.ETF;
import it.pyrox.justetf.model.Quote;
import it.pyrox.justetf.model.SearchResponse;

class JustETFTest {
	
	@Test
	void testSearchWithLocaleITAndNoQueryAndNoRangeThenReturnFirst25Results() {
		SearchResponse response = new JustETF().search(Locale.ITALY, null);
		assertNotNull(response);
		assertNotNull(response.getRecordsTotal());
		assertTrue(response.getRecordsTotal() > 0);
		assertNotNull(response.getRecordsFiltered());
		assertTrue(response.getRecordsFiltered() > 0);
		assertEquals(response.getRecordsFiltered(), response.getRecordsTotal());
		assertNotNull(response.getData());
		assertTrue(response.getData().size() > 0 && response.getData().size() <= 25);
	}
	
	@Test
	void testSearchWithLocaleITAndNoQueryAndRangeFrom10To20ThenReturnCorrespondingResults() {
		SearchResponse responseFirst25 = new JustETF().search(Locale.ITALY, null);
		assertNotNull(responseFirst25);
		assertNotNull(responseFirst25.getRecordsTotal());
		assertTrue(responseFirst25.getRecordsTotal() > 0);
		assertNotNull(responseFirst25.getRecordsFiltered());
		assertTrue(responseFirst25.getRecordsFiltered() > 0);
		assertEquals(responseFirst25.getRecordsFiltered(), responseFirst25.getRecordsTotal());
		assertNotNull(responseFirst25.getData());
		assertTrue(responseFirst25.getData().size() > 0 && responseFirst25.getData().size() <= 25);
		SearchResponse responseFrom10To20 = new JustETF().search(Locale.ITALY, null, 10, 10, new ObjectMapper());
		assertNotNull(responseFrom10To20);
		assertNotNull(responseFrom10To20.getRecordsTotal());
		assertTrue(responseFrom10To20.getRecordsTotal() > 0);
		assertNotNull(responseFrom10To20.getRecordsFiltered());
		assertTrue(responseFrom10To20.getRecordsFiltered() > 0);
		assertEquals(responseFrom10To20.getRecordsFiltered(), responseFrom10To20.getRecordsTotal());
		assertNotNull(responseFrom10To20.getData());
		assertTrue(responseFrom10To20.getData().size() > 0 && responseFrom10To20.getData().size() <= 10);
		int index = 10;
		for (ETF element : responseFrom10To20.getData()) {
			ETF elementToCompare = responseFirst25.getData().get(index++);
			assertEquals(element.getTicker(), elementToCompare.getTicker());
			assertEquals(element.getIsin(), elementToCompare.getIsin());
			assertEquals(element.getName(), elementToCompare.getName());
		}
	}

	@Test
	void testSearchWithLocaleITAndQueryByTickerAndNoRangeThenReturnOneResult() {
		SearchResponse response = new JustETF().search(Locale.ITALY, "vwce");
		assertNotNull(response);
		assertNotNull(response.getRecordsTotal());
		assertTrue(response.getRecordsTotal() > 0);
		assertNotNull(response.getRecordsFiltered());
		assertTrue(response.getRecordsFiltered() > 0);
		assertEquals(response.getRecordsFiltered(), response.getRecordsTotal());
		assertNotNull(response.getData());
		assertEquals(1, response.getData().size());
		assertEquals("VWCE", response.getData().get(0).getTicker());
	}
	
	@Test
	void testSearchWithPriceAndLocaleITAndQueryByTickerAndNoRangeThenReturnOneResultWithPrice() {
		SearchResponse response = new JustETF().searchWithQuote(Locale.ITALY, "vwce");
		assertNotNull(response);
		assertNotNull(response.getRecordsTotal());
		assertTrue(response.getRecordsTotal() > 0);
		assertNotNull(response.getRecordsFiltered());
		assertTrue(response.getRecordsFiltered() > 0);
		assertEquals(response.getRecordsFiltered(), response.getRecordsTotal());
		assertNotNull(response.getData());
		assertEquals(1, response.getData().size());
		assertEquals("VWCE", response.getData().get(0).getTicker());
		assertNotNull(response.getData().get(0).getQuote());
		assertNotNull(response.getData().get(0).getQuote().getLatestQuote());
		assertNotNull(response.getData().get(0).getQuote().getLatestQuote().getLocalized());
		assertNotNull(response.getData().get(0).getQuote().getLatestQuote().getRaw());
		assertTrue(response.getData().get(0).getQuote().getLatestQuote().getRaw() > 0);
	}
	
	@Test
	void testGetQuoteWithLocaleIT() {
		Quote quote = new JustETF().getQuote("IE00BK5BQT80", new Locale.Builder().setLanguage("it").setRegion("IT").build(), new ObjectMapper());
		assertNotNull(quote);
		assertNotNull(quote.getLatestQuote());
		assertNotNull(quote.getLatestQuote().getLocalized());
		assertNotNull(quote.getLatestQuote().getRaw());
		assertTrue(quote.getLatestQuote().getRaw() > 0);
	}
	
	@Test
	void testGetQuoteWithLocaleUK() {
		Quote quote = new JustETF().getQuote("IE00BK5BQT80", Locale.UK, new ObjectMapper());
		assertNotNull(quote);
		assertNotNull(quote.getLatestQuote());
		assertNotNull(quote.getLatestQuote().getLocalized());
		assertNotNull(quote.getLatestQuote().getRaw());
		assertTrue(quote.getLatestQuote().getRaw() > 0);
	}
	
	@Test
	void testGetQuoteWithLocaleCH() {
		Quote quote = new JustETF().getQuote("IE00BK5BQT80", new Locale.Builder().setLanguage("en").setRegion("CH").build(), new ObjectMapper());
		assertNotNull(quote);
		assertNotNull(quote.getLatestQuote());
		assertNotNull(quote.getLatestQuote().getLocalized());
		assertNotNull(quote.getLatestQuote().getRaw());
		assertTrue(quote.getLatestQuote().getRaw() > 0);
	}
}
