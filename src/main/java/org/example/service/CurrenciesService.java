package org.example.service;

import org.example.exceptions.UniqueConstraintException;
import org.example.model.Currency;
import org.example.repository.CurrenciesRepository;

import java.sql.SQLException;
import java.util.List;
import java.util.Optional;

public class CurrenciesService {
    private final CurrenciesRepository currenciesRepository;

    public CurrenciesService() {
        currenciesRepository = new CurrenciesRepository();
    }

    public List<Currency> getCurrencies() {
        return currenciesRepository.getCurrenciesList();
    }

    public Optional<Currency> getCurrencyByCode(String code) {
        return currenciesRepository.getCurrencyByCode(code);
    }

    public Optional<Currency> setNewCurrency(Currency currency) throws UniqueConstraintException {
        try {
            currenciesRepository.setNewCurrency(currency);
        } catch (SQLException e) {
            throw new UniqueConstraintException();
        }
        return getCurrencyByCode(currency.getCode());
    }
}
