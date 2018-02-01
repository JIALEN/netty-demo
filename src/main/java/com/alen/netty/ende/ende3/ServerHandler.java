package com.alen.netty.ende.ende3;

import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	private static final Logger logger= LogManager.getLogger(ServerHandler.class.getName());

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		System.out.println(" server channel active... ");
	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		String request = (String)msg;
		System.out.println("Server :" + msg);
		String response =  request ;
		ctx.writeAndFlush(Unpooled.copiedBuffer(response.getBytes()));
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable t) throws Exception {
		logger.error("异常-------------",t);

	}
}
