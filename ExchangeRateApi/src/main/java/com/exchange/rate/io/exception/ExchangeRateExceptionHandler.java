package com.exchange.rate.io.exception;

import java.time.LocalDateTime;
import java.util.LinkedHashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.exchangerate.constants.ExchangeRateConstants;

@ControllerAdvice
public class ExchangeRateExceptionHandler {


	@ExceptionHandler(value = Exception.class)
	public ResponseEntity<Object> generalException(Exception exception) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ExchangeRateConstants.SERVER_ISSUE);

		return new ResponseEntity<>(body, HttpStatus.INTERNAL_SERVER_ERROR);
	}

	@ExceptionHandler(value = ExchangeRateDataNotFoundException.class)
	public ResponseEntity<Object> exchangeRateDataNotFoundException(ExchangeRateDataNotFoundException exception) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ExchangeRateConstants.DATA_NOT_FOUND);

		return new ResponseEntity<>(body, HttpStatus.NOT_FOUND);
	}
	
	@ExceptionHandler(value = InvalidInputException.class)
	public ResponseEntity<Object> invalidInputException(InvalidInputException exception) {

		Map<String, Object> body = new LinkedHashMap<>();
		body.put("timestamp", LocalDateTime.now());
		body.put("message", ExchangeRateConstants.INVALID_INPUT);

		return new ResponseEntity<>(body, HttpStatus.BAD_REQUEST);
	}
	

}
