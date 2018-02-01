package com.alen.netty.helloworld;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

public class Server {

    public static void main(String[] args) throws Exception {
        //1 创建线两个程组
        //一个是用于处理服务器端接收客户端连接的
        //一个是进行网络通信的（网络读写的）
        EventLoopGroup pGroup = new NioEventLoopGroup();
        EventLoopGroup cGroup = new NioEventLoopGroup();
        try {
            //2 创建辅助工具类，用于服务器通道的一系列配置
            ServerBootstrap b = new ServerBootstrap();
            b.group(pGroup, cGroup)        //绑定俩个线程组
                    .channel(NioServerSocketChannel.class)        //指定NIO的模式
                    .option(ChannelOption.SO_BACKLOG, 1024)        //设置tcp缓冲区
                    .option(ChannelOption.SO_SNDBUF, 32 * 1024)    //设置发送缓冲大小
                    .option(ChannelOption.SO_RCVBUF, 32 * 1024)    //这是接收缓冲大小
                    .option(ChannelOption.SO_KEEPALIVE, true)    //保持连接
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel sc) throws Exception {
                            //3 在这里配置具体数据接收方法的处理
                            sc.pipeline().addLast(new ServerHandler());
                        }
                    })
                    .option(ChannelOption.SO_BACKLOG, 128)
                    .childOption(ChannelOption.SO_KEEPALIVE, true); // (6);
            /**
             * (5)
             * 对于ChannelOption.SO_BACKLOG的解释：
             * 服务器端TCP内核维护有两个队列，我们称之为A、B队列。客户端向服务器端connect时，会发送带有SYN标志的包（第一次握手），服务器端
             * 接收到客户端发送的SYN时，向客户端发送SYN ACK确认（第二次握手），此时TCP内核模块把客户端连接加入到A队列中，然后服务器接收到
             * 客户端发送的ACK时（第三次握手），TCP内核模块把客户端连接从A队列移动到B队列，连接完成，应用程序的accept会返回。也就是说accept
             * 从B队列中取出完成了三次握手的连接。
             * A队列和B队列的长度之和就是backlog。当A、B队列的长度之和大于ChannelOption.SO_BACKLOG时，新的连接将会被TCP内核拒绝。
             * 所以，如果backlog过小，可能会出现accept速度跟不上，A、B队列满了，导致新的客户端无法连接。要注意的是，backlog对程序支持的
             * 连接数并无影响，backlog影响的只是还没有被accept取出的连接
             */

            //4 绑定端口，开始接收进来的连接
            ChannelFuture cf1 = b.bind(8765).sync();
            //ChannelFuture cf2 = b.bind(8764).sync();
            //5 等待关闭
            cf1.channel().closeFuture().sync();
            //cf2.channel().closeFuture().sync();
        } finally {
            //shutdownGracefully() 方法来关闭你构建的所有的 EventLoopGroup。
            //当EventLoopGroup 被完全地终止,并且对应的所有 channel 都已经被关闭时，
            //Netty 会返回一个Future对象来通知你。
            pGroup.shutdownGracefully();
            cGroup.shutdownGracefully();
        }

    }

}

