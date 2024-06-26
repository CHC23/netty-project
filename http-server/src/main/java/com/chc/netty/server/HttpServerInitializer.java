package com.chc.netty.server;

import io.netty.channel.ChannelInitializer;
import io.netty.channel.socket.SocketChannel;
import io.netty.handler.codec.http.HttpRequestEncoder;
import io.netty.handler.codec.http.HttpResponseEncoder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HttpServerInitializer extends ChannelInitializer<SocketChannel> {
    private static final Logger LOGGER = LoggerFactory.getLogger(HttpServerInitializer.class);

    private static final HttpServerHandler httpServerHandler = new HttpServerHandler();

    /**
     * channel初始化 channel注册到loop时调用
     */
    @Override
    protected void initChannel(SocketChannel socketChannel) {
        LOGGER.info("Initializing channel...");
        // 编码解码
        socketChannel.pipeline().addLast(new HttpResponseEncoder());
        socketChannel.pipeline().addLast(new HttpRequestEncoder());
        socketChannel.pipeline().addLast(httpServerHandler);
    }
}
