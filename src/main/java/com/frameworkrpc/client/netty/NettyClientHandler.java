package com.frameworkrpc.client.netty;

import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.RpcResponse;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.consumer.future.InvokeFuture;
import io.netty.channel.Channel;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

public class NettyClientHandler extends SimpleChannelInboundHandler<RpcResponse> {

	private static final Logger logger = LoggerFactory.getLogger(NettyClientHandler.class);
	private URL url;
	private Channel channel;
	private SocketAddress remotePeer;

	public URL getUrl() {
		return url;
	}

	public Channel getChannel() {
		return channel;
	}

	public SocketAddress getRemotePeer() {
		return remotePeer;
	}

	public NettyClientHandler(URL url) {
		this.url = url;
	}

	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		super.channelActive(ctx);
		this.remotePeer = this.channel.remoteAddress();
	}

	@Override
	public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
		super.channelRegistered(ctx);
		this.channel = ctx.channel();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		super.channelInactive(ctx);
		this.remotePeer = this.channel.remoteAddress();
	}

	@Override
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
		String requestId = response.getRequestId();
		InvokeFuture rpcFuture = InvokeFuture.pendingRPC.get(requestId);
		if (rpcFuture != null) {
			InvokeFuture.pendingRPC.remove(requestId);
			rpcFuture.done(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Client exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(),
				cause.getMessage(), cause);
		ctx.close();
	}

	public InvokeFuture sendRequest(RpcRequest request) {
		InvokeFuture rpcFuture = new InvokeFuture(request);
		InvokeFuture.pendingRPC.put(request.getRequestId(), rpcFuture);
		channel.writeAndFlush(request);
		return rpcFuture;
	}
}
