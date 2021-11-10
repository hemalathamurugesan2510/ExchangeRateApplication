package com.exchange.rate.io.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity(name = "exchangerate")
@Table(name = "exchangerate")
@Getter
@Setter
@NoArgsConstructor
public class ExchangeRate {

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(name = "date")
	public String date;

	@Override
	public String toString() {
		return "ExchangeRate [id=" + id + ", date=" + date + ", base=" + base + ", gbp=" + gbp + ", usd=" + usd
				+ ", hkd=" + hkd + "]";
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
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

	public Double getGbp() {
		return gbp;
	}

	public void setGbp(Double gbp) {
		this.gbp = gbp;
	}

	public Double getUsd() {
		return usd;
	}

	public void setUsd(Double usd) {
		this.usd = usd;
	}

	public Double getHkd() {
		return hkd;
	}

	public void setHkd(Double hkd) {
		this.hkd = hkd;
	}

	@Column(name = "base")
	public String base;

	@Column(name = "gbp")
	public Double gbp;

	@Column(name = "usd")
	public Double usd;

	@Column(name = "hkd")
	public Double hkd;

}
