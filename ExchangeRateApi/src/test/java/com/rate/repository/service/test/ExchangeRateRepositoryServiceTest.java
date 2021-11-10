package com.rate.repository.service.test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.when;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;
import org.springframework.boot.SpringBootConfiguration;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.web.client.ResourceAccessException;

import com.exchange.rate.io.ExchangeRateApplication;
import com.exchange.rate.io.exception.ExchangeRateDataNotFoundException;
import com.exchange.rate.io.model.ExchangeRate;
import com.exchange.rate.io.model.ExchangeRateResponseDateCurrency;
import com.exchange.rate.io.model.ExchangeRateSymbol;
import com.exchange.rate.io.repository.ExchangeRateRepository;
import com.exchange.rate.io.repository.ExchangeRateSymbolRepository;
import com.exchange.rate.io.service.ExchangeRateService;
import com.exchange.rate.io.service.ExchangeRateServiceImpl;
import com.exchangerate.constants.ExchangeRateConstants;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = ExchangeRateRepositoryServiceTest.class)
@ExtendWith(SpringExtension.class)

public class ExchangeRateRepositoryServiceTest {

	@Mock
	ExchangeRateRepository exchangeRateRepository;
	ExchangeRateSymbolRepository exchangeRateSymbolRepository;
	private AutoCloseable closeable;
	@Mock
	private static ObjectMapper obj = new ObjectMapper();

	@Spy // mock it partially
	@InjectMocks
	private ExchangeRateService exchangeRateService = new ExchangeRateServiceImpl();

	@BeforeEach
	void initService() {
		closeable = MockitoAnnotations.openMocks(this);
	}

	@AfterEach
	void closeService() throws Exception {
		closeable.close();
	}

	@Test
	public void getExchangeRateDateCurrencyFromRateApiTest() throws ResourceAccessException, JsonProcessingException {
		ExchangeRateResponseDateCurrency exchangeRateResponseDateCurrency = new ExchangeRateResponseDateCurrency();

		exchangeRateResponseDateCurrency.setSuccess(true);
		exchangeRateResponseDateCurrency.setTimestamp(1631231999);
		exchangeRateResponseDateCurrency.setDate("2021-09-09");
		exchangeRateResponseDateCurrency.setHistorical(true);
		Map<String, Double> rateMap = new HashMap<String, Double>();
		rateMap.put("GBP", 0.854423);
		exchangeRateResponseDateCurrency.setRates(rateMap);

		String date = "2021-09-09";
		String symbol = "gbp";
		Map<String, Double> rates1 = new HashMap<String, Double>();
		double rate = 0.854423;
		try {
			when(exchangeRateService.getExchangeRateByDateCurrencyFromRateApi(date, symbol))
					.thenReturn(exchangeRateResponseDateCurrency);
		} catch (ResourceAccessException e) { // TODO Auto-generated catch block
			e.printStackTrace();
		} catch (JsonProcessingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		ExchangeRateResponseDateCurrency exc = exchangeRateService.getExchangeRateByDateCurrencyFromRateApi(date,
				symbol);
		assertEquals("2021-09-09", exc.getDate());
		double rate1 = exc.getRates().get("GBP");
		System.out.println("hello");
		assertNotNull(exc);

	}

	@Test
	public void loadDataToDbTest() {
		String expectedResult = "Datas are loaded into DB successfully";
		String message = exchangeRateService.loadDataToDb();

		assertEquals(message, expectedResult);

	}

	@Test
	public void getAllDataFromDbTest() {
		List<ExchangeRate> exchange = exchangeRateService.getAllDataFromDb();
		assertNotNull(exchange);
	}

	@Test
	public void getExchangeRateByDateCurrencyFromDbTest() throws JsonProcessingException {

		ExchangeRate exchangeRate = new ExchangeRate();

		exchangeRate.setGbp(1.8054423);
		String date = "2021-04-01";
		String symbol = "GBP";

		String exchange = exchangeRate.toString();
		System.out.println(exchange);
		String exchange_rateResult = null;
		when(exchangeRateRepository.getExchangeRateFordateAndGbpSymbol(date)).thenReturn(exchange);
		exchange_rateResult = exchangeRateRepository.getExchangeRateFordateAndGbpSymbol(date);
		System.out.println(exchange);
		assertEquals(exchange, exchange_rateResult);

	}

	@Test
	public void getExchangeRateByDateCurrencyFromDbBetweenDates() throws JsonProcessingException {

		List<ExchangeRate> exchangeRateList = new ArrayList();
		ExchangeRate exchangeRate = new ExchangeRate();
		exchangeRate.setId((long) 1);
		exchangeRate.setDate("2021-09-09");
		exchangeRate.setBase("EUR");
		exchangeRate.setGbp(0.854423);

		ExchangeRate exchangeRate1 = new ExchangeRate();
		exchangeRate.setId((long) 1);
		exchangeRate.setDate("2021-09-09");
		exchangeRate.setBase("EUR");
		exchangeRate.setGbp(0.854423);
		exchangeRateList.add(exchangeRate);
		exchangeRateList.add(exchangeRate1);

		String startDate = "2021-09-07";
		String endDate = "2021-09-09";

		List<ExchangeRate> exchangeRateList1 = exchangeRateService.getExchangeRatesBetweenDates(startDate);

		assertNotNull(exchangeRateList1);

	}

}
