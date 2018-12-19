package com.myrpc.client.netty;

import com.myrpc.consumer.future.DefaultInvokeFuture;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@io.netty.channel.ChannelHandler.Sharable
public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
	private Channel channel;

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.channel = ctx.channel();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		this.channel = ctx.channel();
	}


	@Override
	protected void channelRead0(ChannelHandlerContext ctx, RpcResponse response) {
		DefaultInvokeFuture.received(response);
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("Client exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(),
				cause.getMessage(), cause);
		ctx.close();
	}

	public void sendRequest(RpcRequest request) {
		channel.writeAndFlush(request);
	}
}
