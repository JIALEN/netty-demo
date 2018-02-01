package com.alen.netty.serial.factory;

import com.alen.netty.serial.Serializer;

/**
 * 编解码工具类工厂类
 */
public class SerializerFactory {

    public static <T> Serializer getSerializer(Class<T> t){
        Serializer  serializer=null;
        try {
           serializer= (Serializer) t.newInstance();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return serializer;
    }
}
