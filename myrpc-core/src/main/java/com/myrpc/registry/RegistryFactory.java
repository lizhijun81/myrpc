package com.myrpc.registry;

import com.myrpc.config.URL;

public interface RegistryFactory {

	Registry getRegistry(URL url);
}
