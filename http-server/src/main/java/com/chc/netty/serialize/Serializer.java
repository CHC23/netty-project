package com.chc.netty.serialize;

public interface Serializer {
    /**
     * Java对象to二进制
     */
    byte[] serialize(Object object);

    /**
     * 二进制to Java对象
     */
    <T> T deserialize(Class<T> clazz,byte[] bytes);
}
