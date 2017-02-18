package com.romtn.netty.client;

import io.netty.buffer.ByteBuf;
import io.netty.buffer.Unpooled;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.CharsetUtil;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.romtn.netty.util.TimeUtil;

/**
 * 字符串回显客户端Handler
 * 
 * @author RomanTan
 * @since 2015-12-09
 *
 */
public class SimpleEchoClientHandler extends SimpleChannelInboundHandler<ByteBuf> {
	
	private Logger logger = LoggerFactory.getLogger(getClass());
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.write(Unpooled.copiedBuffer("send echo message by buffer:" + TimeUtil.now(),
				CharsetUtil.UTF_8));
		ctx.write("send echo message direct: " + TimeUtil.now());
		ctx.flush();
	}
	
	@Override
	protected void channelRead0(ChannelHandlerContext ctx, ByteBuf in)
			throws Exception {
		logger.info("client read in:{}", in.toString(CharsetUtil.UTF_8));
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("", cause);
		ctx.close();
	}
	
}
