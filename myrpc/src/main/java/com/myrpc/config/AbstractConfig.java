package com.myrpc.config;

import com.myrpc.common.StringUtils;

import java.io.Serializable;

public abstract class AbstractConfig implements Serializable {

	private static final long serialVersionUID = 4004312429333017685L;
	protected String id;

	public void setId(String id) {
		this.id = id;
	}

	public String getId() {
		return this.id;
	}

	protected String getVal(String val, String defaultVal) {
		return !StringUtils.isEmpty(val) ? val : defaultVal;
	}

	protected String getVal(int val, int defaultVal) {
		return val > 0 ? String.valueOf(val) : String.valueOf(defaultVal);
	}
}
