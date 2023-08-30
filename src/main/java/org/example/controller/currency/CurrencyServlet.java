package org.example.controller.currency;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import org.example.Utils;
import org.example.exceptions.EntityNotFoundException;
import org.example.model.Currency;
import org.example.service.CurrenciesService;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

@WebServlet("/currency/*")
public class CurrencyServlet extends HttpServlet {
    private CurrenciesService currenciesService;
    private ObjectMapper objectMapper;

    @Override
    public void init() throws ServletException {
        currenciesService = new CurrenciesService();
        objectMapper = new ObjectMapper();
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT); // Enable pretty-printing
        super.init();
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        PrintWriter writer = resp.getWriter();
        String[] params = Utils.getParametersOfRequestFromURL(req);
        if (params.length == 2) {
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        String currencyCode = params[2];
        Currency currency = null;
        try {
            currency = currenciesService.getCurrencyByCode(currencyCode);
        } catch (EntityNotFoundException e) {
            resp.setStatus(HttpServletResponse.SC_NOT_FOUND);
            writer.write(e.getMessage());
            return;
        }

        String currencyJson = objectMapper.writeValueAsString(currency);
        writer.write(currencyJson);
    }
}
