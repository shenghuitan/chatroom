package com.romtn.netty.server;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.util.CharsetUtil;

import java.net.SocketAddress;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


/**
 * 简单处理字符串的服务端Handler
 * 
 * @author RomanTan
 * @since 2015-12-09
 *
 */
public class SimpleEchoServerHandler extends ChannelInboundHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(SimpleEchoServerHandler.class);
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SocketAddress remoteAddress = ctx.channel().remoteAddress();
		logger.info("remoteAddress:{}", remoteAddress);
		ByteBuf in = (ByteBuf) msg;
		String content = in.toString(CharsetUtil.UTF_8);
		logger.info("server receive:" + content);
		ctx.write(in);
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("", cause);
		ctx.close();
	}
	
}
