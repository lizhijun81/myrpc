package com.frameworkrpc.common;

import java.util.regex.Pattern;

public class RpcConstants {

	public final static String ZK_REGISTRY_PATH = "/myrpc";

	public final static String PATH_SEPARATOR = "/";
	public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");

	public final static String CHARSET = "utf-8";

	public final static String APPLICATION = "application";
	public final static String APPLICATION_VERSION = "application.version";
	public final static String VERSION = "version";
	public final static String GROUP = "group";
	public final static String INTERFACE = "interface";
	public final static String LOADBALANCE = "loadbalance";
	public final static String CLUSTER = "cluster";
	public final static String TIMEOUT = "timeout";
	public final static String CONNECTTIMEOUT = "connecttimeout";
	public final static String REGISTRY_TIMEOUT = "registry.timeout";
	public final static String SESSIONTIMEOUT = "sessiontimeout";
	public final static String REGISTRY_NAME = "registry.name";
	public final static String REGISTRY_SESSIONTIMEOUT = "registry.sessiontimeout";
	public final static String ADDRESS = "address";
	public final static String RETRIES = "retries";

	public static final String PROTOCOL = "protocol";
	public final static String SERIALIZATION = "serialization";
	public final static String HOST = "host";
	public final static String PORT = "port";
	public static final String TRANSPORTER = "transporter";
	public final static String IOTHREADS = "iothreads";
	public final static String THREADPOOL = "threadpool";
	public final static String THREADS = "threads";
	public final static String HEARTBEAT = "heartbeat";

	public final static String PROVIDERSCHEME = "provider";
	public final static String CONSUMERSCHEME = "consumer";
	;

	public final static String DEFAULT_VERSION = "0.0.1";
	public final static String DEFAULT_GROUP = "default";
	public final static String DEFAULT_CLUSTER = "failover";
	public final static String DEFAULT_LOADBALANCE = "roundrobin";
	public final static String DEFAULT_SERIALIZATION = "hessian";
	public final static int DEFAULT_TMEOUT = 1000;
	public final static int DEFAULT_CONNECTTIMEOUT = 3000;
	public final static int DEFAULT_RRETRIES = 2;

	public final static String DEFAULT_PROTOCOL = "defaultprotocol";
	public final static String DEFAULT_TRANSPORTER = "netty";
	public final static int DEFAULT_IOTHREADS = Math.max(2, Runtime.getRuntime().availableProcessors()) * 2;
	public final static int DEFAULT_IOPORT = 2180;
	public final static String DEFAULT_THREADPOOL = "fixed";
	public final static int DEFAULT_THREADS = 200;
	public final static int DEFAULT_HEARTBEAT = 30;

	public final static int DEFAULT_REGISTRY_TIMEOUT = 5000;
	public final static int DEFAULT_REGISTRY_SESSIONTIMEOUT = 60000;

	public static final String CATEGORY_KEY = "category";
	public static final String ANY_VALUE = "*";

	public static final String PROVIDERS_CATEGORY = "providers";
	public static final String CONSUMERS_CATEGORY = "consumers";
	public static final String DEFAULT_CATEGORY = PROVIDERS_CATEGORY;
}
