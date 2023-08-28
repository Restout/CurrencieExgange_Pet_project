package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.Utils;
import org.example.dto.ExchangeRateDTO;
import org.example.model.ExchangeRate;
import org.example.service.ExchangeRateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Optional;

@WebServlet("/exchangeRate/*")
public class ExchangeRateServlet extends HttpServlet {
    ExchangeRateService exchangeRateService;
    ObjectMapper objectMapper;


    @Override
    public void init() throws ServletException {
        exchangeRateService = new ExchangeRateService();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing
        super.init();
    }

    @Override
    protected void service(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        if (req.getMethod().equalsIgnoreCase("PATCH")) {
            doPatch(req, resp);
        } else {
            super.service(req, resp);
        }
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("txt/html");
        PrintWriter writer = resp.getWriter();
        String[] params = Utils.getParametersOfRequestFromURL(req);
        if (params.length <= 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String baseCurrency = params[2].substring(0, 3);
        String targetCurrency = params[2].substring(3);
        Optional<ExchangeRate> exchangeRateOptional = exchangeRateService.getExchangeRatByBaseAndTargetCurrencies(baseCurrency, targetCurrency);
        if (exchangeRateOptional.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            return;
        }
        writer.write(objectMapper.writeValueAsString(exchangeRateOptional.get()));
    }

    protected void doPatch(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        PrintWriter writer = resp.getWriter();
        BufferedReader reader = req.getReader();
        String requestBody = reader.readLine();
        if (requestBody == null && !requestBody.contains("rate=")) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String rate = requestBody.replaceAll("rate=", "");
        String[] params = Utils.getParametersOfRequestFromURL(req);
        if (params.length <= 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String baseCurrency = params[2].substring(0, 3);
        String targetCurrency = params[2].substring(3);
        ExchangeRateDTO exchangeRateDTO = new ExchangeRateDTO(baseCurrency, targetCurrency, new BigDecimal(rate));
        try {
            writer.write(objectMapper.writeValueAsString(exchangeRateService.setNewRateToExistExchangeRate(exchangeRateDTO)));
        } catch (SQLException e) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
        }


    }

}
