package it.pyrox.justetf;

import java.util.Currency;
import java.util.Locale;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import it.pyrox.justetf.model.Price;
import it.pyrox.justetf.model.SearchResponse;

public class Main {

	public static void main(String[] args) {
		Locale locale = Locale.ITALY;
		Price price = JustETF.getPrice("IE00BK5BQT80", locale);
		System.out.println("Prezzo: " + price.getValue() + " " + Currency.getInstance(locale).getSymbol());
		
		SearchResponse response = JustETF.search(locale, "vwce");
		try {
			System.out.println(new ObjectMapper().writeValueAsString(response));
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}		
}
