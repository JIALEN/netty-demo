package com.alen.netty.serial;

import com.alen.netty.serial.ende.NettyMessageDecoder;
import com.alen.netty.serial.ende.NettyMessageEncoder;
import com.alen.netty.serial.handler.ServerHandler;
import com.alen.netty.serial.model.Request;
import com.alen.netty.serial.model.Response;
import com.alen.netty.serial.protostuff.ProtostuffSerializer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.handler.logging.LogLevel;
import io.netty.handler.logging.LoggingHandler;

public class Server {

	public static void main(String[] args) throws Exception{
		EventLoopGroup pGroup = new NioEventLoopGroup();
		EventLoopGroup cGroup = new NioEventLoopGroup();
		ServerBootstrap b = new ServerBootstrap();
		b.group(pGroup, cGroup)
		 .channel(NioServerSocketChannel.class)
		 .option(ChannelOption.SO_BACKLOG, 1024)
		 //设置日志
		 .handler(new LoggingHandler(LogLevel.INFO))
		 .childHandler(new ChannelInitializer<SocketChannel>() {
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new NettyMessageDecoder<Request,ProtostuffSerializer>(Request.class,ProtostuffSerializer.class,1<<20, 2, 4));
				sc.pipeline().addLast(new NettyMessageEncoder<Response,ProtostuffSerializer>(Response.class,ProtostuffSerializer.class));
				sc.pipeline().addLast(new ServerHandler());
			}
		});
		
		ChannelFuture cf = b.bind(8765).sync();
		
		cf.channel().closeFuture().sync();
		pGroup.shutdownGracefully();
		cGroup.shutdownGracefully();
		
	}
}
