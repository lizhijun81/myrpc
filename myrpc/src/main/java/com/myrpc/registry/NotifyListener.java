package com.myrpc.registry;

import com.myrpc.model.URL;

import java.util.List;

public interface NotifyListener {

	void notify(List<URL> urls);

}
