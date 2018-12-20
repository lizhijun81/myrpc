package com.myrpc.io.nio.reactor.mainsubmuitlthread;

import java.io.IOException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class MainReactorThread extends Thread {

	private Selector selector;
	private SubReactorThreadGroup subReactorThreadGroup;
	private CountDownLatch countDownLatch = new CountDownLatch(1);

	public MainReactorThread(SubReactorThreadGroup subReactorThreadGroup) {
		try {
			selector = Selector.open();
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.subReactorThreadGroup = subReactorThreadGroup;
	}

	public void register(ServerSocketChannel serverSocketChannel) {
		if (serverSocketChannel != null) {
			try {
				serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
				if (countDownLatch.getCount() > 0)
					countDownLatch.countDown();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void run() {
		try {
			try {
				countDownLatch.await();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			for (; ; ) {
				selector.select();
				Set<SelectionKey> keys = selector.selectedKeys();
				Iterator<SelectionKey> iterator = keys.iterator();
				while (iterator.hasNext()) {
					SelectionKey key = iterator.next();
					iterator.remove();
					if (key.isAcceptable()) {
						ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
						SocketChannel socketChannel = serverSocketChannel.accept();
						System.out.println("收到客户端的连接请求。。。");
						subReactorThreadGroup.dispatch(socketChannel);
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			if (selector != null) {
				try {
					selector.close();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
			selector = null;
		}
	}
}
