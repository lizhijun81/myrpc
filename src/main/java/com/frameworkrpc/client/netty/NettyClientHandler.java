package com.frameworkrpc.client.netty;

import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.RpcResponse;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.proxy.RPCFuture;
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
	protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcResponse response) throws Exception {
		String requestId = response.getRequestId();
		RPCFuture rpcFuture = RPCFuture.pendingRPC.get(requestId);
		if (rpcFuture != null) {
			RPCFuture.pendingRPC.remove(requestId);
			rpcFuture.done(response);
		}
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Client exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(),
				cause.getMessage(), cause);
		ctx.close();
	}

	public RPCFuture sendRequest(RpcRequester request) {
		RPCFuture rpcFuture = new RPCFuture(request);
		RPCFuture.pendingRPC.put(request.getRequestId(), rpcFuture);
		channel.writeAndFlush(request);
		return rpcFuture;
	}
}
