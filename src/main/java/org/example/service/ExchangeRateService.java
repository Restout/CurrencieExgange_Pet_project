package org.example.service;

import org.example.dto.ExchangeDTO;
import org.example.dto.ExchangeRateDTO;
import org.example.model.ExchangeRate;
import org.example.repository.ExchangeRateRepository;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class ExchangeRateService {
    ExchangeRateRepository exchangeRateRepository = new ExchangeRateRepository();

    public List<ExchangeRate> getListOfExchangeRates() {
        return exchangeRateRepository.getListOfExchangeRates();
    }

    public Optional<ExchangeRate> getExchangeRatByBaseAndTargetCurrencies(String base, String target) {
        return exchangeRateRepository.getExchangeRatByBaseAndTargetCurrencies(base, target);
    }

    public Optional<ExchangeRate> putNewExchangeRate(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        return exchangeRateRepository.setNewExchangeRate(exchangeRateDTO);
    }

    public Optional<ExchangeRate> setNewRateToExistExchangeRate(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        return exchangeRateRepository.updateNewRateToExistExchangeRate(exchangeRateDTO);
    }

    public Optional<BigDecimal> exchangeCurrencies(ExchangeDTO exchangeDTO) {
        BigDecimal rate;
        if ((rate = findExchangeRateByCurrenciesCode(exchangeDTO.getBaseCode(), exchangeDTO.getTargetCode())) != null) {
            return Optional.of(rate.multiply(exchangeDTO.getAmount()));

        }
        if ((rate = findExchangeRateByCurrenciesCode(exchangeDTO.getTargetCode(), exchangeDTO.getBaseCode())) != null) {
            return Optional.of(rate.divide(exchangeDTO.getAmount()));

        }
        if ((rate = getExchangeRateByUSDRates(exchangeDTO.getBaseCode(), exchangeDTO.getTargetCode())) != null) {
            return Optional.of(rate.multiply(exchangeDTO.getAmount()));
        }
        return Optional.empty();
    }

    public BigDecimal getExchangeRateByUSDRates(String baseCode, String targetCode) {
        BigDecimal baseRate;
        BigDecimal targetRate;
        baseRate = findExchangeRateByCurrenciesCode("USD", baseCode);
        targetRate = findExchangeRateByCurrenciesCode("USD", baseCode);
        if (baseCode != null && targetCode != null) {
            return targetRate.divide(baseRate).setScale(2, RoundingMode.HALF_EVEN);
        }
        return null;

    }

    private BigDecimal findExchangeRateByCurrenciesCode(String baseCode, String targetCode) {
        BigDecimal rate = null;
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.getExchangeRatByBaseAndTargetCurrencies(baseCode, targetCode);
        if (exchangeRate.isPresent()) {
            rate = exchangeRate.get().getRate();
        }
        return rate;
    }
}
