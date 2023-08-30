package org.example.service;

import org.example.dto.ExchangeDTO;
import org.example.dto.ExchangeRateDTO;
import org.example.exceptions.EntityNotFoundException;
import org.example.model.Currency;
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
        return exchangeRateRepository.getAll();
    }

    public ExchangeRate getExchangeRateByBaseAndTargetCurrencies(String base, String target) throws EntityNotFoundException {
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.getExchangeRateByBaseAndTargetCurrencies(base, target);
        if(exchangeRate.isEmpty()){
            throw new EntityNotFoundException();
        }
        return exchangeRate.get();
    }

    public Optional<ExchangeRate> putNewExchangeRate(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate(0, exchangeRateDTO.getRate(), new Currency(exchangeRateDTO.getBaseCode()), new Currency(exchangeRateDTO.getTargetCode()));
        exchangeRateRepository.create(exchangeRate);
        return exchangeRateRepository.getExchangeRateByBaseAndTargetCurrencies(exchangeRateDTO.getBaseCode(), exchangeRateDTO.getTargetCode());
    }

    public ExchangeRate setNewRateToExistExchangeRate(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        ExchangeRate exchangeRate = new ExchangeRate(0, exchangeRateDTO.getRate(), new Currency(exchangeRateDTO.getBaseCode()), new Currency(exchangeRateDTO.getTargetCode()));
        Optional<ExchangeRate> result = exchangeRateRepository.getExchangeRateByBaseAndTargetCurrencies(exchangeRateDTO.getBaseCode(), exchangeRateDTO.getTargetCode());
        if (result.isEmpty()) {
            throw new EntityNotFoundException();
        }
        exchangeRateRepository.update(exchangeRate);
        return result.get();
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
        Optional<ExchangeRate> exchangeRate = exchangeRateRepository.getExchangeRateByBaseAndTargetCurrencies(baseCode, targetCode);
        if (exchangeRate.isPresent()) {
            rate = exchangeRate.get().getRate();
        }
        return rate;
    }
}
