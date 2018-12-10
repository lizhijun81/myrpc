package com.frameworkrpc.registry.zookeeper;

import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.model.URL;

public class ZkUtils {

	public static String toGroupPath(URL url) {
		return String.format("%s%s%s", RpcConstants.ZK_REGISTRY_PATH, RpcConstants.PATH_SEPARATOR, url.getParameter(RpcConstants.GROUP));
	}

	public static String toServicePath(URL url) {
		return String.format("%s%s%s", toGroupPath(url), RpcConstants.PATH_SEPARATOR, url.getPath());
	}

	public static String[] toCategoriesPath(URL url) {
		String[] categories;
		if (RpcConstants.ANY_VALUE.equals(url.getParameter(RpcConstants.CATEGORY_KEY))) {
			categories = new String[]{RpcConstants.PROVIDERS_CATEGORY, RpcConstants.CONSUMERS_CATEGORY,};
		} else {
			categories = url.getParameter(RpcConstants.CATEGORY_KEY, new String[]{RpcConstants.DEFAULT_CATEGORY});
		}
		String[] paths = new String[categories.length];
		for (int i = 0; i < categories.length; i++) {
			paths[i] = toServicePath(url) + RpcConstants.PATH_SEPARATOR + categories[i];
		}
		return paths;
	}

	public static String toCategoryPath(URL url) {
		return toServicePath(url) + RpcConstants.PATH_SEPARATOR + url.getParameter(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
	}

	public static String toNodePath(URL url) {
		return String.format("%s%s%s", toCategoryPath(url), RpcConstants.PATH_SEPARATOR, URL.encode(url.toFullStr()));
	}
}
