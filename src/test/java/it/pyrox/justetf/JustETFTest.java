package it.pyrox.justetf;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Currency;
import java.util.Locale;

import org.junit.jupiter.api.Test;

import it.pyrox.justetf.model.ETF;
import it.pyrox.justetf.model.Price;
import it.pyrox.justetf.model.SearchResponse;

public class JustETFTest {
	
	@Test
	public void searchWithLocaleITAndNoQueryAndNoRangeThenReturnFirst25Results() {
		SearchResponse response = JustETF.search(Locale.ITALY, null);
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
	public void searchWithLocaleITAndNoQueryAndRangeFrom10To20ThenReturnCorrespondingResults() {
		SearchResponse responseFirst25 = JustETF.search(Locale.ITALY, null);
		assertNotNull(responseFirst25);
		assertNotNull(responseFirst25.getRecordsTotal());
		assertTrue(responseFirst25.getRecordsTotal() > 0);
		assertNotNull(responseFirst25.getRecordsFiltered());
		assertTrue(responseFirst25.getRecordsFiltered() > 0);
		assertEquals(responseFirst25.getRecordsFiltered(), responseFirst25.getRecordsTotal());
		assertNotNull(responseFirst25.getData());
		assertTrue(responseFirst25.getData().size() > 0 && responseFirst25.getData().size() <= 25);
		SearchResponse responseFrom10To20 = JustETF.search(Locale.ITALY, null, 10, 10);
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
	public void searchWithLocaleITAndQueryByTickerAndNoRangeThenReturnOneResult() {
		SearchResponse response = JustETF.search(Locale.ITALY, "vwce");
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
	public void searchWithPriceAndLocaleITAndQueryByTickerAndNoRangeThenReturnOneResultWithPrice() {
		SearchResponse response = JustETF.searchWithPrice(Locale.ITALY, "vwce");
		assertNotNull(response);
		assertNotNull(response.getRecordsTotal());
		assertTrue(response.getRecordsTotal() > 0);
		assertNotNull(response.getRecordsFiltered());
		assertTrue(response.getRecordsFiltered() > 0);
		assertEquals(response.getRecordsFiltered(), response.getRecordsTotal());
		assertNotNull(response.getData());
		assertEquals(1, response.getData().size());
		assertEquals("VWCE", response.getData().get(0).getTicker());
		assertNotNull(response.getData().get(0).getPrice());
		assertNotNull(response.getData().get(0).getPrice().getCurrency());
		assertNotNull(response.getData().get(0).getPrice().getValue());
		assertTrue(response.getData().get(0).getPrice().getValue() > 0);
	}
	
	@Test
	public void getPriceWithLocaleITAndIsinThenReturnPriceInEur() {
		Price price = JustETF.getPrice("IE00BK5BQT80", Locale.ITALY);
		assertNotNull(price);
		assertNotNull(price.getCurrency());
		assertEquals(Currency.getInstance("EUR"), price.getCurrency());
		assertNotNull(price.getValue());
	}
	
	@Test
	public void getPriceWithLocaleUKAndIsinThenReturnPriceInGbp() {
		Price price = JustETF.getPrice("IE00BK5BQT80", Locale.UK);
		assertNotNull(price);
		assertNotNull(price.getCurrency());
		assertEquals(Currency.getInstance("GBP"), price.getCurrency());
		assertNotNull(price.getValue());
	}
	
	@Test
	public void getPriceWithLocaleCHAndIsinThenReturnPriceInCHF() {
		Price price = JustETF.getPrice("IE00BK5BQT80", new Locale.Builder().setLanguage("en").setRegion("CH").build());
		assertNotNull(price);
		assertNotNull(price.getCurrency());
		assertEquals(Currency.getInstance("CHF"), price.getCurrency());
		assertNotNull(price.getValue());
	}
}
