package com.exchange.rate.io.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.exchange.rate.io.exception.ExchangeRateDataNotFoundException;
import com.exchange.rate.io.exception.InvalidInputException;
import com.exchange.rate.io.model.ExchangeRate;
import com.exchange.rate.io.model.ExchangeRateResponse;
import com.exchange.rate.io.model.ExchangeRateResponseDateCurrency;
import com.exchange.rate.io.service.ExchangeRateService;
import com.fasterxml.jackson.core.JsonProcessingException;

@RestController
@RequestMapping("/exchange_rate")
public class ExchangeRateController {
	private static final Logger LOGGER = LoggerFactory.getLogger(ExchangeRateController.class);

	@Autowired
	ExchangeRateService exchangeRateService;

	/**
	 * This method will fetch currenicies from External Rate Api and loaded into
	 * Embedded H2 Db
	 * 
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/get_exchangerates/all_currencies/from_api_to_db")
	public ResponseEntity<String> getValuesFromrateIo() throws JsonProcessingException {
		LOGGER.info("ExchangeRateController : fetching exchange rate of the currencies from rate api");
		String exchangeRateServiceResponse = exchangeRateService.loadDataToDb();
		LOGGER.info(" Response of get values from rate api{} :" + exchangeRateServiceResponse);
		return new ResponseEntity<>(exchangeRateServiceResponse, HttpStatus.OK);

	}

	/**
	 * This method fetch exchange rates from Rate Api for the pas 12 months of the
	 * currency and loaded into Db
	 * 
	 * @param symbol
	 * @param base
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/get_exchangerates/currency/from_api/load_to_db")
	public ResponseEntity<String> getExchangeRatesFromrateApiByCurrencyLoadToDb(@RequestParam String symbol,
			@RequestParam String base) throws JsonProcessingException {
		LOGGER.info("ExchangeRateController : fetching exchange rate of the currency for past 12 months from rate api");
		String exchangeRateSymbolResponse = exchangeRateService.getExchangeRateFromApiCurrencyLoadtoDb(symbol, base);
		LOGGER.info(" Response of get values from rate api{} :" + exchangeRateSymbolResponse);
		return new ResponseEntity<>(exchangeRateSymbolResponse, HttpStatus.OK);

	}

	/**
	 * This method will fetch values from H2 Db between the dates and return the
	 * request to the user
	 * 
	 * @param startDate
	 * @param endDate
	 * @return
	 * @throws JsonProcessingException
	 */

	@GetMapping("/get_exchangerates/between_dates")
	public ResponseEntity<List<ExchangeRate>> getDatasBetweenDates(@RequestParam String startDate) throws JsonProcessingException {
		LOGGER.info("Fetching values between dates from Db : ");

		List<ExchangeRate> exchangeRateList = exchangeRateService.getExchangeRatesBetweenDates(startDate);
		LOGGER.info("Datas between the dates {}" + exchangeRateList);

		if (exchangeRateList == null) {
			throw new ExchangeRateDataNotFoundException();
		}

		return new ResponseEntity<List<ExchangeRate>>(exchangeRateList, HttpStatus.OK);

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

	@GetMapping("/get_exchangerates/from_db/by_date_currency")
	public ResponseEntity<String> getExchangeRateByDateCurrencyFromDb(@RequestParam String date,
			@RequestParam String symbol) throws JsonProcessingException {

		LOGGER.info("Getting values from rate api of the currency" + symbol + " for date" + date);
		if (date == null) {
			throw new InvalidInputException();
		}

		String exchange_rate = exchangeRateService.getExchangeRateByDateCurrencyFromDb(date, symbol);

		if (exchange_rate == null) {
			throw new ExchangeRateDataNotFoundException();
		}

		LOGGER.info(" ExchangeRate by date and currency is {} " + exchange_rate);
		return new ResponseEntity<>("The Exchange Rate of is " + exchange_rate + " as base EUR", HttpStatus.OK);

	}

	/**
	 * This method will fetch all currencies from api
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */

	/**
	 * This method will fetch the datas from Rate Api by getting date and currency
	 * from the user from the user
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */
	@GetMapping("/get_exchangerates/by_date_currency/from_rate_api")
	public ResponseEntity<String> getExchangeRateByDateCurrencyFromRateApi(@RequestParam String date,
			@RequestParam String symbol) throws JsonProcessingException {

		LOGGER.info("Fetching values from rate api by date and currency ");
		ExchangeRateResponseDateCurrency exchangerateCurrency = exchangeRateService
				.getExchangeRateByDateCurrencyFromRateApi(date, symbol);
		LOGGER.info("Exchangerate by date and currency is  {}" + exchangerateCurrency);

		Map<String, Double> rateMap = new HashMap();
		rateMap = exchangerateCurrency.getRates();
		String value = rateMap.toString();

		if (exchangerateCurrency == null) {
			throw new ExchangeRateDataNotFoundException();
		}

		return new ResponseEntity<>("The coversion Rate of  as base EUR is" + value, HttpStatus.OK);

	}

	/**
	 * This method will fetch all datas from H2 Db
	 * 
	 * @param date
	 * @param symbol
	 * @return
	 * @throws JsonProcessingException
	 */

	@GetMapping("/get_all_datas/from_db")
	public ResponseEntity<List<ExchangeRate>> getAllDataFromDb() throws JsonProcessingException {
		LOGGER.info("Fetching all values from Database ");
		List<ExchangeRate> exchangeRateList = exchangeRateService.getAllDataFromDb();
		LOGGER.info("Fetching all values from Db : Response {}" + exchangeRateList);

		if (exchangeRateList == null) {
			throw new ExchangeRateDataNotFoundException();
		}

		return new ResponseEntity<List<ExchangeRate>>(exchangeRateList, HttpStatus.OK);

	}
}
