package com.alen.netty.serial.handler;

import com.alen.netty.serial.model.Request;
import com.alen.netty.serial.model.Response;
import com.alen.netty.utils.GzipUtils;
import io.netty.channel.ChannelHandlerContext;

import java.io.FileOutputStream;

import io.netty.channel.ChannelInboundHandlerAdapter;

public class ServerHandler extends ChannelInboundHandlerAdapter {

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {

	}

	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		Request req = (Request)msg;
		System.out.println("Server : " + req.getId() + ", " + req.getName() + ", " + req.getRequestMessage());
		byte[] attachment = GzipUtils.ungzip(req.getAttachment());
		double s=Math.random();
		String path = "D:\\workFile\\netty-demo\\doc\\"+s+".jpg";
        FileOutputStream fos = new FileOutputStream(path);
        fos.write(attachment);
        fos.close();
		
		Response resp = new Response();
		resp.setId(req.getId());
		resp.setName("resp" + req.getId());
		resp.setResponseMessage("响应内容" + req.getId());
		ctx.writeAndFlush(resp);//.addListener(ChannelFutureListener.CLOSE);
	}

	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		ctx.close();
	}

	
	
}
