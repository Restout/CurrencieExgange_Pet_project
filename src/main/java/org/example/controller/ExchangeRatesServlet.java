package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.model.ExcnahgeRate;
import org.example.service.ExchangeRateService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

@WebServlet("/exchangeRates")
public class ExchangeRatesServlet extends HttpServlet {
    ExchangeRateService exchangeRateService;

    @Override
    public void init() throws ServletException {
        exchangeRateService = new ExchangeRateService();

        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("txt/html");
        PrintWriter writer = resp.getWriter();
        ObjectMapper objectMapper = new ObjectMapper();
        List<ExcnahgeRate> result = exchangeRateService.getListOfExchangeRates();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);
        String jsonResult = objectMapper.writeValueAsString(result);
        writer.write(jsonResult);
    }
}
