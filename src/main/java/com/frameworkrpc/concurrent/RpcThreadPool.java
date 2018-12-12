package com.frameworkrpc.concurrent;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.model.URL;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class RpcThreadPool {

	private static final Logger logger = LoggerFactory.getLogger(RpcThreadPool.class);

	public static ExecutorService getExecutor(URL url) {
		logger.info("ThreadPool Core[threads:" + url.getParameter(RpcConstants.THREADS_KEY) + ", threadpool:" + url.getParameter(RpcConstants.THREADPOOL_KEY)
				+ "]");
		boolean fixedThreadpool = url.getParameter(RpcConstants.THREADPOOL_KEY).equals("fixed");
		final String name = "rpcthreadpool";
		int corePoolSize = url.getIntParameter(RpcConstants.THREADS_KEY);
		int maximumPoolSize = fixedThreadpool ? url.getIntParameter(RpcConstants.THREADS_KEY) : Integer.MAX_VALUE;
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
