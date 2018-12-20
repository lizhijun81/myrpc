## myrpc
a simple, java based, rpc framework implemented by netty, zookeeper ,spring

## Getting started
### Define an interface

```java
public interface DemoService {

	String sayHello(String name);
}
```

### Implement service interface for the provider
```java
public class DemoServiceImpl implements DemoService {
	@Override
	public String sayHello(String name) {
		return "Hello " + name + ", response from provider: " + RpcContext.getContext().getRequesterId();
	}
}
```
### Start service provider
```java
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

		Server server = new DefaultServer().with(service).init();
		
		server.start();

		server.publish(service);

		System.in.read();
	}
}
```

### Call remote service in consumer
```java
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
		System.out.println(hello); 
	}
}
```

### Start service provider With Spring
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:myrpc="http://www.frameworkrpc.com/myrpc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.frameworkrpc.com/myrpc http://www.frameworkrpc.com/myrpc/myrpc.xsd">

    <context:property-placeholder location="classpath:rpc-config.properties"/>

    <myrpc:application name="demo-provider"/>

    <myrpc:registry name="${zookeeper.name}" address="${zookeeper.address}"/>

    <myrpc:reference id="demoService" interface="com.myrpc.demo.api.DemoService"/>
</beans>

```
```java
public class ProviderWithSpring {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"myrpc-demo-provider.xml"});
		context.start();
		System.in.read();
	}
}
```
 
### Call remote service in consumer With Spring
```xml
<?xml version="1.0" encoding="UTF-8"?>
<beans xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
       xmlns="http://www.springframework.org/schema/beans" xmlns:context="http://www.springframework.org/schema/context"
       xmlns:myrpc="http://www.frameworkrpc.com/myrpc"
       xsi:schemaLocation="http://www.springframework.org/schema/beans http://www.springframework.org/schema/beans/spring-beans-4.3.xsd http://www.springframework.org/schema/context http://www.springframework.org/schema/context/spring-context.xsd http://www.frameworkrpc.com/myrpc http://www.frameworkrpc.com/myrpc/myrpc.xsd">

    <context:property-placeholder location="classpath:rpc-config.properties"/>

    <myrpc:application name="demo-provider"/>

    <myrpc:registry name="${zookeeper.name}" address="${zookeeper.address}"/>

    <bean id="demoService" class="com.myrpc.demo.provider.DemoServiceImpl"/>

    <myrpc:service interface="com.myrpc.demo.api.DemoService" ref="demoService"/>

</beans>
```
```java
public class ConsumerWithSpring {
	public static void main(String[] args) throws Exception {
		ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext(new String[]{"myrpc-demo-consumer.xml"});
		context.start();
		DemoService demoService = (DemoService) context.getBean("demoService"); 
		String hello = demoService.sayHello("world"); 
		System.out.println(hello); 
	}
}
```
