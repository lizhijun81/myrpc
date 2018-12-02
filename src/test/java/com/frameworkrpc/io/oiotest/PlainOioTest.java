package com.frameworkrpc.io.oiotest;


import com.frameworkrpc.io.oio.PlainOioClient;
import com.frameworkrpc.io.oio.PlainOioServer;

public class PlainOioTest {
	public static void main(String[] args) {
		new Thread(() -> {
			try {
				new PlainOioServer().serve(12345);
			} catch (Exception e) {
				e.printStackTrace();
			}

		}).start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new PlainOioClient().send(12345, "Hello \r\n");


	}
}
