package com.threed.payloads;

/**
 * @className: ObjectPayload
 * @description: TODO 类描述
 * @author: two_day
 * @date: 2022/5/11
 **/

@SuppressWarnings ( "rawtypes" )
public interface ObjectPayload <T> {
    public T getObject(byte[] classBytes) throws Exception;
}
