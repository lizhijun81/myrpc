package com.frameworkrpc.extension;

import com.frameworkrpc.common.MyClassUtils;
import com.frameworkrpc.exception.RpcException;

import java.lang.annotation.Annotation;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

public class ExtensionLoader<T> {

	private static ConcurrentMap<Class<?>, ExtensionLoader<?>> extensionLoaders = new ConcurrentHashMap<Class<?>, ExtensionLoader<?>>();
	private ConcurrentMap<String, T> singletonInstances = new ConcurrentHashMap<>();
	protected ConcurrentMap<String, Class<T>> extensionClasses = new ConcurrentHashMap<>();

	private ClassLoader classLoader;
	private Class<T> type;

	private ExtensionLoader(Class<T> type) {
		this(type, Thread.currentThread().getContextClassLoader());
	}

	private ExtensionLoader(Class<T> type, ClassLoader classLoader) {
		this.type = type;
		this.classLoader = classLoader;
	}

	public T getExtension(String name, Scope scope) {
		if (extensionClasses.isEmpty()) {
			initExtensionClasss();
		}

		if (name == null) {
			return null;
		}
		try {
			if (scope == Scope.SINGLETON) {
				return getSingletonInstance(name);
			} else {
				Class<T> clz = extensionClasses.get(name);

				if (clz == null) {
					return null;
				}

				return clz.newInstance();
			}
		} catch (Exception e) {
			throw new RpcException("Error when getExtension " + name, e);
		}
	}


	private T getSingletonInstance(String name) throws InstantiationException, IllegalAccessException {
		T obj = singletonInstances.get(name);

		if (obj != null) {
			return obj;
		}

		Class<T> clz = extensionClasses.get(name);

		if (clz == null) {
			return null;
		}

		synchronized (singletonInstances) {
			obj = singletonInstances.get(name);
			if (obj != null) {
				return obj;
			}

			obj = clz.newInstance();
			singletonInstances.put(name, obj);
		}

		return obj;
	}

	public T getExtension(String name) {
		return getExtension(name, Scope.PROTOTYPE);
	}

	private synchronized void initExtensionClasss() {
		if (!extensionClasses.isEmpty()) {
			return;
		}
		try {
			String packageName = this.type.getPackage().getName();
			List<Class<?>> clazzs = MyClassUtils.getClassList(packageName, true);
			for (Class<?> clazz : clazzs) {
				if (type.isAssignableFrom(clazz) && clazz != type) {
					Annotation[] annotations = clazz.getDeclaredAnnotations();
					if (annotations == null || annotations.length == 0) {
						continue;
					}
					for (Annotation annotation : annotations) {
						if (annotation instanceof RpcComponent) {
							extensionClasses.put(((RpcComponent) annotation).name(), (Class<T>) clazz);
						}
					}

				}
			}
		} catch (ClassNotFoundException e) {
			throw new RpcException(e.getMessage(), e);
		}
	}

	public static <T> ExtensionLoader<T> getExtensionLoader(Class<T> type) {
		checkInterfaceType(type);

		ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);

		if (loader == null) {
			loader = initExtensionLoader(type);
		}
		return loader;
	}

	public static synchronized <T> ExtensionLoader<T> initExtensionLoader(Class<T> type) {
		ExtensionLoader<T> loader = (ExtensionLoader<T>) extensionLoaders.get(type);

		if (loader == null) {
			loader = new ExtensionLoader<T>(type);

			extensionLoaders.putIfAbsent(type, loader);

			loader = (ExtensionLoader<T>) extensionLoaders.get(type);
		}

		return loader;
	}

	private static <T> void checkInterfaceType(Class<T> clz) {
		if (clz == null) {
			throw new RpcException("Error extension type is null");
		}

		if (!clz.isInterface()) {
			throw new RpcException("Error extension type is not interface" + clz.getName());
		}
	}

}
