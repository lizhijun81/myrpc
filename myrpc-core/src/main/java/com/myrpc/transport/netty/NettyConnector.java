package com.myrpc.transport.netty;

import com.myrpc.common.NetUtils;
import com.myrpc.common.RpcConstants;
import com.myrpc.exception.MyRpcRemotingException;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.extension.RpcComponent;
import com.myrpc.extension.Scope;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import com.myrpc.serialize.MessageDecoder;
import com.myrpc.serialize.MessageEncoder;
import com.myrpc.serialize.Serialize;
import com.myrpc.transport.AbstractConnector;
import com.myrpc.transport.Connector;
import io.netty.bootstrap.Bootstrap;
import io.netty.buffer.PooledByteBufAllocator;
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

@RpcComponent(name = "netty")
public class NettyConnector extends AbstractConnector {

	private static final Logger logger = LoggerFactory.getLogger(NettyConnector.class);
	private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(RpcConstants.DEFAULT_IOTHREADS,
			new DefaultThreadFactory("NettyClientBoss", true));

	private Bootstrap bootstrap;
	private volatile Channel channel;
	private Serialize serialize;
	private NettyConnectorHandler nettyConnectorHandler;


	@Override
	public Connector init() {

		this.serialize = ExtensionLoader.getExtensionLoader(Serialize.class)
				.getExtension(url.getParameter(RpcConstants.SERIALIZATION_KEY), Scope.SINGLETON);

		this.nettyConnectorHandler = new NettyConnectorHandler();

		return this;
	}

	@Override
	public boolean isClosed() {
		return false;
	}


	@Override
	public boolean isConnected() {
		return channel != null && channel.isActive();
	}

	@Override
	public void close() {

	}

	@Override
	public synchronized void connect() {
		if (isConnected()) {
			return;
		}
		bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY));
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.group(nioEventLoopGroup).channel(NioSocketChannel.class).handler(new ChannelInitializer<NioSocketChannel>() {
			@Override
			protected void initChannel(NioSocketChannel ch) {
				ch.pipeline().addLast("decoder", new MessageDecoder(serialize, RpcResponse.class))
						.addLast("encoder", new MessageEncoder(serialize, RpcRequest.class)).addLast("handler", nettyConnectorHandler);
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
					Channel oldChannel = NettyConnector.this.channel; // copy reference
					if (oldChannel != null) {
						try {
							logger.info("Close old netty channel " + oldChannel + " on create new netty channel " + newChannel);
							oldChannel.close();
						} finally {
							//NettyChannel.removeChannelIfDisconnected(oldChannel);
						}
					}
				} finally {
					if (NettyConnector.this.isClosed()) {
						try {
							logger.info("Close new netty channel " + newChannel + ", because the client closed.");
							newChannel.close();
						} finally {
							NettyConnector.this.channel = null;
							//NettyChannel.removeChannelIfDisconnected(newChannel);
						}
					} else {
						NettyConnector.this.channel = newChannel;
					}
				}
			} else if (future.cause() != null) {
				throw new MyRpcRemotingException(
						"client(url: " + url.toFullStr() + ") failed to connect to server " + getConnectAddress() + ", error message is:" + future
								.cause().getMessage(), future.cause());
			} else {
				throw new MyRpcRemotingException(
						"client(url: " + url.toFullStr() + ") failed to connect to server " + getConnectAddress() + " client-side timeout "
								+ getConnectAddress() + "ms (elapsed: " + (System.currentTimeMillis() - start) + "ms) from netty client " + NetUtils
								.getLocalHost() + " using  version " + url.getParameter(RpcConstants.VERSION_KEY));
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
	public void request(RpcRequest request) {
		//nettyClientHandler.sendRequest(request);
		channel.writeAndFlush(request);
	}
}
