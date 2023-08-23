package org.example;

import org.example.model.Currency;
import org.example.repository.CurrenciesRepository;

import java.util.List;


public class Main {
    public static void main(String[] args) {
        CurrenciesRepository currenciesRepository=new CurrenciesRepository();
Iterable<Currency>currencyList =currenciesRepository.getCurrenciesList();


        System.out.println("Hello world!");
    }
}