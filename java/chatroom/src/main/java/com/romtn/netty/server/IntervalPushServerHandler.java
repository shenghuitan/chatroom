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

import com.romtn.netty.util.TimeUtil;


/**
 * 定时推送Handler
 * 服务端不会主动关闭客户端的连接
 * 
 * @author RomanTan
 * @since 2015-12-11
 *
 */
public class IntervalPushServerHandler extends ChannelInboundHandlerAdapter {

	private Logger logger = LoggerFactory.getLogger(IntervalPushServerHandler.class);
	
	private static final String MARK = "**************** ";
	
	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		logger.info(MARK + "channelActive:ctx:{}", ctx);
		ctx.write(MARK + "push message: " + TimeUtil.now());
		ctx.flush();
	}
	
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg)
			throws Exception {
		SocketAddress remoteAddress = ctx.channel().remoteAddress();
		logger.info(MARK + "remoteAddress:{}", remoteAddress);
		ByteBuf in = (ByteBuf) msg;
		String content = in.toString(CharsetUtil.UTF_8);
		logger.info(MARK + "server receive:" + content);
		ctx.write(in);
		ctx.flush();
	}
	
	@Override
	public void channelReadComplete(ChannelHandlerContext ctx) throws Exception {
//		ctx.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
//		ctx.flush();
	}
	
	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
			throws Exception {
		logger.error("", cause);
		ctx.close();
	}
	
}
