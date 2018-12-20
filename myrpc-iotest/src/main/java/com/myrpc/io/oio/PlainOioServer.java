package com.myrpc.io.oio;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PlainOioServer {

	static final ExecutorService executorService = Executors.newFixedThreadPool(2);

	public void serve(int port) throws IOException {
		try (final ServerSocket socket = new ServerSocket(port)) {
			System.out.println("服务器已启动，端口号：" + port);
			for (; ; ) {
				final Socket clientSocket = socket.accept();
				executorService.execute(new PlainOioServerHandler(clientSocket));
			}
		}
	}
}
