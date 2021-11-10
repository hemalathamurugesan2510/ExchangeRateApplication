package com.exchange.rate.io.model;

import java.util.Map;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "exchangeratesymbol")
@Table(name = "exchangeratesymbol")
@Getter
@Setter
@NoArgsConstructor
public class ExchangeRateSymbol {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long exchangeRateId;
	
	

	@Column(name = "date")
	public String date;

		@Column(name = "base")
	public String base;

	@Column(name = "exchangerate")
	public Double exchangerate;

	public Long getExchangeRateId() {
		return exchangeRateId;
	}

	public void setExchangeRateId(Long exchangeRateId) {
		this.exchangeRateId = exchangeRateId;
	}

	public String getDate() {
		return date;
	}

	public void setDate(String date) {
		this.date = date;
	}

	public String getBase() {
		return base;
	}

	public void setBase(String base) {
		this.base = base;
	}

	public Double getExchangerate() {
		return exchangerate;
	}

	public void setExchangerate(Double exchangerate) {
		this.exchangerate = exchangerate;
	}

	

}
