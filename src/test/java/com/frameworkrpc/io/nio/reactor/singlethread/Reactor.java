package com.frameworkrpc.io.nio.reactor.singlethread;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

/**
 * 单线程模型
 */
public class Reactor implements Runnable {

	private Selector selector;
	private ServerSocketChannel serverSocketChannel;
	private int port;

	public Reactor(int port) {
		this.port = port;
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
					try {
						//处理新接入的请求消息
						if (key.isAcceptable()) {
							ServerSocketChannel serverSocketChannel = (ServerSocketChannel) key.channel();
							//通过ServerSocketChannel的accept创建SocketChannel实例
							//完成该操作意味着完成TCP三次握手，TCP物理链路正式建立
							SocketChannel socketChannel = serverSocketChannel.accept();
							socketChannel.configureBlocking(false);
							socketChannel.register(selector, SelectionKey.OP_READ);
							System.out.println("收到客户端的连接请求。。。");
						}
						//读消息
						if (key.isReadable()) {
							SocketChannel socketChannel = (SocketChannel) key.channel();
							ByteBuffer buffer = ByteBuffer.allocate(1024);
							int readBytes = socketChannel.read(buffer);
							if (readBytes > 0) {
								//将缓冲区当前的limit设置为position=0，用于后续对缓冲区的读取操作
								buffer.flip();
								byte[] bytes = new byte[buffer.remaining()];
								buffer.get(bytes);
								String intStr = "服务器收到消息：" + new String(bytes, "UTF-8");
								byte[] bytes1 = intStr.getBytes();
								ByteBuffer writeBuffer = ByteBuffer.allocate(bytes1.length);
								writeBuffer.put(bytes1);
								writeBuffer.flip();
								socketChannel.write(writeBuffer);
								//socketChannel.register(selector, SelectionKey.OP_WRITE, writeBuffer);
							}
						}
//						if (key.isWritable()) {
//							SocketChannel socketChannel = (SocketChannel)key.channel();
//							ByteBuffer buf = (ByteBuffer)key.attachment();
//							buf.flip();
//							socketChannel.write(buf);
//							System.out.println("服务端向客户端发送数据。。。");
//						}
					} finally {
//						if (key != null) {
//							key.cancel();
//							if (key.channel() != null) {
//								key.channel().close();
//							}
//						}
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
