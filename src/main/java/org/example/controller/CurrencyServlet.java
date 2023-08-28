package org.example.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.Utils;
import org.example.model.Currency;
import org.example.service.CurrenciesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
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
        PrintWriter writer = resp.getWriter();
        String[] params = Utils.getParametersOfRequestFromURL(req);
        if(params.length==2){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String currencyCode = params[2];
        Optional<Currency> currencyOptional = currenciesService.getCurrencyByCode(currencyCode);
        if(currencyOptional.isEmpty()){
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.write("No Currency with given code");
            return;
        }
        String currencyJson = objectMapper.writeValueAsString(currencyOptional.get());
        writer.write(currencyJson);
    }
}
