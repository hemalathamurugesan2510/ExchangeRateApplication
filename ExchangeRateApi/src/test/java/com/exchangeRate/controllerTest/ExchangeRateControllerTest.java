
package com.exchangeRate.controllerTest;

import java.util.ArrayList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.junit.Before;
import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.engine.TestExecutionResult.Status;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.mockito.junit.MockitoJUnitRunner;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.RequestBuilder;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import com.exchange.rate.io.controller.ExchangeRateController;
import com.exchange.rate.io.model.ExchangeRate;
import com.exchange.rate.io.model.ExchangeRateResponse;
import com.exchange.rate.io.model.ExchangeRateResponseDateCurrency;
import com.exchange.rate.io.model.ExchangeRateSymbol;
import com.exchange.rate.io.model.Rates;
import com.exchange.rate.io.service.ExchangeRateService;
import com.exchange.rate.io.service.ExchangeRateServiceImpl;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(MockitoJUnitRunner.class)

@SpringBootTest(classes = ExchangeRateControllerTest.class)
@ExtendWith(SpringExtension.class)

public class ExchangeRateControllerTest {

	private MockMvc mockMvc;

	@Mock
	private ExchangeRateService exchangeRateService;

	@InjectMocks
	ExchangeRateController exchangeRateController;
	@Mock
	private static ObjectMapper obj = new ObjectMapper();

	@Before
	public void setup() {
		mockMvc = MockMvcBuilders.standaloneSetup(exchangeRateController).build();
		MockMvcBuilders.standaloneSetup(exchangeRateController).build();
	}

	@Test
	public void getValuesFromrateIoTest() throws Exception {

		ExchangeRateResponse exchangeRateresponse = new ExchangeRateResponse();
		exchangeRateresponse.setBase("eur");
		exchangeRateresponse.setDate("2021-01-01");
		exchangeRateresponse.setSuccess(true);
		exchangeRateresponse.setTimestamp(1609545599);

		Rates rate = new Rates();
		rate.setGbp(0.890496);
		exchangeRateresponse.setRates(rate);
		String exchangeRateresponse1 = exchangeRateresponse.toString();
		Mockito.when(exchangeRateService.loadDataToDb()).thenReturn(exchangeRateresponse.toString());

		MvcResult result = mockMvc.perform(get("/exchange_rate/get_exchangerates/all_currencies/from_api_to_db"))
				.andReturn();
		String exchange_RateResponse = result.getResponse().getContentAsString();
		assertEquals(exchangeRateresponse1, exchange_RateResponse);

	}

	@Test
	public void getAllDataFromDbTest() throws Exception {

		MvcResult result = mockMvc.perform(get("/exchange_rate/get_all_datas/from_db")).andExpect(status().isOk())
				.andReturn();
		int response = result.getResponse().getContentLength();
		assertNotNull(response);

	}

	@Test
	public void getExchangeRateByDateCurrencyFromDbTest() throws Exception {

		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setBase("eur");
		exchangeRate.setDate("2021-01-01");
		exchangeRate.setUsd(0.890496);

		String exchangeRate1 = exchangeRate.toString();

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("date", "2021-01-01");
		requestParams.add("symbol", "USD");
		Mockito.when(exchangeRateService.getExchangeRateByDateCurrencyFromDb(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(exchangeRate1);
		MvcResult result = mockMvc
				.perform(get("/exchange_rate/get_exchangerates/from_db/by_date_currency").params(requestParams))
				.andExpect(status().isOk()).andReturn();

	}

	@Test
	public void getExchangeRateByDateCurrencyFromRateApiTest() throws Exception

	{

		ExchangeRateResponseDateCurrency exchangeRateCurrency = new ExchangeRateResponseDateCurrency();
		exchangeRateCurrency.setBase("eur");
		exchangeRateCurrency.setDate("2021-01-01");
		exchangeRateCurrency.setHistorical(true);
		exchangeRateCurrency.setSuccess(true);
		exchangeRateCurrency.setTimestamp(1631231999);
		Map<String, Double> rates = new HashMap();
		rates.put("USD", 1.182453);
		exchangeRateCurrency.setRates(rates);

		String date = "2021-01-01";
		String symbol = "USD";

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("date", "2021-01-01");
		requestParams.add("symbol", "USD");
		Mockito.when(
				exchangeRateService.getExchangeRateByDateCurrencyFromRateApi(Mockito.anyString(), Mockito.anyString()))
				.thenReturn(exchangeRateCurrency);

		mockMvc.perform(get("/exchange_rate/get_exchangerates/by_date_currency/from_rate_api").params(requestParams))
				.andExpect(status().isOk());

	}

	@Test
	public void getDatasBetweenDatesTest() throws Exception {

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("startDate", "2021-01-01");
		requestParams.add("endDate", "2021-04-01");

		MvcResult result = mockMvc.perform(get("/exchange_rate/get_exchangerates/between_dates").params(requestParams))
				.andReturn();
		int status = result.getResponse().getStatus();
		assertEquals(200, status);
	}

	@Test
	public void getExchangeRatesFromrateApiByCurrencyLoadToDbTest() throws Exception {

		LinkedMultiValueMap<String, String> requestParams = new LinkedMultiValueMap<>();
		requestParams.add("symbol", "GBP");
		requestParams.add("base", "EUR");

		MvcResult result = mockMvc
				.perform(get("/exchange_rate/get_exchangerates/currency/from_api/load_to_db").params(requestParams))
				.andReturn();
		result.getResponse().getStatus();
		assertEquals(200, result.getResponse().getStatus());

	}
}
