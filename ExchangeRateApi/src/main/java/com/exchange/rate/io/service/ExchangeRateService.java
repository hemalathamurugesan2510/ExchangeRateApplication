package com.exchange.rate.io.service;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.web.client.ResourceAccessException;

import com.exchange.rate.io.model.ExchangeRate;
import com.exchange.rate.io.model.ExchangeRateResponse;
import com.exchange.rate.io.model.ExchangeRateResponseDateCurrency;
import com.fasterxml.jackson.core.JsonProcessingException;

@Service
public interface ExchangeRateService {

	public String loadDataToDb();

	public String getExchangeRateByDateCurrencyFromDb(String date, String symbol) throws JsonProcessingException;

	public List<ExchangeRate> getAllDataFromDb();

	

	public ExchangeRateResponseDateCurrency getExchangeRateByDateCurrencyFromRateApi(String date, String symbol)
			throws ResourceAccessException, JsonProcessingException;

	public List<ExchangeRate> getExchangeRatesBetweenDates(String startDate);
	public String getExchangeRateFromApiCurrencyLoadtoDb(String symbol,String base);

}
