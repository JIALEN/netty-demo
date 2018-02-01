package com.alen.netty.ende.ende4;

import com.alen.netty.ende.ende4.model.Header;
import com.alen.netty.ende.ende4.model.MessageType;
import com.alen.netty.ende.ende4.model.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

/**
 * 握手认证(client)
 * @author alen
 * @create 2018-01-31 17:22
 **/
public class LoginAuthReqHandler extends ChannelInboundHandlerAdapter {

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //握手请求
        ctx.writeAndFlush(buildLoginReq());
        System.out.println("client: send LoginAuthReq ---> " + buildLoginReq());
    }

    private NettyMessage buildLoginReq() {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType((byte) 3);
        message.setHeader(header);
        message.setBody((byte) 0);
        return message;
    }

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        //握手应答消息
        NettyMessage message = (NettyMessage) msg;
        if (message.getHeader() != null && message.getHeader().getType() == MessageType.LOGIN_RESP.value()) {
            byte loginResult = (Byte) message.getBody();
            if (loginResult != (byte) 0) {
                //如果应答消息体不为0则认证失败
                ctx.close();
            } else {
                System.out.println("client:Login is OK --->" + message);
                ctx.fireChannelRead(message);
            }

        } else {
            //通知下一个Handler
            ctx.fireChannelRead(message);
        }


    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        ctx.close();
    }
}