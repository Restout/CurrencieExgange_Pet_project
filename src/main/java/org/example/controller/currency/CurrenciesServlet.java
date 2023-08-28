package org.example.controller.currency;

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
import java.util.Optional;

@WebServlet("/currencies")
public class CurrenciesServlet extends HttpServlet {
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
        BufferedReader reader = req.getReader();
     String id=req.getParameter("id");
        String name=req.getParameter("name");
        String code=req.getParameter("code");
        String sign=req.getParameter("sign");
        if(id==null||name==null||code==null||sign==null){
            resp.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            return;
        }
        Currency currency=new Currency(Integer.valueOf(id),code,name,sign);
        try {
            Optional<Currency> currencyOptional = currenciesService.setNewCurrency(currency);
            PrintWriter writer = resp.getWriter();
            writer.write(objectMapper.writeValueAsString(currencyOptional.get()));
        } catch (UniqueConstraintException e) {
            resp.setStatus(409);
        } catch (Exception e) {
            resp.setStatus(500);
        }

    }
}
