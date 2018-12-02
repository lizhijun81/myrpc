package com.frameworkrpc.registry;

import java.util.List;

@FunctionalInterface
public interface ServiceListener {

	void handleServiceChanges(String parentPath, List<String> currentChilds);
}
