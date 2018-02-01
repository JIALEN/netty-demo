package com.alen.netty.serial.ende;


import com.alen.netty.serial.Serializer;
import com.alen.netty.serial.factory.SerializerFactory;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.LengthFieldBasedFrameDecoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

/**
 * netty解码实现类
 * @param <T>
 * @param <K>
 */
public class NettyMessageDecoder<T, K extends Serializer> extends LengthFieldBasedFrameDecoder {

    private Logger logger = LoggerFactory.getLogger(getClass());
    //判断传送客户端传送过来的数据是否按照协议传输，头部信息的大小应该是 byte+byte+int = 1+1+4 = 6
    private static final int HEADER_SIZE = 6;

    private Serializer serializer;
    private Class<T> clazz;

    public NettyMessageDecoder(Class<T> clazz, Class<K> serial, int maxFrameLength, int lengthFieldOffset,
                               int lengthFieldLength) throws IOException {
        super(maxFrameLength, lengthFieldOffset, lengthFieldLength);
        this.clazz = clazz;

        this.serializer = SerializerFactory.getSerializer(serial);
    }

    @Override
    protected Object decode(ChannelHandlerContext ctx, ByteBuf in)
            throws Exception {

        if (in.readableBytes() < HEADER_SIZE) {
            return null;
        }
        in.markReaderIndex();
        //注意在读的过程中，readIndex的指针也在移动
        byte type = in.readByte();
        byte flag = in.readByte();
        int dataLength = in.readInt();
        if (in.readableBytes() < dataLength) {
            logger.error("body length < {}", dataLength);
            in.resetReaderIndex();
            return null;
        }

        byte[] data = new byte[dataLength];
        in.readBytes(data);

        try {
            return serializer.decode(data, clazz);
        } catch (Exception e) {
            throw new RuntimeException("serializer decode error");
        }
    }
}
