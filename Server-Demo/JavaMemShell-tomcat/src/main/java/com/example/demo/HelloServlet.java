package com.example.demo;

import sun.misc.BASE64Decoder;

import java.io.*;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.*;

@WebServlet(name = "helloServlet", value = "/readObject")
public class HelloServlet extends HttpServlet {
    private String message;

    public void init() {
        message = "Hello World!";
    }

    public void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String message = "tomcat index servlet test";
        String id      = request.getParameter("id");

        StringBuilder sb = new StringBuilder();
        sb.append(message);
        if (id != null && !id.isEmpty()) {
            sb.append("\nid: ").append(id);
        }

        response.getWriter().println(sb);
    }

    public void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException{
        String input = req.getParameter("input").replace(" ", "+");
        System.out.println(input);
        if(input!=null) {
            byte[] bytes = new BASE64Decoder().decodeBuffer(input);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            try {
                ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

    }
    public void destroy() {
    }
}