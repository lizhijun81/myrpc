package com.myrpc.concurrent;

import com.myrpc.common.RpcConstants;
import com.myrpc.config.URL;
import com.google.common.util.concurrent.ThreadFactoryBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.*;

public class SimpleThreadExecutor {

	private static final Logger logger = LoggerFactory.getLogger(SimpleThreadExecutor.class);

	private ThreadPoolExecutor executor;

	public SimpleThreadExecutor(URL url) {
		this.executor = getExecutor(url);
	}

	public void execute(Runnable command) {
		((ExecutorService) executor).execute(command);
	}

	public int getPoolSize() {
		return executor.getPoolSize();
	}

	public int getActiveCount() {
		return executor.getActiveCount();
	}

	public int getCorePoolSize() {
		return executor.getCorePoolSize();
	}

	public int getMaximumPoolSize() {
		return executor.getMaximumPoolSize();
	}

	public int getLargestPoolSize() {
		return executor.getLargestPoolSize();
	}

	public long getTaskCount() {
		return executor.getTaskCount();
	}

	public long getCompletedTaskCount() {
		return executor.getCompletedTaskCount();
	}

	private  ThreadPoolExecutor getExecutor(URL url) {

		logger.info("ThreadPool Core[threads:" + url.getParameter(RpcConstants.THREADS_KEY) + ", threadpool:" + url
				.getParameter(RpcConstants.THREADPOOL_KEY) + "]");

		boolean fixedThreadpool = url.getParameter(RpcConstants.THREADPOOL_KEY).equals("fixed");

		final String name = "rpcthreadpool";

		final int corePoolSize = url.getIntParameter(RpcConstants.THREADS_KEY);
		final int maximumPoolSize = fixedThreadpool ? url.getIntParameter(RpcConstants.THREADS_KEY) : Integer.MAX_VALUE;
		final int keepAliveTime = fixedThreadpool ? 0 : 60 * 1000;
		final BlockingQueue<Runnable> workQueue = fixedThreadpool ? new LinkedBlockingQueue(1024) : new SynchronousQueue<Runnable>();
		final ThreadFactory threadFactory = new ThreadFactoryBuilder().setNameFormat(name).build();
		final RejectedExecutionHandler handler = new ThreadPoolExecutor.AbortPolicy();

		ThreadPoolExecutor executor = new ThreadPoolExecutor(corePoolSize, maximumPoolSize, keepAliveTime, TimeUnit.MILLISECONDS, workQueue,
				threadFactory, handler);

		return executor;
	}
}
