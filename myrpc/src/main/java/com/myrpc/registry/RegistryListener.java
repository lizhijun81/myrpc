package com.myrpc.registry;

import com.myrpc.common.NetUtils;
import com.myrpc.common.RpcConstants;
import com.myrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RegistryListener implements NotifyListener {

	private static final Logger logger = LoggerFactory.getLogger(RegistryListener.class);
	private List<URL> providerUrls;
	private URL url;

	public RegistryListener(URL url) {
		this.url = url;
	}

	public List<URL> getProviderUrls() {
		return providerUrls;
	}

	@Override
	public synchronized void notify(List<URL> urls) {
		providerUrls = new ArrayList<>();
		for (URL u : urls) {
			String category = u.getParameter(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
			if (RpcConstants.PROVIDERS_CATEGORY.equals(category) && u.getParameter(RpcConstants.VERSION_KEY)
					.equals(url.getParameter(RpcConstants.VERSION_KEY))) {
				providerUrls.add(u);
			} else {
				logger.warn(
						"Unsupported category " + category + " in notified url: " + url + " from registry " + url.getServerPortStr() + " to consumer "
								+ NetUtils.getLocalHost());
			}
		}
	}

}
