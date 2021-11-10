package com.exchange.rate.io.model;

import java.util.Map;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateResponseDateCurrency {

	public boolean success;
	public int timestamp;
	public String base;
	public String date;
	public boolean historical;
	private Map<String, Double> rates;

	@Override
	public String toString() {
		return "ExchangeRateResponseDateCurrency [success=" + success + ", timestamp=" + timestamp + ", base=" + base
				+ ", date=" + date + ", historical=" + historical + ", rates=" + rates + "]";
	}

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public int getTimestamp() {
		return timestamp;
	}

	public void setTimestamp(int timestamp) {
		this.timestamp = timestamp;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public boolean isHistorical() {
		return historical;
	}

	public void setHistorical(boolean historical) {
		this.historical = historical;
	}

	public Map<String, Double> getRates() {
		return rates;
	}

	public void setRates(Map<String, Double> rates) {
		this.rates = rates;
	}

}
