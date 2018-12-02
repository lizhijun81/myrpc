package com.frameworkrpc.io.nio.reactor.mainsubmuitlthread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;

/**
 * 主从多线程模型
 */
public class Acceptor implements Runnable {

	private ServerSocketChannel serverSocketChannel;
	private MainReactorThreadGroup mainReactorThreadGroup;
	private SubReactorThreadGroup subReactorThreadGroup;
	private int port;

	public Acceptor(int port) {
		this.port = port;
		this.subReactorThreadGroup = new SubReactorThreadGroup();
		this.mainReactorThreadGroup = new MainReactorThreadGroup(subReactorThreadGroup);
	}

	@Override
	public void run() {
		try {
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			mainReactorThreadGroup.dispatch(serverSocketChannel);
			System.out.println("服务器已启动，端口号：" + port);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
