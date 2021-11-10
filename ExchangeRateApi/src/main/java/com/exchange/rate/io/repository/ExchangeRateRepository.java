package com.exchange.rate.io.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.exchange.rate.io.model.ExchangeRate;


@Repository
public interface ExchangeRateRepository extends JpaRepository<ExchangeRate, Long> {

	@Query("select gbp FROM exchangerate WHERE date=:date")
	public String getExchangeRateFordateAndGbpSymbol(@Param(value = "date") String date);

	@Query("select hkd FROM exchangerate WHERE date=:date")
	public String getExchangeRateFordateAndHkdSymbol(@Param(value = "date") String date);

	@Query("select usd FROM exchangerate WHERE date=:date")
	public String getExchangeRateFordateAndUsdSymbol(@Param(value = "date") String date);

	@Query("FROM exchangerate where date BETWEEN :startDate AND :endDate")
	public List<ExchangeRate> findByDate(@Param(value = "startDate") String startDate,
			@Param(value = "endDate") String endDate);
	
	

}
