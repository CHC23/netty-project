package com.chc.netty.server;

import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.InetAddress;
import java.util.Date;

/**
 * 客户端处理器
 */
public class ServerHandler extends SimpleChannelInboundHandler<String> {
    private static final Logger logger = LoggerFactory.getLogger(ServerHandler.class);

    @Override
    public void channelActive(ChannelHandlerContext ctx)  throws Exception {
        ctx.write( new Date() + " : Welcome to " + InetAddress.getLocalHost().getHostName() + "!\r\n");
        ctx.flush();
    }

    @Override
    public void channelRead0(ChannelHandlerContext ctx, String s) throws Exception {
        ChannelFuture channelFuture = ctx.newSucceededFuture();
        boolean close = false;
        if (s.equals("exit")) {
            close = true;
        } else {
            String responseMsg = "Got it:" + s;
            channelFuture = ctx.write(responseMsg);
        }
        if (close) {
            channelFuture.addListener(ChannelFutureListener.CLOSE);
        }
    }

    @Override
    public void channelReadComplete(ChannelHandlerContext ctx) {
        ctx.flush();
    }

    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
        logger.error("#ServerHandler.exceptionCaught()# error:{}", cause.getMessage());
        ctx.close();
    }
}
