package com.myrpc.consumer.future;

import com.myrpc.model.RpcRequest;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import static com.myrpc.common.StackTraceUtils.stackTrace;

public class DefaultInvokeFuture<V> extends AbstractFuture<V> {

	private static final Logger logger = LoggerFactory.getLogger(DefaultInvokeFuture.class);

	public DefaultInvokeFuture(RpcRequest request) {
		this.request = request;
		this.startTime = System.currentTimeMillis();
		pendingFutures.put(request.getRequestId(), this);
	}

	@Override
	public Class<V> returnType() {
		return returnType;
	}

	@Override
	protected void notifyListener0(Listener<V> listener, int state, Object x) {
		try {
			if (state == NORMAL) {
				listener.complete((V) x);
			} else {
				listener.failure((Throwable) x);
			}
		} catch (Throwable t) {
			logger.error("An exception was thrown by {}.{}, {}.", listener.getClass().getName(), state == NORMAL ? "complete()" : "failure()",
					stackTrace(t));
		}
	}

	@Override
	protected void done(int state, Object x) {
		notifyListeners(state, x);
	}
}

