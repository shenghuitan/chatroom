package com.romtn.netty.client;

import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.romtn.netty.util.TimeUtil;

import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.CharsetUtil;

/**
 * 定时发送字符串到服务端
 * 
 * @author RomanTan
 * @since 2015-12-09
 *
 */
public class KeepAliveClient {

	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private final String host = "172.19.142.43";
	private final int port = 8083;
	
	private SocketChannel channel;
	
	// 创建一个连接集合
//	private static List<>
	
	public void start() throws InterruptedException{
		// 
		EventLoopGroup group = new NioEventLoopGroup();
		try {
			// 
			Bootstrap b = new Bootstrap();
			// 1 boss group
			b.group(group);
			 // 2 channel
			b.channel(NioSocketChannel.class);
			 // 3 remote address
			b.remoteAddress(host, port);
			b.option(ChannelOption.SO_KEEPALIVE, true);
			 // 4 handler
			b.handler(new ChannelInitializer<SocketChannel>() {
				 @Override
				protected void initChannel(SocketChannel ch) throws Exception {
					ch.pipeline().addLast(new SimpleEchoClientHandler());
				}
			});
			// connect and wait response
			ChannelFuture f = b.connect().sync();
			logger.info("connect:" + TimeUtil.now());
//			f.addListener(new ChannelFutureListener() {
//				@Override
//				public void operationComplete(ChannelFuture future) throws Exception {
//					if (future.isSuccess()){
//						logger.info("future success:" + TimeUtil.now());
//						channel = (SocketChannel) future.channel();
//					}
//				}
//			});
			if (f.isSuccess()){
				logger.info("future success:" + TimeUtil.now());
				channel = (SocketChannel) f.channel();
			}
			// close client
//			f.channel().closeFuture().sync();
//			f.channel().eventLoop().
			
			for (int i = 0; i < 2; i++){
//				Thread.sleep(200);
				if (channel == null){
					logger.info("socket channel null: " + TimeUtil.now());
					continue;
				}
				channel.writeAndFlush("send from channel messge: " + TimeUtil.now());
				logger.info("send from channel success");
				channel.pipeline().writeAndFlush("send from pipeline: " + TimeUtil.now());
				logger.info("send from pipeline success");
				channel.pipeline().lastContext().writeAndFlush("send from context: " + TimeUtil.now());
				logger.info("send from last context success");
			}
//			f.channel().closeFuture().sync();
//			logger.info("close channel future success");
		} finally {
//			group.shutdownGracefully().sync();
		}
	}
	
	public static void main(String[] args) throws InterruptedException {
		KeepAliveClient client = new KeepAliveClient();
		client.start();
	}
	
}
