package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;



public interface RegistryFactory {

	Registry getRegistry(URL url);
}