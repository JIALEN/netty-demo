package com.alen.netty.ende.ende4;

import com.alen.netty.ende.ende4.model.Header;
import com.alen.netty.ende.ende4.model.MessageType;
import com.alen.netty.ende.ende4.model.NettyMessage;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;

import java.net.InetSocketAddress;
import java.util.HashMap;
import java.util.Map;

/**
 * 握手认证(server)
 * Created by root on 1/12/18.
 */
public class LoginAuthRespHandler extends ChannelInboundHandlerAdapter {
    //已登录列表
    Map node = new HashMap<String, Boolean>();
    //白名单
    private String[] whiteList = {"127.0.0.1"};

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
        NettyMessage message = (NettyMessage) msg;

        //握手请求消息
        if (message != null && message.getHeader().getType() == MessageType.LOGIN_REQ.value()) {
            String nodeInde = ctx.channel().remoteAddress().toString();
            System.out.println("server:receive  LoginAuthReq <--- " + message);
            NettyMessage loginResp = null;
            //拒绝重复登录
            if (node.containsKey(nodeInde)) {
                loginResp = buildResp((byte) -1);
            } else {
                InetSocketAddress address = (InetSocketAddress) ctx.channel().remoteAddress();
                Boolean isOK = false;
                String IP = address.getAddress().getHostAddress();
                for (String WIP : whiteList) {
                    if (WIP.equalsIgnoreCase(IP)) {
                        isOK = true;
                        break;
                    }
                }
                loginResp = isOK ? buildResp((byte) 0) : buildResp((byte) -1);
                if (isOK) {
                    node.put(nodeInde, true);
                }

            }
            System.out.println("server:send LoginAuthResp ---> " + loginResp);
            ctx.writeAndFlush(loginResp);
        } else {
            ctx.fireChannelRead(message);
        }
    }

    private NettyMessage buildResp(byte b) {
        NettyMessage message = new NettyMessage();
        Header header = new Header();
        header.setType(MessageType.LOGIN_RESP.value());
        message.setHeader(header);
        message.setBody(b);
        return message;
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
        //删除缓存
        node.remove(ctx.channel().remoteAddress().toString());
        ctx.close();
        ctx.fireExceptionCaught(cause);
    }
}
