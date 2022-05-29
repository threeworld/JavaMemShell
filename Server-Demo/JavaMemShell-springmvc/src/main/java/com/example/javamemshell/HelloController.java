package com.example.javamemshell;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import sun.misc.BASE64Decoder;

import java.io.*;
import javax.servlet.http.*;

@Controller
@RequestMapping(value = "/index")
public class HelloController {

    @GetMapping()
    public void index(HttpServletRequest request, HttpServletResponse response) throws Exception {
        response.getWriter().println("spring index controller");
    }

    @RequestMapping(value = "/readObject", method = RequestMethod.POST)
    public void readObjectInput(HttpServletRequest request, HttpServletResponse response) throws Exception{
        String input = request.getParameter("input").replace(" ", "+");
        if(input!=null) {
            byte[] bytes = new BASE64Decoder().decodeBuffer(input);
            ObjectInputStream ois = new ObjectInputStream(new ByteArrayInputStream(bytes));
            try {
                ois.readObject();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }
        String message = "input test";
        response.getWriter().println(message);
    }
}