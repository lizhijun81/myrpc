package com.frameworkrpc.client.netty;

import com.frameworkrpc.client.AbstractClient;
import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.exception.MyRpcRemotingException;
import com.frameworkrpc.extension.ExtensionLoader;
import com.frameworkrpc.extension.Scope;
import com.frameworkrpc.consumer.future.InvokeFuture;
import com.frameworkrpc.model.RpcRequest;
import com.frameworkrpc.model.RpcResponse;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.serialize.MessageDecoder;
import com.frameworkrpc.serialize.MessageEncoder;
import com.frameworkrpc.serialize.Serialize;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.ChannelOption;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.TimeUnit;

public class NettyClient extends AbstractClient {

	private static final Logger logger = LoggerFactory.getLogger(NettyClient.class);
	private Bootstrap bootstrap;
	private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(RpcConstants.DEFAULT_IOTHREADS,
			new DefaultThreadFactory("NettyClientBoss", true));

	private volatile Channel channel;
	private volatile NettyClientHandler nettyClientHandler;

	public NettyClient(URL url) {
		super(url);
	}

	@Override
	public URL getUrl() {
		return url;
	}

	@Override
	public boolean isClosed() {
		return isClosed;
	}

	@Override
	public boolean isConnected() {
		return channel != null && channel.isActive();
	}

	@Override
	public void doClose() {

	}

	@Override
	public synchronized void doConnect() {
		if (isConnected()) {
			return;
		}
		nettyClientHandler = new NettyClientHandler(url);
		Serialize serialize = ExtensionLoader.getExtensionLoader(Serialize.class)
				.getExtension(url.getParameter(RpcConstants.SERIALIZATION_KEY), Scope.SINGLETON);
		bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY));
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) throws Exception {
				ch.pipeline().addLast("decoder", new MessageDecoder(serialize, RpcResponse.class))
						.addLast("encoder", new MessageEncoder(serialize, RpcRequest.class)).addLast("handler", nettyClientHandler);
			}
		});

		long start = System.currentTimeMillis();
		ChannelFuture future = bootstrap.connect(getConnectAddress());
		try {
			boolean ret = future.awaitUninterruptibly(url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY), TimeUnit.MILLISECONDS);

			if (ret && future.isSuccess()) {
				Channel newChannel = future.channel();
				try {
					// Close old channel
					Channel oldChannel = NettyClient.this.channel; // copy reference
					if (oldChannel != null) {
						try {
							logger.info("Close old netty channel " + oldChannel + " on create new netty channel " + newChannel);
							oldChannel.close();
						} finally {
							//NettyChannel.removeChannelIfDisconnected(oldChannel);
						}
					}
				} finally {
					if (NettyClient.this.isClosed()) {
						try {
							logger.info("Close new netty channel " + newChannel + ", because the client closed.");
							newChannel.close();
						} finally {
							NettyClient.this.channel = null;
							//NettyChannel.removeChannelIfDisconnected(newChannel);
						}
					} else {
						NettyClient.this.channel = newChannel;
					}
				}
			} else if (future.cause() != null) {
				throw new MyRpcRemotingException(
						"client(url: " + getUrl().toFullStr() + ") failed to connect to server " + getConnectAddress() + ", error message is:"
								+ future.cause().getMessage(), future.cause());
			} else {
				throw new MyRpcRemotingException(
						"client(url: " + getUrl().toFullStr() + ") failed to connect to server " + getConnectAddress() + " client-side timeout "
								+ getConnectAddress() + "ms (elapsed: " + (System.currentTimeMillis() - start) + "ms) from netty client " + NetUtils
								.getLocalHost() + " using  version " + getUrl().getParameter(RpcConstants.VERSION_KEY));
			}
		} finally {
			//			if (!isConnected()) {
			//				//future.cancel(true);
			//			}
		}
	}

	@Override
	public synchronized void disConnect() {
		if (isConnected()) {
			channel.close();
		}
	}

	@Override
	public InvokeFuture request(RpcRequest request) {
		return nettyClientHandler.sendRequest(request);
	}

}
