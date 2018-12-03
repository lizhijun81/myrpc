package com.frameworkrpc.server.netty;

import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.rpc.NettyRpcInstanceFactory;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class NettyServerHandler extends SimpleChannelInboundHandler<RpcRequester> {

	private static final Logger logger = LoggerFactory.getLogger(RpcRequester.class);

	private NettyRpcInstanceFactory nettyRpcInstanceFactory;

	public NettyServerHandler(NettyRpcInstanceFactory nettyRpcInstanceFactory) {
		this.nettyRpcInstanceFactory = nettyRpcInstanceFactory;
	}

	@Override
	public void channelRead0(final ChannelHandlerContext ctx, final RpcRequester request) throws Exception {
		//		RpcServer.submit(new Runnable() {
		//			@Override
		//			public void run() {
		//				logger.debug("Receive request " + request.getRequestId());
		//				RpcResponse response = new RpcResponse();
		//				response.setRequestId(request.getRequestId());
		//				try {
		//					Object result = handle(request);
		//					response.setResult(result);
		//				} catch (Throwable t) {
		//					response.setError(t.toString());
		//					logger.error("RPC Server handle request error",t);
		//				}
		//				ctx.writeAndFlush(response).addListener(new ChannelFutureListener() {
		//					@Override
		//					public void operationComplete(ChannelFuture channelFuture) throws Exception {
		//						logger.debug("Send response for request " + request.getRequestId());
		//					}
		//				});
		//			}
		//		});
	}

	private Object handle(RpcRequester request) throws Throwable {
		String className = request.getClassName();
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
	public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause) {
		logger.error("server caught exception", cause);
		ctx.close();
	}
}
