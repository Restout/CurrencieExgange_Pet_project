package org.example;

import javax.servlet.http.HttpServletRequest;

public class Utils {
    public static String[] getParametersOfRequestFromURL(HttpServletRequest req) {
        String request = req.getRequestURI();
        String contextPath = req.getContextPath();
        String path = request.substring(contextPath.length());
        return path.split("/");
    }
}
