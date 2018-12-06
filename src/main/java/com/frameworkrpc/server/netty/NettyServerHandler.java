package com.frameworkrpc.server.netty;

import com.frameworkrpc.exception.RpcException;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.RpcResponse;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.rpc.NettyRpcInstanceFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.RejectedExecutionException;
import java.util.concurrent.ThreadPoolExecutor;


public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequester> {

	private static final Logger logger = LoggerFactory.getLogger(NettyServerHandler.class);

	private URL url;
	private NettyRpcInstanceFactory nettyRpcInstanceFactory;
	private ExecutorService threadPoolExecutor;

	public URL getUrl() {
		return url;
	}

	public NettyRpcInstanceFactory getNettyRpcInstanceFactory() {
		return nettyRpcInstanceFactory;
	}

	public ExecutorService getThreadPoolExecutor() {
		return threadPoolExecutor;
	}

	public NettyServerHandler(URL url, NettyRpcInstanceFactory nettyRpcInstanceFactory, ExecutorService threadPoolExecutor) {
		this.url = url;
		this.nettyRpcInstanceFactory = nettyRpcInstanceFactory;
		this.threadPoolExecutor = threadPoolExecutor;
	}


	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final RpcRequester request) throws Exception {
		try {
			threadPoolExecutor.execute(new Runnable() {
				@Override
				public void run() {
					logger.debug("Receive request:{},remoteAddress:{}", request.getRequestId(), ctx.channel().remoteAddress());
					RpcResponse response = new RpcResponse();
					response.setRequestId(request.getRequestId());
					final long processStartTime = System.currentTimeMillis();
					try {
						Object result = handle(request);
						response.setResult(result);
					} catch (Exception e) {
						response.setError(new RpcException("RPC Server handle request error request:" + NettyServerHandler.toString(request), e));
						logger.error("RPC Server handle request error request:" + NettyServerHandler.toString(request), e);
					}
					response.setProcessTime(System.currentTimeMillis() - processStartTime);
					sendResponse(ctx, response);
				}
			});
		} catch (RejectedExecutionException rejectException) {
			logger.warn(
					"process thread pool is full, run in io thread, active={} poolSize={} corePoolSize={} maxPoolSize={} taskCount={} requestId={}",
					((ThreadPoolExecutor) threadPoolExecutor).getActiveCount(), ((ThreadPoolExecutor) threadPoolExecutor).getPoolSize(),
					((ThreadPoolExecutor) threadPoolExecutor).getCorePoolSize(), ((ThreadPoolExecutor) threadPoolExecutor).getMaximumPoolSize(),
					((ThreadPoolExecutor) threadPoolExecutor).getTaskCount(), request.getRequestId());
			RpcResponse response = new RpcResponse();
			response.setRequestId(request.getRequestId());
			response.setError(new RpcException("process thread pool is full, reject by server: " + ctx.channel().localAddress(), rejectException));
			sendResponse(ctx, response);
		}
	}

	public static String toString(RpcRequester request) {
		return "requestId=" + request.getRequestId() + " interface=" + request.getInterfaceName() + " method=" + request.getMethodName();
	}

	private void sendResponse(ChannelHandlerContext ctx, RpcResponse response) {
		ctx.writeAndFlush(response).addListener(new GenericFutureListener() {
			@Override
			public void operationComplete(Future future) throws Exception {
				logger.debug("Send response for request:{}", response.getRequestId());
			}
		});
	}

	private Object handle(RpcRequester request) throws Exception {
		String className = request.getInterfaceName();
		Object serviceBean = nettyRpcInstanceFactory.getRpcInstance(className);

		Class<?> serviceClass = serviceBean.getClass();
		String methodName = request.getMethodName();
		Class<?>[] parameterTypes = request.getParameterTypes();
		Object[] parameters = request.getParameters();

		// JDK reflect
        /*Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);*/

		// Cglib reflect
		FastClass serviceFastClass = FastClass.create(serviceClass);
		FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
		return serviceFastMethod.invoke(serviceBean, parameters);
	}


	@Override
	public void channelActive(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelActive();
	}

	@Override
	public void channelInactive(ChannelHandlerContext ctx) throws Exception {
		ctx.fireChannelInactive();
	}

	@Override
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) throws Exception {
		logger.error("Server exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(), cause.getMessage(), cause);
		ctx.channel().close();
	}
}
