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
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    CurrenciesService currenciesService;

    @Override
    public void init() throws ServletException {
        currenciesService = new CurrenciesService();
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("txt/html");
        PrintWriter writer = resp.getWriter();
        String request = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = request.substring(contextPath.length());
        String[] params = path.split("/");
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
        ObjectMapper objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing
        String currencyJson = objectMapper.writeValueAsString(currencyOptional.get());
        writer.write(currencyJson);
    }
}
