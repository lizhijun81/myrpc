package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.model.URL;

public class ZKUtils {

	public static String toGroupPath(URL url) {
		return String.format("%s%s%s", RpcConstant.ZK_REGISTRY_PATH, RpcConstant.PATH_SEPARATOR, url.getParameter(RpcConstant.GROUP));
	}

	public static String toServicePath(URL url) {
		String path = url.getPath();
		return String.format("%s%s%s", toGroupPath(url), RpcConstant.PATH_SEPARATOR, path.substring(1, path.length()));
	}

	public static String toNodeTypePath(URL url, ZkNodeType nodeType) {
		return String.format("%s%s%s", toServicePath(url), RpcConstant.PATH_SEPARATOR, nodeType.getValue());
	}

	public static String toNodePath(URL url, ZkNodeType nodeType) {
		return String.format("%s%s%s", toNodeTypePath(url, nodeType), RpcConstant.PATH_SEPARATOR, url.getServerPortStr());
	}
}
