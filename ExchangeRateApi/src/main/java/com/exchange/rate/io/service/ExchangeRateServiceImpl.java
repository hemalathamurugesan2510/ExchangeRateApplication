package com.exchange.rate.io.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

import com.exchange.rate.io.exception.ExchangeRateDataNotFoundException;
import com.exchange.rate.io.model.ExchangeRate;
import com.exchange.rate.io.model.ExchangeRateResponse;
import com.exchange.rate.io.model.ExchangeRateResponseDateCurrency;
import com.exchange.rate.io.model.ExchangeRateSymbol;
import com.exchange.rate.io.model.ExchangeRateSymbolResponse;

import com.exchange.rate.io.repository.ExchangeRateRepository;
import com.exchange.rate.io.repository.ExchangeRateSymbolRepository;
import com.exchangerate.constants.ExchangeRateConstants;
import com.exchangerate.utilities.ExchangeRateUtilities;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

@Service
public class ExchangeRateServiceImpl implements ExchangeRateService {

	public static final Logger LOGGER = (Logger) LogManager.getLogger(ExchangeRateServiceImpl.class);

	@Autowired
	private ExchangeRateRepository exchangeRateRepository;
	@Autowired
	private ExchangeRateSymbolRepository exchangeRateSymbolRepository;

	// ExchangeRateUtilities exchangeRateUtilities = new ExchangeRateUtilities();

	/**
	 * This method will fetch datas from External Rate Api and loaded values to Db
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */
	@Override
	public String loadDataToDb() {
		LOGGER.info("Fecthing Datas from Rate Api and loading into  Db");

		List<String> allDates = ExchangeRateUtilities.getDateList();
		for (String dates : allDates) {
			ExchangeRate rateObject = new ExchangeRate();

			rateObject = ExchangeRateUtilities.getExchangeRate(dates);

			if (rateObject == null) {
				throw new ExchangeRateDataNotFoundException();
			}

			exchangeRateRepository.save(rateObject);

		}

		return ExchangeRateConstants.SAVE_MESSAGE;
	}

	/**
	 * This method will fetch the datas from H2 Database and returning response to
	 * the user
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */

	@Override
	public List<ExchangeRate> getAllDataFromDb() {

		LOGGER.info("Fetching all values from Database");
		return exchangeRateRepository.findAll();
	}

	/**
	 * This method will fetch the datas from H2 Db by getting date and currency from
	 * the user
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */
	@Override
	public String getExchangeRateByDateCurrencyFromDb(String date, String symbol)
			throws JsonProcessingException, ClassCastException {

		LOGGER.info("Fetching values for the currency and date" + date + " Symbol" + symbol);
		LOGGER.info(symbol.toLowerCase());
		String exchangeRate = null;

		if (ExchangeRateUtilities.toLowerCase(symbol).equals("gbp")) {

			LOGGER.info("fetching exchangerate of the currency" + symbol);
			exchangeRate = exchangeRateRepository.getExchangeRateFordateAndGbpSymbol(date);
			System.out.println(exchangeRate + " " + symbol);
		} else if (ExchangeRateUtilities.toLowerCase(symbol).equals("hkd")) {
			LOGGER.info("fetching exchangerate of the currency" + symbol);
			exchangeRate = exchangeRateRepository.getExchangeRateFordateAndHkdSymbol(date);
			System.out.println(exchangeRate + " " + symbol);
		} else if (ExchangeRateUtilities.toLowerCase(symbol).equals("usd")) {
			LOGGER.info("fetching exchangerate of the currency" + symbol);
			exchangeRate = exchangeRateRepository.getExchangeRateFordateAndUsdSymbol(date);
			System.out.println(exchangeRate + " " + symbol);
		}

		if (exchangeRate == null) {
			throw new ExchangeRateDataNotFoundException();
		}

		return exchangeRate;
	}

	/**
	 * This method will fetch the datas from Rate Api
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */

	@Override
	public ExchangeRateResponseDateCurrency getExchangeRateByDateCurrencyFromRateApi(String date, String symbol)
			throws ResourceAccessException, JsonProcessingException {

		LOGGER.info("Fetching values for the date" + date + " Symbol" + symbol);
		LOGGER.info(symbol.toLowerCase());
		// String exchangeRate = null;
		String url = ExchangeRateConstants.API_URL + date + ExchangeRateConstants.ACCESS_URL
				+ ExchangeRateConstants.ACCESS_KEY + ExchangeRateConstants.SYMBOL + symbol;
		LOGGER.info("URL" + url);
		RestTemplate restTemplate = new RestTemplate();
		ExchangeRateResponseDateCurrency result = restTemplate.getForObject(url,
				ExchangeRateResponseDateCurrency.class);

		LOGGER.info("Result" + result);
		if (result == null) {
			throw new ExchangeRateDataNotFoundException();
		}

		return result;

	}

	/**
	 * This method will fetch the datas between the dates from H2 Db
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */
	public List<ExchangeRate> getExchangeRatesBetweenDates(String startDate) {

		String todayDate = ExchangeRateUtilities.todayDate();
		LOGGER.info("Fetching values for the currency between the dates" + startDate + " Symbol" + todayDate);
		List<ExchangeRate> exchangeRateList = exchangeRateRepository.findByDate(startDate, todayDate);
		if (exchangeRateList == null) {
			throw new ExchangeRateDataNotFoundException();
		}

		return exchangeRateList;
	}

	public String getExchangeRateFromApiCurrencyLoadtoDb(String symbol, String base) {

		LOGGER.info("Fecthing Datas from Rate Api for the past 12 months of the currency and loading into  Db");

		List<String> allDates = ExchangeRateUtilities.getDateList();
		for (String dates : allDates) {
			String symbolRate = symbol;

			ExchangeRateSymbol exchangeRateObject = ExchangeRateUtilities.getExchangeRateBycurrencyLoadtoDb(symbol,
					dates, base);

			if (exchangeRateObject == null) {
				throw new ExchangeRateDataNotFoundException();
			}

			exchangeRateSymbolRepository.save(exchangeRateObject);

		}

		return ExchangeRateConstants.SAVE_CURRENCY_EXCHANGE_RATE;
	}

}
