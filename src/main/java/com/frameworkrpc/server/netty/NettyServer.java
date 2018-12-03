package com.frameworkrpc.server.netty;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.RpcResponse;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.serialize.MessageDecoder;
import com.frameworkrpc.serialize.MessageEncoder;
import com.frameworkrpc.serialize.Serialize;
import com.frameworkrpc.serialize.SerializeFactory;
import com.frameworkrpc.server.AbstractServer;
import com.frameworkrpc.server.Server;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer extends AbstractServer implements Server {

	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);

	private ServerBootstrap bootstrap;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private Channel serverChannel;

	public NettyServer(URL url) {
		super(url);
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
	public boolean isBound() {
		return serverChannel != null && serverChannel.isActive();
	}

	@Override
	public void doOpen() {

		bootstrap = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
		workerGroup = new NioEventLoopGroup(RpcConstant.DEFAULT_NIO_THREAD_COUNT, new DefaultThreadFactory("NettyServerWorker", true));

		bootstrap
				.group(bossGroup, workerGroup)
				.channel(NioServerSocketChannel.class)
				.childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE)
				.childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) throws Exception {
						Serialize serialize = SerializeFactory.createSerialize(getURL().getParameter(RpcConstant.SERIALIZATION));
						ch.pipeline()
								.addLast("decoder", new MessageDecoder(serialize, RpcRequester.class))
								.addLast("encoder", new MessageEncoder(serialize, RpcResponse.class));
								//.addLast("handler", new NettyServerHandler());
					}
				});
		ChannelFuture channelFuture = bootstrap.bind(url.getIntParameter(RpcConstant.PORT));
		channelFuture.syncUninterruptibly();
		serverChannel = channelFuture.channel();
		logger.info("Netty RPC Server start success!port:{}", NetUtils.getAvailablePort());
		isOpen = true;
	}

	@Override
	public void doClose() {
		if (serverChannel != null) {
			serverChannel.close();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			bossGroup = null;
			workerGroup = null;
		}
		logger.info("Netty RPC Server shutdown success!port:{}", NetUtils.getAvailablePort());
		isClose = true;
	}
}
