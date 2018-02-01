package com.alen.netty.helloworld;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.ReferenceCountUtil;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	//channelActive()：客户端连接服务器后被调用
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println("server channel active... ");
	}

	//从服务器接收到数据后调用
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		try {
			ByteBuf buf = (ByteBuf) msg;
			byte[] req = new byte[buf.readableBytes()];
			buf.readBytes(req);
			String body = new String(req, "utf-8");
			System.out.println("Server :" + body );
			String response = "进行返回给客户端的响应：" + body ;
			ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
		} finally {
			ReferenceCountUtil.release(msg);
		}

	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx)
			throws Exception {
		System.out.println("读完了");
		ctx.flush();
	}

	//	发生异常时被调用
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t)
			throws Exception {
		ctx.close();
	}

}
