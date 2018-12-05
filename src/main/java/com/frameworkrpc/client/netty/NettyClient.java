package com.frameworkrpc.client.netty;

import com.frameworkrpc.client.AbstractClient;
import com.frameworkrpc.client.Client;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.RpcResponse;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.serialize.MessageDecoder;
import com.frameworkrpc.serialize.MessageEncoder;
import com.frameworkrpc.serialize.Serialize;
import com.frameworkrpc.serialize.SerializeFactory;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

;
public class NettyClient extends AbstractClient implements Client {

	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);

	private Bootstrap bootstrap;
	private EventLoopGroup nioEventLoopGroup;

	public NettyClient(URL url) {
		super(url);
	}

	@Override
	public URL getURL() {
		return url;
	}

	@Override
	public boolean isOpen() {
		return isOpen;
	}

	@Override
	public boolean isClose() {
		return isClose;
	}

	@Override
	public synchronized void doOpen() {

		bootstrap = new Bootstrap();

		nioEventLoopGroup = new NioEventLoopGroup();

		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, url.getIntParameter(RpcConstant.TIMEOUT));
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);

		IdleStateHandler idleStateHandler = new IdleStateHandler(0, url.getIntParameter(RpcConstant.HEARTBEAT), 0) {
			@Override
			public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
				if (evt instanceof IdleStateEvent) {
					IdleStateEvent e = (IdleStateEvent) evt;
					if (e.state() == IdleState.READER_IDLE) {
					} else if (e.state() == IdleState.WRITER_IDLE) {
						ctx.close();
					}
				}
			}
		};

		bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				Serialize serialize = SerializeFactory.createSerialize(getURL().getParameter(RpcConstant.SERIALIZATION));
				ch.pipeline()
						.addLast("idlestate", idleStateHandler)
						.addLast("decoder", new MessageDecoder(serialize, RpcResponse.class))
						.addLast("encoder", new MessageEncoder(serialize, RpcRequester.class));
			}
		});

	}

	@Override
	public synchronized void doClose() {
		ChannelFuture future = bootstrap.connect(getConnectAddress());

	}

	@Override
	public void doconnect() {

	}

	@Override
	public void disconnect() {

	}

	@Override
	public void reconnect() {

	}
}
