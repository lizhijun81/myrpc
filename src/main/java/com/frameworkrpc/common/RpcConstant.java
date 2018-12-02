package com.frameworkrpc.common;

public class RpcConstant {

	public static int DEFAULT_NIO_THREAD_COUNT;
	public static int DEFAULT_NIO_PORT = 2180;

	static {
		DEFAULT_NIO_THREAD_COUNT = Runtime.getRuntime().availableProcessors() > 1 ? 2 * (Runtime.getRuntime().availableProcessors() - 1) : 2;
	}

	public final static String ZK_REGISTRY_PATH = "/myrpc";
	public final static String PATH_SEPARATOR = "/";

	public final static String CHARSET = "utf-8";

	public final static String APPLICATION = "application";
	public final static String APPLICATION_VERSION = "application.version";
	public final static String VERSION = "version";
	public final static String GROUP = "group";
	public final static String INTERFACE = "interface";
	public final static String LOADBALANCE = "loadbalance";
	public final static String CLUSTER = "cluster";
	public final static String TIMEOUT = "timeout";
	public final static String REGISTRY_TIMEOUT = "registry.timeout";
	public final static String SESSIONTIMEOUT = "sessiontimeout";
	public final static String REGISTRY_SESSIONTIMEOUT = "registry.sessiontimeout";
	public final static String REGISTRYNAME = "registryname";
	public final static String ADDRESS = "address";
	public final static String RETRIES = "retries";


	public static final String PROTOCOL = "protocol";
	public final static String SERIALIZATION = "serialization";
	public final static String HOST = "host";
	public final static String PORT = "port";
	public static final String TRANSPORTER = "transporter";

	public final static String PROVIDERSCHEME = "provider";
	public final static String CONSUMERSCHEME = "consumer";
	public final static String APPLICATIONSCHEME = "application";
	public final static String PROTOCOLSCHEME = "protocol";
	public final static String REGISTRYSCHEME = "registry";

	public final static String DEFAULT_VERSION = "0.0.1";
	public final static String DEFAULT_GROUP = "defaultgroup";
	public final static String DEFAULT_CLUSTER = "failover";
	public final static String DEFAULT_SERIALIZATION = "hessian";
	public final static int DEFAULT_TMEOUT = 1000;
	public final static int DEFAULT_RRETRIES = 2;
	public final static String DEFAULT_LOADBALANCE = "random";

	public final static String DEFAULT_PROTOCOL = "myrpcprotocol";
	public final static String DEFAULT_TRANSPORTER = "netty";

	public final static int DEFAULT_REGISTRY_TIMEOUT = 5000;
	public final static int DEFAULT_REGISTRY_SESSIONTIMEOUT = 60000;
}
