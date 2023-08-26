package org.example.service;

import org.example.model.ExcnahgeRate;
import org.example.repository.ExchangeRateRepository;

import java.util.List;

public class ExchangeRateService {
    ExchangeRateRepository exchangeRateRepository=new ExchangeRateRepository();
    public List<ExcnahgeRate> getListOfExchangeRates(){
        return exchangeRateRepository.getListOfExchangeRates();
    }
}
