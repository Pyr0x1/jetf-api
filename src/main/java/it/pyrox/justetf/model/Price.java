package it.pyrox.justetf.model;

import java.util.Currency;

public class Price {
	
	private Double value;
	private Currency currency;
	
	public Price(Double value, Currency currency) {
		this.value = value;
		this.currency = currency;
	}

	public Double getValue() {
		return value;
	}
	
	public void setValue(Double value) {
		this.value = value;
	}
	
	public Currency getCurrency() {
		return currency;
	}
	
	public void setCurrency(Currency currency) {
		this.currency = currency;
	}
}
