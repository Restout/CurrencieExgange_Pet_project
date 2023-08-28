package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.dto.ExchangeAmountDTO;
import org.example.dto.ExchangeDTO;
import org.example.model.Currency;
import org.example.model.ExchangeRate;
import org.example.service.CurrenciesService;
import org.example.service.ExchangeRateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.util.Optional;

@WebServlet("/exchange")
public class ExchangeServlet extends HttpServlet {
    DataSource dataSource;
    ExchangeRateService exchangeRateService;
    CurrenciesService currenciesService;
    ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        dataSource = org.example.DataSource.getDataSource();
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
        ExchangeDTO exchangeDTO = new ExchangeDTO(baseCurrencyCode, targetCurrencyCode, new BigDecimal(amount));
        Optional<BigDecimal> exchangeAmount = exchangeRateService.exchangeCurrencies(exchangeDTO);
        if (exchangeAmount.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        Currency baseCurrency = currenciesService.getCurrencyByCode(baseCurrencyCode).get();
        Currency targetCurrency = currenciesService.getCurrencyByCode(targetCurrencyCode).get();

        ExchangeAmountDTO exchangeAmountDTO = new ExchangeAmountDTO(new ExchangeRate(0, new BigDecimal(10), baseCurrency, targetCurrency), new BigDecimal(amount), exchangeAmount.get());
        writer.write(objectMapper.writeValueAsString(exchangeAmountDTO));

    }
}
