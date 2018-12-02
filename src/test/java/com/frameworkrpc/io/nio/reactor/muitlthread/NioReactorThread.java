package com.frameworkrpc.io.nio.reactor.muitlthread;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;
import java.util.concurrent.CountDownLatch;

public class NioReactorThread extends Thread {

	private Selector selector;
	private CountDownLatch countDownLatch = new CountDownLatch(1);

	public void register(SocketChannel socketChannel) {
		if (socketChannel != null) {
			try {
				socketChannel.configureBlocking(false);
				socketChannel.register(selector, SelectionKey.OP_READ);
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
			selector = Selector.open();
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
					if (key.isReadable()) {
						SocketChannel socketChannel = (SocketChannel) key.channel();
						ByteBuffer buffer = ByteBuffer.allocate(1024);
						int readBytes = socketChannel.read(buffer);
						if (readBytes > 0) {
							//将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
							buffer.flip();
							byte[] bytes = new byte[buffer.remaining()];
							buffer.get(bytes);
							String intStr = "服务器收到消息：" + Thread.currentThread().getName() + " " + new String(bytes, "UTF-8");
							byte[] bytes1 = intStr.getBytes();
							ByteBuffer writeBuffer = ByteBuffer.allocate(bytes1.length);
							writeBuffer.put(bytes1);
							writeBuffer.flip();
							socketChannel.write(writeBuffer);
							//socketChannel.register(selector, SelectionKey.OP_WRITE, writeBuffer);
						}
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
