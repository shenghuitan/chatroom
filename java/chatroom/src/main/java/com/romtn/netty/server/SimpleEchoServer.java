package com.romtn.netty.server;

import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;

import java.net.InetSocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 接收客户端发送过来的消息（字符串），附加上服务端的响应时间，回显
 * 响应时间单位：纳秒，可使用于同机器测量时间消耗
 * 
 * @author RomanTan
 * @since 2015-12-09
 *
 */
public class SimpleEchoServer {
	
	private static final Logger logger = LoggerFactory.getLogger(SimpleEchoServer.class);

	private final int port = 8081;
	
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
					ch.pipeline().addLast(new SimpleEchoServerHandler());
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
		SimpleEchoServer server = new SimpleEchoServer();
		server.start();
	}
	
}
