package com.alen.netty.serial;

import java.io.IOException;

/**
 * 编解码接口
 */
public interface Serializer {

    byte[] encode(Object msg) throws IOException;

    <T> T decode(byte[] buf, Class<T> type) throws IOException;
}
