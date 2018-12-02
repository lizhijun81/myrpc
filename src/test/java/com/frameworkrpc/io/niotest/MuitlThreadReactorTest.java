package com.frameworkrpc.io.niotest;


import com.frameworkrpc.io.nio.reactor.ReactorClient;
import com.frameworkrpc.io.nio.reactor.muitlthread.Acceptor;

public class MuitlThreadReactorTest {

	public static void main(String[] args) {
		new Thread(new Acceptor(12345)).start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new ReactorClient().Start(12345).send("Hello \r\n");
		new ReactorClient().Start(12345).send("Hello1 \r\n");
	}
}
