package com.myrpc.io.nio.reactor.mainsubmuitlthread;

import java.nio.channels.ServerSocketChannel;

public class MainReactorThreadGroup {
	private final int nioThreadCount;  // 线程池IO线程的数量
	private static final int DEFAULT_NIO_THREAD_COUNT;
	private MainReactorThread[] nioThreads;

	static {
		DEFAULT_NIO_THREAD_COUNT = 1;
	}

	public MainReactorThreadGroup(SubReactorThreadGroup subReactorThreadGroup) {
		this(DEFAULT_NIO_THREAD_COUNT, subReactorThreadGroup);
	}

	public MainReactorThreadGroup(int threadCount, SubReactorThreadGroup subReactorThreadGroup) {
		if (threadCount < 1) {
			threadCount = DEFAULT_NIO_THREAD_COUNT;
		}
		this.nioThreadCount = threadCount;
		this.nioThreads = new MainReactorThread[threadCount];
		for (int i = 0; i < threadCount; i++) {
			this.nioThreads[i] = new MainReactorThread(subReactorThreadGroup);
			this.nioThreads[i].start(); //构造方法中启动线程，由于nioThreads不会对外暴露，故不会引起线程逃逸
		}
		System.out.println("Main Nio 线程数量：" + threadCount);
	}

	public void dispatch(ServerSocketChannel serverSocketChannel) {
		if (serverSocketChannel != null) {
			System.out.print("serverSocketChannel hashCode:" + serverSocketChannel.hashCode() + "\r\n");
			this.nioThreads[serverSocketChannel.hashCode() % nioThreadCount].register(serverSocketChannel);
		}
	}
}
