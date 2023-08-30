package org.example.service;

import org.example.exceptions.EntityNotFoundException;
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
        return currenciesRepository.getAll();
    }

    public Currency getCurrencyByCode(String code) throws EntityNotFoundException {
        Optional<Currency> optionalCurrency = currenciesRepository.getCurrencyByCode(code);
        if(optionalCurrency.isEmpty()){
            throw new EntityNotFoundException();
        }
        return optionalCurrency.get();
    }

    public Currency setNewCurrency(Currency currency) throws UniqueConstraintException, EntityNotFoundException {
        try {
            currenciesRepository.create(currency);
        } catch (SQLException e) {
            throw new UniqueConstraintException();
        }
        return getCurrencyByCode(currency.getCode());
    }
}
