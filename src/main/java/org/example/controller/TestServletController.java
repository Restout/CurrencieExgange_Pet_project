package org.example.controller;

import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.http.HttpRequest;

@WebServlet("/hello")
public class TestServletController extends HttpServlet{
    @Override
    protected void doGet( HttpServletRequest request,HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter printWriter=response.getWriter();
        printWriter.write("Hello");
        response.addHeader("HelloHead","HelloWorld!!!");
    }

}
