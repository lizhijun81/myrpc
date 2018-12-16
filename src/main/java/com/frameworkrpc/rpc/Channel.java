package com.frameworkrpc.rpc;

import java.net.SocketAddress;

public interface Channel {

	String id();

	boolean isActive();

	boolean inIoThread();

	SocketAddress localAddress();

	SocketAddress remoteAddress();

	boolean isWritable();

	boolean isAutoRead();

	void setAutoRead(boolean autoRead);

	Channel close();

	Channel close(FutureListener<Channel> listener);

	Channel write(Object msg);

	Channel write(Object msg, FutureListener<Channel> listener);

}
