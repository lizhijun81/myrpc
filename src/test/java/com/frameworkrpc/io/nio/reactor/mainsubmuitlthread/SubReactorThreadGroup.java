package com.frameworkrpc.io.nio.reactor.mainsubmuitlthread;

import java.nio.channels.SocketChannel;


public class SubReactorThreadGroup {

	private final int nioThreadCount;  // 线程池IO线程的数量
	private static final int DEFAULT_NIO_THREAD_COUNT;
	private SubReactorThread[] nioThreads;

	static {
		//		DEFAULT_NIO_THREAD_COUNT = Runtime.getRuntime().availableProcessors() > 1
		//				? 2 * (Runtime.getRuntime().availableProcessors() - 1 ) : 2;
		DEFAULT_NIO_THREAD_COUNT = 4;
	}

	public SubReactorThreadGroup() {
		this(DEFAULT_NIO_THREAD_COUNT);
	}

	public SubReactorThreadGroup(int threadCount) {
		if (threadCount < 1) {
			threadCount = DEFAULT_NIO_THREAD_COUNT;
		}
		this.nioThreadCount = threadCount;
		this.nioThreads = new SubReactorThread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			this.nioThreads[i] = new SubReactorThread();
			this.nioThreads[i].start(); //构造方法中启动线程，由于nioThreads不会对外暴露，故不会引起线程逃逸
		}
		System.out.println("Sub Nio 线程数量：" + threadCount);
	}

	public void dispatch(SocketChannel socketChannel) {
		if (socketChannel != null) {
			System.out.print("socketChannel hashCode:" + socketChannel.hashCode() + "\r\n");
			this.nioThreads[socketChannel.hashCode() % nioThreadCount].register(socketChannel);
		}
	}
}


