package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.exceptions.UniqueConstraintException;
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
    ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        currenciesService = new CurrenciesService();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        PrintWriter printWriter = resp.getWriter();
        List<Currency> currencies = currenciesService.getCurrencies();
        if (currencies.isEmpty()) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            printWriter.write("Nothing was Found");
        } else {
            String cursString = objectMapper.writeValueAsString(currencies);
            resp.setStatus(HttpServletResponse.SC_OK);
            printWriter.write(cursString);
        }
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        BufferedReader reader = req.getReader();
        StringBuilder requestBody = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            requestBody.append(line);
        }
        reader.close();
        Currency currency = objectMapper.readValue(requestBody.toString(), Currency.class);
        try {
            currenciesService.setNewCurrency(currency);
        } catch (UniqueConstraintException e) {
            resp.setStatus(409);
        } catch (Exception e) {
            resp.setStatus(500);
        }
    }
}
