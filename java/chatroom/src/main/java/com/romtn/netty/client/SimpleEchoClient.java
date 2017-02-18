package com.romtn.netty.client;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 定时发送字符串到服务端
 * 
 * @author RomanTan
 * @since 2015-12-09
 *
 */
public class SimpleEchoClient {

	private final String host = "127.0.0.1";
	private final int port = 8081;
	
	public void start() throws InterruptedException{
		
		// 
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 
			Bootstrap b = new Bootstrap();
			// 1 boss group
			b.group(group)
			 // 2 channel
			 .channel(NioSocketChannel.class)
			 // 3 remote address
			 .remoteAddress(host, port)
			 // 4 handler
			 .handler(new ChannelInitializer<SocketChannel>() {
				 @Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new SimpleEchoClientHandler());
				}
			});
			// connect and wait response
			ChannelFuture f = b.connect().sync();
			// close client
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		SimpleEchoClient client = new SimpleEchoClient();
		client.start();
	}
	
}
