package com.chc.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServer {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServer.class);

    public static void main(String[] args) {
        start(2333);
    }

    private static void start(int port) {
        LOGGER.info("Start netty http server...");
        //用于处理服务端接受客户端连接
        EventLoopGroup boosGroup = new NioEventLoopGroup();
        //用于SocketChannel的网络读写操作
        EventLoopGroup workerGroup = new NioEventLoopGroup();
        try {
            //用于启动NIO服务端的辅助启动类,目的是降低服务端的开发复杂度
            ServerBootstrap b = new ServerBootstrap();
            b.group(boosGroup, workerGroup)
                    //对应JDK NIO类库中的ServerSocketChannel
                    .channel(NioServerSocketChannel.class)
                    //配置NioServerSocketChannel的TCP参数 指定处理客户端连接队列大小
                    //ChannelOption.SO_KEEPALIVE 一直保持连接活动状态
                    .option(ChannelOption.SO_BACKLOG, 1024)
                    //绑定I/O的事件处理类
                    .childHandler(new HttpServerInitializer());
            //调用它的bind操作监听端口号,sync 等待异步操作执行完毕
            ChannelFuture f = b.bind(port).sync();
            LOGGER.info("Netty http server is started");
            //异步操作的通知回调
            f.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            LOGGER.error("There is an exception : ", e);
        } finally {
            //释放线程池资源
            boosGroup.shutdownGracefully();
            workerGroup.shutdownGracefully();
        }
    }
}
