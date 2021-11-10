package com.exchange.rate.io.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exchange.rate.io.model.ExchangeRate;
import com.exchange.rate.io.model.ExchangeRateSymbol;

@Repository
public interface ExchangeRateSymbolRepository extends JpaRepository<ExchangeRateSymbol, Long> {

	

}
