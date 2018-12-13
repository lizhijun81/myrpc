## myrpc
a simple, java based, rpc framework implement by netty, zookeeper ,spring
## Getting started
### Define an interface:

```java
package com.frameworkrpc.demo.api;

public interface DemoService {

	String sayHello(String name);
}
```

### Implement service interface for the provider
```java
package com.frameworkrpc.demo.provider;

import com.frameworkrpc.demo.api.DemoService;
import com.frameworkrpc.rpc.RpcContext;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DemoServiceImpl implements DemoService {
	@Override
	public String sayHello(String name) {
		return "Hello " + name + ", response from provider: " + RpcContext.getContext().getRequesterId();
	}
}
```
### Start service provider
```java
package com.frameworkrpc.demo.provider;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.ProtocolConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.config.ServiceConfig;
import com.frameworkrpc.demo.api.DemoService;

public class Provider {
	public static void main(String[] args) throws Exception {
	
		DemoService demoService = new DemoServiceImpl();

		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx");

		RegistryConfig registry = new RegistryConfig();
		registry.setName("zookeeper");
		registry.setAddress("127.0.0.1:2181");

		ServiceConfig<DemoService> service = new ServiceConfig<DemoService>();
		service.setApplication(application);
		service.setRegistry(registry);
		service.setInterface(DemoService.class);
		service.setRef(demoService);
		service.setVersion("1.0.0");

		service.export();

		System.in.read();
	}
}
```

### Call remote service in consumer
```java
package com.frameworkrpc.demo.consumer;

import com.frameworkrpc.config.ApplicationConfig;
import com.frameworkrpc.config.ProtocolConfig;
import com.frameworkrpc.config.ReferenceConfig;
import com.frameworkrpc.config.RegistryConfig;
import com.frameworkrpc.demo.api.DemoService;

public class Consumer {
	public static void main(String[] args) throws Exception {
	
		ApplicationConfig application = new ApplicationConfig();
		application.setName("xxx");

		RegistryConfig registry = new RegistryConfig();
		registry.setName("zookeeper");
		registry.setAddress("127.0.0.1:2181");
	
		ReferenceConfig<DemoService> reference = new ReferenceConfig<DemoService>();
		reference.setApplication(application);
		reference.setRegistry(registry);
		reference.setInterface(DemoService.class);
		reference.setVersion("1.0.0");

		DemoService demoService = reference.get();
		String hello = demoService.sayHello("world"); 
		System.out.println(hello); // get result
	}
}
```
