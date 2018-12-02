package com.frameworkrpc.io.nio.reactor.muitlthread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 多线程模型
 */
public class Acceptor implements Runnable {

	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private int port;
	private NioReactorThreadGroup nioReactorThreadGroup;

	public Acceptor(int port) {
		this.port = port;
		this.nioReactorThreadGroup = new NioReactorThreadGroup();
	}

	@Override
	public void run() {
		try {
			selector = Selector.open();
			serverSocketChannel = ServerSocketChannel.open();
			serverSocketChannel.configureBlocking(false);
			serverSocketChannel.socket().bind(new InetSocketAddress(port));
			serverSocketChannel.register(selector, SelectionKey.OP_ACCEPT);
			System.out.println("服务器已启动，端口号：" + port);
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
						nioReactorThreadGroup.dispatch(socketChannel);
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
