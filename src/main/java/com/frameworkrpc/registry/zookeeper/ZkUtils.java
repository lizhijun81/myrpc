package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.common.RpcConstant;
import com.frameworkrpc.exception.RpcException;
import com.frameworkrpc.model.URL;
import com.frameworkrpc.registry.RegistrySide;

import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;

public class ZkUtils {

	public static String toGroupPath(URL url) {
		return String.format("%s%s%s", RpcConstant.ZK_REGISTRY_PATH, RpcConstant.PATH_SEPARATOR, url.getParameter(RpcConstant.GROUP));
	}

	public static String toServicePath(URL url) {
		return String.format("%s%s%s", toGroupPath(url), RpcConstant.PATH_SEPARATOR, url.getPath());
	}

	public static String toRegistryPath(URL url, RegistrySide nodeType) {
		return String.format("%s%s%s", toServicePath(url), RpcConstant.PATH_SEPARATOR, nodeType.getName());
	}

	public static String toNodePath(URL url, RegistrySide nodeType) {
		try {
			return String.format("%s%s%s", toRegistryPath(url, nodeType), RpcConstant.PATH_SEPARATOR,
					URLEncoder.encode(url.toFullStr(), RpcConstant.CHARSET));
		} catch (UnsupportedEncodingException e) {
			throw new RpcException(e.getMessage(), e);
		}
	}
}
