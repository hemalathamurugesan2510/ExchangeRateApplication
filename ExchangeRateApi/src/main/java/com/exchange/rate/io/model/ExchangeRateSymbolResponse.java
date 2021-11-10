package com.exchange.rate.io.model;


import java.util.Map;

import com.fasterxml.jackson.databind.JsonNode;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor


public class ExchangeRateSymbolResponse {
	
	
	public boolean success;
	
	@Override
	public String toString() {
		return "ExchangeRateSymbolResponse [success=" + success + ", timestamp=" + timestamp + ", base=" + base
				+ ", date=" + date + "]";
	}
	
	
	public int timestamp;
	public String base;
	public String date;

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

}
