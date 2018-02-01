package com.alen.netty.ende.ende4;

import com.alen.netty.ende.ende4.model.NettyMessage;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.io.IOException;
import java.util.Map;

/**
 * 消息编码器
 * @author alen
 * @create 2018-01-31 16:16
 **/
public class NettyMessageEncoder extends  MessageToByteEncoder<NettyMessage> {

    NettyMarshallingEncoder marshallingEncoder;

    public NettyMessageEncoder() throws IOException {
        this.marshallingEncoder = MarshallingCodeCFactory.buildMarshallingEncoder();
    }

    @Override
    protected void encode(ChannelHandlerContext ctx, NettyMessage msg,
                          ByteBuf sendBuf) throws Exception {
        if (msg == null || msg.getHeader() == null)
            throw new Exception("The encode message is null");
        sendBuf.writeInt((msg.getHeader().getCrcCode()));
        sendBuf.writeInt((msg.getHeader().getLength()));
        sendBuf.writeLong((msg.getHeader().getSessionID()));
        sendBuf.writeByte((msg.getHeader().getType()));
        sendBuf.writeByte((msg.getHeader().getPriority()));
        sendBuf.writeInt((msg.getHeader().getAttachment().size()));
        String key = null;
        byte[] keyArray = null;
        Object value = null;
        for (Map.Entry<String, Object> param : msg.getHeader().getAttachment()
                .entrySet()) {
            key = param.getKey();
            keyArray = key.getBytes("UTF-8");
            sendBuf.writeInt(keyArray.length);
            sendBuf.writeBytes(keyArray);
            value = param.getValue();
            marshallingEncoder.encode(ctx,value, sendBuf);
        }
        key = null;
        keyArray = null;
        value = null;
        if (msg.getBody() != null) {
            marshallingEncoder.encode(ctx,msg.getBody(), sendBuf);
        } else
            sendBuf.writeInt(0);
        sendBuf.setInt(4, sendBuf.readableBytes() - 8);
    }
}
