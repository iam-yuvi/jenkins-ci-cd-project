package com.example;

import jakarta.servlet.http.*;
import jakarta.servlet.annotation.WebServlet;
import java.io.IOException;


@WebServlet("/")
public class HelloWorld extends HttpServlet {

    private String unusedField = "I am never used"; // ❌ Unused field
    private String $pass = ""; // ❌ Unused field
    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws IOException {
        resp.setContentType("text/plain");
        resp.getWriter().write("Hello, THis is Your First Devops demo Project..!");

        // ❌ Hardcoded credentials (Security issue)
        String password = $pass;

        // ❌ Empty catch block
        try {
            int x = 1 / 0;
        } catch (Exception e) {
            // ignored
        }

        // ❌ TODO comment
        // TODO: improve this logic later
    }
}
