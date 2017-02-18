package com.romtn.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 字符串回显客户端Handler
 * 
 * @author RomanTan
 * @since 2015-12-09
 *
 */
public class KeepAliveClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	private static final String MARK = "**************** ";
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.write(Unpooled.copiedBuffer("message from client:" + new Date(), CharsetUtil.UTF_8));
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		logger.info("client received:{}", in.toString(CharsetUtil.UTF_8));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("", cause);
		ctx.close();
	}
	
}
