package com.myrpc.transport.netty;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.extension.RpcComponent;
import com.myrpc.extension.Scope;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import com.myrpc.rpc.netty.NettyChannel;
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

import java.net.InetSocketAddress;
import java.util.concurrent.TimeUnit;

@RpcComponent(name = "netty")
public class NettyConnector extends AbstractConnector {

	private static final Logger logger = LoggerFactory.getLogger(NettyConnector.class);
	private static final NioEventLoopGroup nioEventLoopGroup = new NioEventLoopGroup(RpcConstants.DEFAULT_IOTHREADS,
			new DefaultThreadFactory("NettyClientBoss", true));

	private Bootstrap bootstrap;
	private NettyConnectorHandler nettyConnectorHandler;

	@Override
	public Connector init() {

		this.nettyConnectorHandler = new NettyConnectorHandler();
		Serialize serialize = ExtensionLoader.getExtensionLoader(Serialize.class)
				.getExtension(url.getParameter(RpcConstants.SERIALIZATION_KEY), Scope.SINGLETON);

		bootstrap = new Bootstrap();
		bootstrap.option(ChannelOption.CONNECT_TIMEOUT_MILLIS, url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY));
		bootstrap.option(ChannelOption.SO_KEEPALIVE, true);
		bootstrap.option(ChannelOption.TCP_NODELAY, true);
		bootstrap.option(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT);
		bootstrap.group(nioEventLoopGroup)
				.channel(NioSocketChannel.class)
				.handler(new ChannelInitializer<NioSocketChannel>() {
						@Override
						protected void initChannel(NioSocketChannel ch) {
							ch.pipeline()
									.addLast("decoder", new MessageDecoder(serialize, RpcResponse.class))
									.addLast("encoder", new MessageEncoder(serialize, RpcRequest.class))
									.addLast("handler", nettyConnectorHandler);
						}
					});

		return this;
	}



	@Override
	public void close() {

	}

	@Override
	public  com.myrpc.rpc.Channel connect(URL url) {

		ChannelFuture future = bootstrap.connect(new InetSocketAddress(url.getHost(), url.getPort()));

			boolean ret = future.awaitUninterruptibly(url.getIntParameter(RpcConstants.CONNECTTIMEOUT_KEY), TimeUnit.MILLISECONDS);

			if (ret && future.isSuccess()) {

				Channel newChannel = future.channel();

//				Serialize serialize = ExtensionLoader.getExtensionLoader(Serialize.class)
//						.getExtension(url.getParameter(RpcConstants.SERIALIZATION_KEY), Scope.SINGLETON);
//
//				newChannel.pipeline()
//							.addLast("decoder", new MessageDecoder(serialize, RpcResponse.class))
//							.addLast("encoder", new MessageEncoder(serialize, RpcRequest.class))
//							.addLast("handler", nettyConnectorHandler);
				return NettyChannel.attachChannel(newChannel);

		}
		return null;
	}

}
