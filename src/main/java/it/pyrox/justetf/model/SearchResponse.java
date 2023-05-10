package it.pyrox.justetf.model;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

public class SearchResponse {

	private Integer recordsFiltered;
	@JsonProperty("data")
	private List<ETF> data;
	private Integer draw;
	private Integer recordsTotal;
	private Map<String, Object> additionalProperties = new LinkedHashMap<>();
	
	public Integer getRecordsFiltered() {
		return recordsFiltered;
	}
	
	public void setRecordsFiltered(Integer recordsFiltered) {
		this.recordsFiltered = recordsFiltered;
	}
	
	public List<ETF> getData() {
		return data;
	}
	
	public void setData(List<ETF> data) {
		this.data = data;
	}
	
	public Integer getDraw() {
		return draw;
	}
	
	public void setDraw(Integer draw) {
		this.draw = draw;
	}
	
	public Integer getRecordsTotal() {
		return recordsTotal;
	}
	
	public void setRecordsTotal(Integer recordsTotal) {
		this.recordsTotal = recordsTotal;
	}
	
	public Map<String, Object> getAdditionalProperties() {
		return this.additionalProperties;
	}
	
	public void setAdditionalProperty(String name, Object value) {
		this.additionalProperties.put(name, value);
	}
}
