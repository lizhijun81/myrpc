package com.frameworkrpc.demo.provider;

import com.frameworkrpc.demo.api.DemoService;
import com.frameworkrpc.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoServiceImpl implements DemoService {
	@Override
	public String sayHello(String name) {
		System.out.println(
				"[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext.getContext()
						.getClientRpcRequesterId());
		return "Hello " + name + ", response from provider: " + RpcContext.getContext().getClientRpcRequesterId();
	}
}
