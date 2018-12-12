package com.frameworkrpc.proxy;

import com.frameworkrpc.exception.InvokeException;
import com.frameworkrpc.model.RpcRequester;
import com.frameworkrpc.model.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class RPCFuture implements Future<Object> {

	private static final Logger logger = LoggerFactory.getLogger(RPCFuture.class);
	public static final ConcurrentHashMap<String, RPCFuture> pendingRPC = new ConcurrentHashMap<>();
	private RpcRequester request;
	private RpcResponse response;
	private long startTime;

	private Lock lock = new ReentrantLock();
	private Condition finish = lock.newCondition();

	public RPCFuture(RpcRequester request) {
		this.request = request;
		this.startTime = System.currentTimeMillis();
	}

	@Override
	public boolean isDone() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object get() throws InterruptedException, ExecutionException {
		try {
			lock.lock();
			finish.await();
			if (this.response != null) {
				return this.response.getResult();
			} else {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	public void done(RpcResponse reponse) {
		try {
			lock.lock();
			finish.signal();
			this.response = reponse;
			long responseTime = System.currentTimeMillis() - startTime;
			logger.debug("Service response Request id: " + this.request.getRequestId() + ". Request interfaceName: " + this.request.getInterfaceName()
					+ ". Request method: " + this.request.getMethodName() + ". Response Time: " + responseTime + "ms");
		} finally {
			lock.unlock();
		}
	}

	@Override
	public Object get(long timeout, TimeUnit unit) {
		try {
			lock.lock();
			await(timeout, unit);
			if (this.response != null) {
				return this.response.getResult();
			} else {
				return null;
			}
		} finally {
			lock.unlock();
		}
	}

	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}

	private void await(long timeout, TimeUnit unit) {
		boolean isTimeout = false;
		try {
			isTimeout = finish.await(timeout, unit);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		if (!isTimeout) {
			pendingRPC.remove(this.request.getRequestId());
			throw new InvokeException(
					"Timeout exception. Request id: " + this.request.getRequestId() + ". Request class name: " + this.request.getInterfaceName()
							+ ". Request method: " + this.request.getMethodName());
		}
	}
}

