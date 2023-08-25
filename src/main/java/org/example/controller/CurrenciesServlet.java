package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.model.Currency;
import org.example.service.CurrenciesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
    CurrenciesService currenciesService;

    @Override
    public void init() throws ServletException {
        currenciesService = new CurrenciesService();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("txt/html");

        PrintWriter printWriter = resp.getWriter();
        List<Currency> currencies = currenciesService.getCurrencies();
        if (currencies.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("Nothing was Found");
        } else {
            ObjectMapper objectMapper = new ObjectMapper();
            objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing
            String cursString = objectMapper.writeValueAsString(currencies);
            resp.setStatus(HttpServletResponse.SC_OK);
            printWriter.write(cursString);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
        resp.setContentType("application/json");
        BufferedReader reader = req.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        ObjectMapper objectMapper = new ObjectMapper();
        Currency currency = objectMapper.readValue(requestBody.toString(), Currency.class);
currenciesService.setNewCurrency(currency);
    }
}
