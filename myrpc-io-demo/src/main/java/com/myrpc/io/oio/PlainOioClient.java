package com.myrpc.io.oio;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

public class PlainOioClient {

	public void send(int port, String inStr) {
		new Thread(() -> {
			for (; ; ) {
				try {
					try (Socket socket = new Socket("127.0.0.1", port)) {
						try (BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
								PrintWriter out = new PrintWriter(socket.getOutputStream(), true)) {
							out.println(inStr);
							System.out.println("___结果为：" + in.readLine());
						}
					}
					Thread.currentThread().sleep(1000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		}).start();

	}
}
