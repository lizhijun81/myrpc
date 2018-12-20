package com.myrpc.transport.netty;

import com.myrpc.concurrent.SimpleThreadExecutor;
import com.myrpc.exception.MyRpcServerRpcException;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import com.myrpc.rpc.InstanceFactory;
import com.myrpc.rpc.RpcContext;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import io.netty.util.concurrent.Future;
import io.netty.util.concurrent.GenericFutureListener;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.RejectedExecutionException;

public class NettyAcceptorHandler extends SimpleChannelInboundHandler<RpcRequest> {

	private static final Logger logger = LoggerFactory.getLogger(NettyAcceptorHandler.class);
	private InstanceFactory nettyRpcInstanceFactory;
	private SimpleThreadExecutor threadPoolExecutor;

	public NettyAcceptorHandler(InstanceFactory nettyRpcInstanceFactory, SimpleThreadExecutor threadPoolExecutor) {
		this.nettyRpcInstanceFactory = nettyRpcInstanceFactory;
		this.threadPoolExecutor = threadPoolExecutor;
	}

	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final RpcRequest request) throws Exception {
		try {
			threadPoolExecutor.execute(new Runnable() {
				@Override
				public void run() {
					logger.debug("Receive request:{},remoteAddress:{}", request.getRequestId(), ctx.channel().remoteAddress());
					RpcResponse response = new RpcResponse();
					response.setRequestId(request.getRequestId());
					final long processStartTime = System.currentTimeMillis();
					try {
						RpcContext.init(request);
						Object result = handle(request);
						response.setResult(result);
					} catch (Exception e) {
						response.setError(
								new MyRpcServerRpcException("RPC Server handle request error request:" + NettyAcceptorHandler.toString(request), e));

						logger.error("RPC Server handle request error request:" + NettyAcceptorHandler.toString(request), e);
					}
					response.setProcessTime(System.currentTimeMillis() - processStartTime);
					sendResponse(ctx, response);
				}
			});
		} catch (RejectedExecutionException rejectException) {
			logger.warn(
					"process thread pool is full, run in io thread, active={} poolSize={} corePoolSize={} maxPoolSize={} taskCount={} requestId={}",
					threadPoolExecutor.getActiveCount(), threadPoolExecutor.getPoolSize(), threadPoolExecutor.getCorePoolSize(),
					threadPoolExecutor.getMaximumPoolSize(), threadPoolExecutor.getTaskCount(), request.getRequestId());

			RpcResponse response = new RpcResponse();
			response.setRequestId(request.getRequestId());
			response.setError(
					new MyRpcServerRpcException("process thread pool is full, reject by server: " + ctx.channel().localAddress(), rejectException));

			sendResponse(ctx, response);
		} finally {
			RpcContext.destroy();
		}
	}

	public static String toString(RpcRequest request) {
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

	private Object handle(RpcRequest request) throws Exception {
		String className = request.getInterfaceName();
		Object serviceBean = nettyRpcInstanceFactory.getInstance(className);

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
		logger.error("Server exceptionCaught: remote={} local={} event={}", ctx.channel().remoteAddress(), ctx.channel().localAddress(),
				cause.getMessage(), cause);
		ctx.channel().close();
	}
}
