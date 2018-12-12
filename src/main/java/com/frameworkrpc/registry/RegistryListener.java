package com.frameworkrpc.registry;

import com.frameworkrpc.common.NetUtils;
import com.frameworkrpc.common.RpcConstants;
import com.frameworkrpc.model.URL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;

public class RegistryListener implements NotifyListener {

	private static final Logger logger = LoggerFactory.getLogger(RegistryListener.class);
	private List<URL> invokerUrls;
	private URL consumerUrl;

	public RegistryListener(URL consumerUrl) {
		this.consumerUrl = consumerUrl;
	}

	public List<URL> getInvokerUrls() {
		return invokerUrls;
	}

	@Override
	public synchronized void notify(List<URL> urls) {
		invokerUrls = new ArrayList<>();
		for (URL url : urls) {
			String category = url.getParameter(RpcConstants.CATEGORY_KEY, RpcConstants.DEFAULT_CATEGORY);
			if (RpcConstants.PROVIDERS_CATEGORY.equals(category) && url.getParameter(RpcConstants.VERSION)
					.equals(consumerUrl.getParameter(RpcConstants.VERSION))) {
				invokerUrls.add(url);
			} else {
				logger.warn(
						"Unsupported category " + category + " in notified url: " + url + " from registry " + url.getServerPortStr() + " to consumer "
								+ NetUtils.getLocalHost());
			}
		}
	}

}
