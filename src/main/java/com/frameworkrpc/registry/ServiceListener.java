package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public interface ServiceListener {

	void handleNodeChildChange(String parentPath, List<String> currentChilds, ConcurrentHashMap<String, List<URL>> availableServiceUrls);

	void handleNodeDataDeleted(String parentPath, String currentNode, ConcurrentHashMap<String, List<URL>> availableServiceUrls);
}
