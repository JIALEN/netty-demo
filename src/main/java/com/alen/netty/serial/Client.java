package com.alen.netty.serial;

import com.alen.netty.serial.ende.NettyMessageDecoder;
import com.alen.netty.serial.ende.NettyMessageEncoder;
import com.alen.netty.serial.handler.ClientHandler;
import com.alen.netty.serial.model.Request;
import com.alen.netty.serial.model.Response;
import com.alen.netty.serial.protostuff.ProtostuffSerializer;
import com.alen.netty.utils.GzipUtils;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

import java.io.File;
import java.io.FileInputStream;


public class Client {

	
	public static void main(String[] args) throws Exception{
		
		EventLoopGroup group = new NioEventLoopGroup();
		Bootstrap b = new Bootstrap();
		b.group(group)
		 .channel(NioSocketChannel.class)
		 .handler(new ChannelInitializer<SocketChannel>() {
			@Override
			protected void initChannel(SocketChannel sc) throws Exception {
				sc.pipeline().addLast(new NettyMessageDecoder<Response,ProtostuffSerializer>(Response.class,ProtostuffSerializer.class,1<<20, 2, 4));
				sc.pipeline().addLast(new NettyMessageEncoder<Request,ProtostuffSerializer>(Request.class,ProtostuffSerializer.class));
				sc.pipeline().addLast(new ClientHandler());
			}
		});
		
		ChannelFuture cf = b.connect("127.0.0.1", 8765).sync();
		
		for(int i = 0; i < 5; i++ ){
			Request req = new Request();
			req.setId("" + i);
			req.setName("pro" + i);
			req.setRequestMessage("数据信息" + i);	
			String path = "D:\\workFile\\netty-demo\\doc\\" + "0.jpg";
			File file = new File(path);
	        FileInputStream in = new FileInputStream(file);  
	        byte[] data = new byte[in.available()];  
	        in.read(data);  
	        in.close(); 
			req.setAttachment(GzipUtils.gzip(data));
			cf.channel().writeAndFlush(req);
		}

		cf.channel().closeFuture().sync();
		group.shutdownGracefully();
	}
}
