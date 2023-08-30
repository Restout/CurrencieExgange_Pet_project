package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.dto.ExchangeAmountDTO;
import org.example.dto.ExchangeDTO;
import org.example.exceptions.EntityNotFoundException;
import org.example.model.Currency;
import org.example.service.CurrenciesService;
import org.example.service.ExchangeRateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    private ExchangeRateService exchangeRateService;
    private CurrenciesService currenciesService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        exchangeRateService = new ExchangeRateService();
        currenciesService = new CurrenciesService();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String baseCurrencyCode = req.getParameter("from");
        String targetCurrencyCode = req.getParameter("to");
        String amount = req.getParameter("amount");
        if (baseCurrencyCode == null || targetCurrencyCode == null || amount == null) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        BigDecimal ammountDeci = new BigDecimal(amount).setScale(2, RoundingMode.HALF_EVEN);
        ExchangeDTO exchangeDTO = new ExchangeDTO(baseCurrencyCode, targetCurrencyCode, ammountDeci);
        Optional<BigDecimal> exchangeAmount = exchangeRateService.exchangeCurrencies(exchangeDTO);
        if (exchangeAmount.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        BigDecimal rate = exchangeAmount.get().divide(ammountDeci).setScale(2, RoundingMode.HALF_EVEN);
        Currency baseCurrency = null;
        Currency targetCurrency = null;
        try {
            baseCurrency = currenciesService.getCurrencyByCode(baseCurrencyCode);
            targetCurrency = currenciesService.getCurrencyByCode(targetCurrencyCode);
        } catch (EntityNotFoundException e) {
           resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
           writer.write(e.getMessage());
        }

        ExchangeAmountDTO exchangeAmountDTO = new ExchangeAmountDTO(baseCurrency, targetCurrency, rate, ammountDeci, exchangeAmount.get());
        writer.write(objectMapper.writeValueAsString(exchangeAmountDTO));

    }
}
