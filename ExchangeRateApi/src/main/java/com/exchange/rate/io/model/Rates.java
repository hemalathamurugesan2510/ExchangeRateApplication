package com.exchange.rate.io.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor

public class Rates {

	@JsonProperty("GBP")
	public double gbp;
	@JsonProperty("USD")
	public double usd;
	@JsonProperty("HKD")
	public double hkd;


	public double getGbp() {
		return gbp;
	}

	public void setGbp(double gbp) {
		this.gbp = gbp;
	}

	public double getUsd() {
		return usd;
	}

	public void setUsd(double usd) {
		this.usd = usd;
	}

	public double getHkd() {
		return hkd;
	}

	public void setHkd(double hkd) {
		this.hkd = hkd;
	}

	@Override
	public String toString() {
		return "Rates [gbp=" + gbp + ", usd=" + usd + ", hkd=" + hkd + "]";
	}

}
