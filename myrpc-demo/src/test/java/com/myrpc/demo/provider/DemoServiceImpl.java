package com.myrpc.demo.provider;

import com.myrpc.demo.api.DemoService;
import com.myrpc.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoServiceImpl implements DemoService {

	@Override
	public String sayHello(String name) {
		System.out.println(
				"[" + new SimpleDateFormat("HH:mm:ss").format(new Date()) + "] Hello " + name + ", request from consumer: " + RpcContext.getContext()
						.getRequesterId());
		return "Hello " + name + ", response from provider: " + RpcContext.getContext().getRequesterId();
	}
}
