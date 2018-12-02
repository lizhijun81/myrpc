package com.frameworkrpc.io.niotest;


import com.frameworkrpc.io.nio.reactor.ReactorClient;
import com.frameworkrpc.io.nio.reactor.singlethread.Reactor;

public class SingleThreadReactorTest {

	public static void main(String[] args) {
		new Thread(new Reactor(12345)).start();

		try {
			Thread.sleep(100);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		new ReactorClient().Start(12345).send("Hello \r\n");


	}
}
