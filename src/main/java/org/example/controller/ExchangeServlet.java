package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.dto.ExchangeAmountDTO;
import org.example.dto.ExchangeDTO;
import org.example.model.ExchangeRate;
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
    ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        dataSource = org.example.DataSource.getDataSource();
        exchangeRateService = new ExchangeRateService();
        objectMapper = new ObjectMapper();

        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
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
        Optional<ExchangeRate> exchangeRate = exchangeRateService.getExchangeRatByBaseAndTargetCurrencies(baseCurrencyCode, targetCurrencyCode);
        ExchangeAmountDTO exchangeAmountDTO = new ExchangeAmountDTO(exchangeRate.get(), new BigDecimal(amount), exchangeAmount.get());
        PrintWriter writer = resp.getWriter();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        writer.write(objectMapper.writeValueAsString(exchangeAmountDTO));

    }
}
