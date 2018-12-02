package com.frameworkrpc.io.nio.reactor;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class ReactorClient {

	Selector selector;
	SocketChannel socketChannel;
	SocketChannel socketChannel1;
	CountDownLatch countDownLatch = new CountDownLatch(1);

	public ReactorClient Start(int port) {
		new Thread(() -> {
			try {
				selector = Selector.open();
				socketChannel = SocketChannel.open();
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_CONNECT);
				socketChannel.connect(new InetSocketAddress("127.0.0.1", port));
				for (; ; ) {
					selector.select();
					Set<SelectionKey> keys = selector.selectedKeys();
					Iterator<SelectionKey> iterator = keys.iterator();
					while (iterator.hasNext()) {
						SelectionKey key = iterator.next();
						iterator.remove();
						if (key.isConnectable()) {
							System.out.println("客户端连接中....");
							SocketChannel _socketChannel = (SocketChannel) key.channel();
							// 判断此通道上是否正在进行连接操作。
							// 完成套接字通道的连接过程。
							if (_socketChannel.isConnectionPending()) {
								_socketChannel.finishConnect();
								System.out.println("完成连接!");
								ByteBuffer buffer = ByteBuffer.allocate(1024);
								buffer.put("Hello,Server".getBytes());
								buffer.flip();
								_socketChannel.write(buffer);
								countDownLatch.countDown();
							}
							_socketChannel.register(selector, SelectionKey.OP_READ);
						}
						if (key.isReadable()) {
							System.out.println("客户端收到服务器的响应....");
							SocketChannel _socketChannel = (SocketChannel) key.channel();
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							int count = _socketChannel.read(buffer);
							if (count > 0) {
								buffer.flip();
								byte[] response = new byte[buffer.remaining()];
								buffer.get(response);
								System.out.println("___结果为：" + new String(response));
							}

						}
					}
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}).start();
		return this;
	}


	public void send(String inStr) {
		new Thread(() -> {
			for (; ; ) {
				try {
					countDownLatch.await();
					if (socketChannel != null) {
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						buffer.put(inStr.getBytes());
						buffer.flip();
						socketChannel.write(buffer);
					}
					Thread.currentThread().sleep(1000);
				} catch (IOException e) {
					e.printStackTrace();
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
