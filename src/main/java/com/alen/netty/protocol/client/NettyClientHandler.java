
package com.alen.netty.protocol.client;

import com.alen.netty.protocol.model.Header;
import com.alen.netty.protocol.model.MessageType;
import com.alen.netty.protocol.model.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;


public class NettyClientHandler extends ChannelInboundHandlerAdapter {


    private static int i = 0;//发五次测试请求

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        NettyMessage message = (NettyMessage) msg;
        if (i <= 5) {
            //测试请求
            Thread t = new Thread(new TestTask(ctx));
            t.start();
            i++;
        }
        // 如果是响应请求消息，处理，其它消息透传
        if (message.getHeader() != null
                && message.getHeader().getType() == MessageType.SERVICE_RESP
                .value()) {
            System.out.println("响应信息-------------" + message.toString());
        } else {
            ctx.fireChannelRead(msg);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        System.out.println("异常-------------" + cause);
        cause.printStackTrace();
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }


    private class TestTask implements Runnable {
        private final ChannelHandlerContext ctx;

        public TestTask(final ChannelHandlerContext ctx) {
            this.ctx = ctx;
        }

        @Override
        public void run() {
            NettyMessage heatBeat = buildClientRequest();
            System.out
                    .println("客户端发送请求 : ---> "
                            + heatBeat);
            ctx.writeAndFlush(heatBeat);
        }

        private NettyMessage buildClientRequest() {
            NettyMessage message = new NettyMessage();
            Header header = new Header();
            header.setType(MessageType.SERVICE_REQ.value());
            message.setHeader(header);
            message.setBody("测试请求");
            return message;
        }
    }

}
