package com.romtn.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 定时推送消息给连接的所有客户端
 * 
 * @author RomanTan
 * @since 2015-12-11
 *
 */
public class IntervalPushServer {
	
	private static final Logger logger = LoggerFactory.getLogger(IntervalPushServer.class);

	private final int port = 8083;
	
	private void start() throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			ServerBootstrap b = new ServerBootstrap();
			b.group(group);
			b.channel(NioServerSocketChannel.class);
			b.localAddress(new InetSocketAddress(port));
			b.childHandler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new IntervalPushServerHandler());
				}
			});
			ChannelFuture f = b.bind().sync();
			logger.info("start simple server:bootstrap:{}", b);
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		IntervalPushServer server = new IntervalPushServer();
		server.start();
	}
	
}
