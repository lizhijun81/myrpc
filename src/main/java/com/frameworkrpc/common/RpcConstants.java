package com.frameworkrpc.common;

import java.util.regex.Pattern;

public class RpcConstants {

	public final static String ZK_REGISTRY_PATH = "/myrpc";

	public final static String PATH_SEPARATOR = "/";
	public static final Pattern COMMA_SPLIT_PATTERN = Pattern.compile("\\s*[,]+\\s*");
	public static final String ANY_VALUE = "*";

	public final static String CHARSET = "utf-8";

	public final static String APPLICATION_KEY = "application";
	public final static String APPLICATION_VERSION_KEY = "application.version";
	public final static String VERSION_KEY = "version";
	public final static String GROUP_KEY = "group";
	public final static String INTERFACE_KEY = "interface";
	public final static String LOADBALANCE_KEY = "loadbalance";
	public final static String CLUSTER_KEY = "cluster";
	public final static String TIMEOUT_KEY = "timeout";
	public final static String CONNECTTIMEOUT_KEY = "connecttimeout";
	public final static String REGISTRY_TIMEOUT_KEY = "registry.timeout";
	public final static String SESSIONTIMEOUT_KEY = "sessiontimeout";
	public final static String REGISTRY_NAME_KEY = "registry.name";
	public final static String REGISTRY_SESSIONTIMEOUT_KEY = "registry.sessiontimeout";
	public final static String ADDRESS_KEY = "address";
	public final static String RETRIES_KEY = "retries";
	public static final String SIDE_KEY = "side";
	public static final String CATEGORY_KEY = "category";

	public static final String PROTOCOL_KEY = "protocol";
	public final static String SERIALIZATION_KEY = "serialization";
	public final static String HOST_KEY = "host";
	public final static String PORT_KEY = "port";
	public static final String TRANSPORTER_KEY = "transporter";
	public final static String IOTHREADS_KEY = "iothreads";
	public final static String THREADPOOL_KEY = "threadpool";
	public final static String THREADS_KEY = "threads";
	public final static String HEARTBEAT_KEY = "heartbeat";

	public final static String PROVIDER = "provider";
	public final static String CONSUMER = "consumer";
	;

	public final static String DEFAULT_VERSION = "0.0.1";
	public final static String DEFAULT_GROUP = "default";
	public final static String DEFAULT_CLUSTER = "failover";
	public final static String DEFAULT_LOADBALANCE = "roundrobin";
	public final static String DEFAULT_SERIALIZATION = "hessian";
	public final static int DEFAULT_TMEOUT = 5000;
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

	public static final String PROVIDERS_CATEGORY = "providers";
	public static final String CONSUMERS_CATEGORY = "consumers";
	public static final String DEFAULT_CATEGORY = PROVIDERS_CATEGORY;

	public static final String PROVIDER_SIDE = "provider";
	public static final String CONSUMER_SIDE = "consumer";
}
