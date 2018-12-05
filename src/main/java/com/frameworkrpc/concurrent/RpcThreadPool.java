package com.frameworkrpc.concurrent;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class RpcThreadPool {

	private static final Logger logger = LoggerFactory.getLogger(RpcThreadPool.class);

	public static ExecutorService getExecutor(URL url) {
		logger.info("ThreadPool Core[threads:" + url.getParameter(RpcConstant.THREADS) + ", threadpool:" + url.getParameter(RpcConstant.THREADPOOL)
				+ "]");
		boolean fixedThreadpool = url.getParameter(RpcConstant.THREADPOOL).equals("fixed");
		final String name = "rpcthreadpool";
		int corePoolSize = url.getIntParameter(RpcConstant.THREADS);
		int maximumPoolSize = fixedThreadpool ? url.getIntParameter(RpcConstant.THREADS) : Integer.MAX_VALUE;
		ThreadPoolExecutor executor = new ThreadPoolExecutor(
				corePoolSize,
				maximumPoolSize,
				fixedThreadpool ? 0 : 60 * 1000,
				TimeUnit.MILLISECONDS,
				fixedThreadpool ? new LinkedBlockingQueue(1024) : new SynchronousQueue<Runnable>(),
				new ThreadFactoryBuilder().setNameFormat(name).build(),
				new ThreadPoolExecutor.AbortPolicy());
		return executor;
	}
}
