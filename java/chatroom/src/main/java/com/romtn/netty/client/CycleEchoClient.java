package com.romtn.netty.client;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.romtn.netty.util.TimeUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;

/**
 * 定时发送字符串到客户端，使用长连接。
 * 
 * @author RomanTan
 * @since 2015-12-09
 *
 */
public class CycleEchoClient {
	
	private Logger logger = LoggerFactory.getLogger(getClass());

	private final String host = "127.0.0.1";
	private final int port = 8081;
	
	private void start() throws InterruptedException{
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			Bootstrap b = new Bootstrap();
			b.group(group);
			b.remoteAddress(host, port);
			b.channel(NioSocketChannel.class);
			b.handler(new ChannelInitializer<SocketChannel>() {
				@Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new SimpleEchoClientHandler());
				}
			});
			ChannelFuture f = null;
			// @Note 在这里创建连接，在哪里关闭连接？
			// @Note 可以尝试在服务端获取客户端的IP和端口
			// @Note 也可以定时在服务端强制关闭客户端的连接，客户端检测到后，执行重连
			f = b.connect().sync();
			if (f.isSuccess()){
				logger.info("connect success:{}", TimeUtil.now());
//				f = b.connect();
			}
//			Channel ch = f.channel();
//			for (int i = 0; i < 100; i++){
//				Thread.sleep(2000);
//				ch = f.channel();
//			}
			f.channel().closeFuture().sync();
		} finally {
			group.shutdownGracefully().sync();
		}
		
	}
	
	public static void main(String[] args) throws InterruptedException {
		CycleEchoClient client = new CycleEchoClient();
		client.start();
	}
	
}
