package com.frameworkrpc.registry;

import com.frameworkrpc.model.URL;

import java.util.List;
import java.util.Set;

public interface Registry {

	void register(URL url, RegistrySide registrySide);

	void unregister(URL url, RegistrySide registrySide);

	void subscribe(URL url, RegistrySide registrySide);

	void unsubscribe(URL url, RegistrySide registrySide);

	List<URL> discover(URL url, RegistrySide registrySide);

	Set<URL> getRegisteredServiceUrls();

}