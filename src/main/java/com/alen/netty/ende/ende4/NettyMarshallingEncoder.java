package com.alen.netty.ende.ende4;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.marshalling.MarshallerProvider;
import io.netty.handler.codec.marshalling.MarshallingEncoder;

/**
 * @author alen
 * @create 2018-01-31 16:32
 **/
public class NettyMarshallingEncoder extends MarshallingEncoder {

    public NettyMarshallingEncoder(MarshallerProvider provider) {
        super(provider);
    }

    public void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
        super.encode(ctx, msg, out);
    }
}
