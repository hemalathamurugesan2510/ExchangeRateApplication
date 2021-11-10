package com.exchangerate.utilities;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import com.exchange.rate.io.controller.ExchangeRateController;
import com.exchange.rate.io.model.ExchangeRate;
import com.exchange.rate.io.model.ExchangeRateResponse;
import com.exchange.rate.io.model.ExchangeRateSymbol;
import com.exchange.rate.io.repository.ExchangeRateRepository;
import com.exchangerate.constants.ExchangeRateConstants;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

public class ExchangeRateUtilities {

	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateUtilities.class);

	@Autowired
	ExchangeRateRepository exchangeRateRepository;

	/**
	 * This method will return date list for 12 months and formatting and validates
	 * date
	 * 
	 * @return
	 */
	public static List<String> getDateList() {

		List<String> allDates = new ArrayList<>();
		String maxDate = "2021-11-01";
		SimpleDateFormat monthDate = new SimpleDateFormat("yyyy-MM-dd");
		Calendar cal = Calendar.getInstance();
		try {
			cal.setTime(monthDate.parse(maxDate));
		} catch (ParseException e) {
			e.printStackTrace();
		}
		for (int i = 1; i <= 12; i++) {
			String month_name1 = monthDate.format(cal.getTime());
			allDates.add(month_name1);
			cal.add(Calendar.MONTH, -1);
		}

		return allDates;

	}

	/**
	 * 
	 * This method will return current date
	 * 
	 * @return
	 */

	public static String todayDate() {
		DateTimeFormatter dateformat = DateTimeFormatter.ofPattern("yyyy-mm-dd");
		LocalDateTime currentDate = LocalDateTime.now();
		return dateformat.format(currentDate);
	}

	/*
	 * This method returns exchange rate values for 12 months
	 */
	public static ExchangeRate getExchangeRate(String dates) {

		String exchangeRateUrl = ExchangeRateConstants.API_URL + dates + ExchangeRateConstants.ACCESS_URL
				+ ExchangeRateConstants.ACCESS_KEY + ExchangeRateConstants.SYMBOLS;
		LOGGER.info("URL of loadDataToDb" + exchangeRateUrl);
		RestTemplate restTemplate = new RestTemplate();
		ExchangeRateResponse result = restTemplate.getForObject(exchangeRateUrl, ExchangeRateResponse.class);
		LOGGER.info("Response {}" + result);
		ExchangeRate rateObject = new ExchangeRate();

		rateObject.setDate(result.getDate());
		rateObject.setBase(result.getBase());
		rateObject.setGbp(result.getRates().getGbp());
		rateObject.setHkd(result.getRates().getHkd());
		rateObject.setUsd(result.getRates().getUsd());

		return rateObject;
	}

	public static String toLowerCase(String symbol) {
		String currency = symbol.toLowerCase();
		return currency;
	}

	/*
	 * This method returns exchange rates for the currency as base currency for past
	 * 12 months
	 */
	public static ExchangeRateSymbol getExchangeRateBycurrencyLoadtoDb(String symbol, String dates, String base) {

		String symbolRate = symbol;
		String exchangeRateUrl = ExchangeRateConstants.API_URL + dates + ExchangeRateConstants.ACCESS_URL
				+ ExchangeRateConstants.ACCESS_KEY + "&base=" + base + ExchangeRateConstants.SYMBOL + symbol;
		LOGGER.info("URL of getExchangeRateFromApiCurrency" + exchangeRateUrl);

		RestTemplate restTemplate = new RestTemplate();
		JsonNode result = restTemplate.getForObject(exchangeRateUrl, JsonNode.class);
		JsonNode rate1 = result.get("rates");

		ObjectMapper objectMapper = new ObjectMapper();
		Map<String, Double> rateMap = objectMapper.convertValue(rate1, Map.class);

		Set<Map.Entry<String, Double>> entry = rateMap.entrySet();
		ExchangeRateSymbol exchangeRateObject = new ExchangeRateSymbol();
		// Map<String, Double> rate_map =
		// entry.stream().collect(Collectors.toMap(Entry::getKey, Entry::getValue));
		for (Map.Entry<String, Double> entry1 : rateMap.entrySet()) {

			Double excRate = entry1.getValue();

			exchangeRateObject.setBase(base);
			exchangeRateObject.setDate(dates);
			exchangeRateObject.setExchangerate(excRate);

		}
		return exchangeRateObject;

	}
}
