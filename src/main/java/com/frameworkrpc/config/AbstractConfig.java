package com.frameworkrpc.config;

import com.frameworkrpc.model.URL;

import java.io.Serializable;
import java.util.concurrent.locks.ReentrantLock;

public abstract class AbstractConfig implements Serializable {

	private static final long serialVersionUID = 4004312429333017685L;

	protected ReentrantLock reentrantLock = new ReentrantLock();

	protected volatile URL url;

	abstract URL getURL();
}
