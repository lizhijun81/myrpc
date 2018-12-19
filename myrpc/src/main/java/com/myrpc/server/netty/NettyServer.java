package com.myrpc.server.netty;

import com.myrpc.common.NetUtils;
import com.myrpc.common.RpcConstants;
import com.myrpc.concurrent.SimpleThreadExecutor;
import com.myrpc.extension.ExtensionLoader;
import com.myrpc.extension.Scope;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import com.myrpc.model.URL;
import com.myrpc.rpc.RpcInstanceFactory;
import com.myrpc.serialize.MessageDecoder;
import com.myrpc.serialize.MessageEncoder;
import com.myrpc.serialize.Serialize;
import com.myrpc.server.AbstractServer;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.buffer.PooledByteBufAllocator;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleState;
import io.netty.handler.timeout.IdleStateEvent;
import io.netty.handler.timeout.IdleStateHandler;
import io.netty.util.concurrent.DefaultThreadFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class NettyServer extends AbstractServer {

	private static final Logger logger = LoggerFactory.getLogger(NettyServer.class);
	private static SimpleThreadExecutor threadPoolExecutor;
	private ServerBootstrap bootstrap;
	private EventLoopGroup bossGroup;
	private EventLoopGroup workerGroup;
	private Channel serverChannel;
	private final RpcInstanceFactory rpcInstanceFactory;
	private final Serialize serialize;

	public NettyServer(URL url, RpcInstanceFactory rpcInstanceFactory) {
		super(url);
		this.rpcInstanceFactory = rpcInstanceFactory;

		this.serialize = ExtensionLoader.getExtensionLoader(Serialize.class)
				.getExtension(url.getParameter(RpcConstants.SERIALIZATION_KEY), Scope.SINGLETON);

		if (threadPoolExecutor == null) {
			synchronized (NettyServer.class) {
				if (threadPoolExecutor == null) {
					threadPoolExecutor = new SimpleThreadExecutor(url);
				}
			}
		}
	}

	@Override
	public boolean isOpened() {
		return serverChannel != null && serverChannel.isActive();
	}

	@Override
	public boolean isClosed() {
		return serverChannel == null || !serverChannel.isActive();
	}


	@Override
	public synchronized void doOpen() {

		bootstrap = new ServerBootstrap();
		bossGroup = new NioEventLoopGroup(1, new DefaultThreadFactory("NettyServerBoss", true));
		workerGroup = new NioEventLoopGroup(url.getIntParameter(RpcConstants.IOTHREADS_KEY), new DefaultThreadFactory("NettyServerWorker", true));

		int readerIdleTimeSeconds = url.getIntParameter(RpcConstants.HEARTBEAT_KEY);

		bootstrap.group(bossGroup, workerGroup).channel(NioServerSocketChannel.class).childOption(ChannelOption.TCP_NODELAY, Boolean.TRUE)
				.childOption(ChannelOption.SO_REUSEADDR, Boolean.TRUE).childOption(ChannelOption.ALLOCATOR, PooledByteBufAllocator.DEFAULT)
				.childHandler(new ChannelInitializer<NioSocketChannel>() {
					@Override
					protected void initChannel(NioSocketChannel ch) {
						ch.pipeline().addLast("idlestate", new IdleStateHandler(readerIdleTimeSeconds, 0, 0) {
							@Override
							public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
								if (evt instanceof IdleStateEvent) {
									IdleStateEvent e = (IdleStateEvent) evt;
									if (e.state() == IdleState.READER_IDLE) {
										ctx.close();
									} else if (e.state() == IdleState.WRITER_IDLE) {
										//ctx.writeAndFlush(new PingMessage());
									}
								} else {
									super.userEventTriggered(ctx, evt);
								}
							}
						}).addLast("decoder", new MessageDecoder(serialize, RpcRequest.class))
								.addLast("encoder", new MessageEncoder(serialize, RpcResponse.class))
								.addLast("handler", new NettyServerHandler(rpcInstanceFactory, threadPoolExecutor));
					}
				});
		ChannelFuture channelFuture = bootstrap.bind(url.getIntParameter(RpcConstants.PORT_KEY));
		channelFuture.syncUninterruptibly();
		serverChannel = channelFuture.channel();
		logger.info("Netty RPC Server start success!port:{}", NetUtils.getAvailablePort());
	}

	@Override
	public synchronized void doClose() {
		if (serverChannel != null) {
			serverChannel.close();
			bossGroup.shutdownGracefully();
			workerGroup.shutdownGracefully();
			bossGroup = null;
			workerGroup = null;
		}
		logger.info("Netty RPC Server shutdown success!port:{}", NetUtils.getAvailablePort());
	}
}
