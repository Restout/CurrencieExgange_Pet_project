package org.example.service;

import org.example.dto.ExchangeRateDTO;
import org.example.model.ExchangeRate;
import org.example.repository.ExchangeRateRepository;

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
    public  Optional<ExchangeRate> setNewRateToExistExchangeRate(ExchangeRateDTO exchangeRateDTO) throws SQLException {
        return exchangeRateRepository.setNewRateToExistExchangeRate(exchangeRateDTO);
    }
}
