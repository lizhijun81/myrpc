package com.myrpc.consumer.future;

import com.myrpc.exception.MyRpcRemotingException;
import com.myrpc.exception.MyRpcTimeOutException;
import com.myrpc.model.RpcRequest;
import com.myrpc.model.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import static com.myrpc.common.Preconditions.checkNotNull;

abstract class AbstractFuture<V> implements InvokeFuture<V> {

	private final Logger logger = LoggerFactory.getLogger(getClass());
	protected static final ConcurrentHashMap<String, DefaultInvokeFuture<?>> pendingFutures = new ConcurrentHashMap<>();
	protected RpcRequest request;
	protected RpcResponse response;
	protected Class<V> returnType;
	protected long startTime;
	protected static final int NEW = 0;
	protected static final int COMPLETING = 1;
	protected static final int NORMAL = 2;
	protected static final int EXCEPTIONAL = 3;
	protected AtomicInteger state = new AtomicInteger(NEW);
	protected List<Listener<V>> listeners = new ArrayList<>();
	protected Lock lock = new ReentrantLock();
	protected Condition condition = lock.newCondition();
	protected Object outcome;

	@Override
	public InvokeFuture<V> with(Class<V> returnType) {
		this.returnType = returnType;
		return this;
	}

	protected int state() {
		return state.get();
	}

	protected Object outcome() {
		return outcome;
	}

	@Override
	public boolean isDone() {
		return state.get() > COMPLETING;
	}

	@Override
	public V get() throws InterruptedException {
		return get(0L, TimeUnit.MILLISECONDS);
	}

	@Override
	public V get(long timeout, TimeUnit unit) throws InterruptedException {
		if (unit == null)
			throw new NullPointerException("unit");

		if (state() <= COMPLETING) {
			try {
				lock.lock();
				state.set(COMPLETING);
				awaitDone(timeout, unit);
			} finally {
				lock.unlock();
			}
		}
		return get(state());
	}

	private V get(int s) {
		Object x = outcome;
		if (s == NORMAL) {
			return (V) x;
		}
		throw new MyRpcRemotingException((Throwable) x);
	}

	private void awaitDone(long timeout, TimeUnit unit) throws InterruptedException {
		if (timeout == 0L)
			condition.await();
		else {
			boolean isTimeout = condition.await(timeout, unit);

			if (!isTimeout) {
				pendingFutures.remove(this.request.getRequestId());
				throw new MyRpcTimeOutException(
						"Timeout exception. Request id: " + this.request.getRequestId() + ". Request class name: " + this.request.getInterfaceName()
								+ ". Request method: " + this.request.getMethodName());
			}
		}
	}

	public void setSuccess(V result) {
		try {
			lock.lock();

			outcome = result;
			state.set(NORMAL);
			done(state(), outcome());

			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	public void setFailure(Throwable lastCause) {
		try {
			lock.lock();

			outcome = lastCause;
			state.set(EXCEPTIONAL);
			done(state(), outcome());

			condition.signal();
		} finally {
			lock.unlock();
		}
	}

	@Override
	public void done(RpcResponse response) {

		this.response = response;

		if (this.response.getError() == null) {
			setSuccess((V) this.response.getResult());
		} else {
			setFailure(this.response.getError());
		}

		long responseTime = System.currentTimeMillis() - startTime;
		logger.debug("Service response Request id: " + this.request.getRequestId() + ". Request interfaceName: " + this.request.getInterfaceName()
				+ ". Request method: " + this.request.getMethodName() + ". Response Time: " + responseTime + "ms");
	}

	public static void received(RpcResponse response) {
		InvokeFuture invokeFuture = pendingFutures.get(response.getRequestId());

		if (invokeFuture == null) {
			return;
		}

		pendingFutures.remove(response.getRequestId());
		invokeFuture.done(response);

	}

	public static void fakeReceived(RpcResponse response) {

		InvokeFuture invokeFuture = pendingFutures.remove(response.getRequestId());

		if (invokeFuture == null) {
			return;
		}

		invokeFuture.done(response);
	}


	@Override
	public boolean isCancelled() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean cancel(boolean mayInterruptIfRunning) {
		throw new UnsupportedOperationException();
	}

	@Override
	public InvokeFuture<V> addListener(Listener<V> listener) {
		checkNotNull(listener, "listener");

		synchronized (this) {
			addListener0(listener);
		}

		if (isDone()) {
			notifyListeners(state(), outcome());
		}

		return this;
	}

	@Override
	public InvokeFuture<V> addListeners(Listener<V>... listeners) {
		checkNotNull(listeners, "listeners");

		synchronized (this) {
			for (Listener<V> listener : listeners) {
				if (listener == null) {
					continue;
				}
				addListener0(listener);
			}
		}

		if (isDone()) {
			notifyListeners(state(), outcome());
		}

		return this;
	}

	@Override
	public InvokeFuture<V> removeListener(Listener<V> listener) {
		checkNotNull(listener, "listener");

		synchronized (this) {
			removeListener0(listener);
		}

		return this;
	}

	@Override
	public InvokeFuture<V> removeListeners(Listener<V>... listeners) {
		checkNotNull(listeners, "listeners");

		synchronized (this) {
			for (Listener<V> listener : listeners) {
				if (listener == null) {
					continue;
				}
				removeListener0(listener);
			}
		}
		return this;
	}

	protected void notifyListeners(int state, Object x) {
		List<Listener<V>> listeners;
		synchronized (this) {
			if (this.listeners.isEmpty()) {
				return;
			}

			listeners = this.listeners;
			//this.listeners = null;
		}
		for (Listener<V> listener : listeners)
			notifyListener0(listener, state, x);
	}

	private void addListener0(Listener<V> listener) {
		listeners.add(listener);
	}

	private void removeListener0(Listener<V> listener) {
		listeners.remove(listener);
	}

	protected abstract void notifyListener0(Listener<V> listener, int state, Object x);

	protected abstract void done(int state, Object x);

	@Override
	public String toString() {
		final String status;
		switch (state.get()) {
			case NORMAL:
				status = "[Completed normally]";
				break;
			case EXCEPTIONAL:
				status = "[Completed exceptionally: " + response + "]";
				break;
			default:
				status = "[Not completed]";
		}
		return super.toString() + status;
	}
}
