package com.myrpc.rpc;

public enum InvokeType {

	SYNC, ASYNC;

	public static InvokeType parse(String name) {
		for (InvokeType s : values()) {
			if (s.name().equalsIgnoreCase(name)) {
				return s;
			}
		}
		return null;
	}

	public static InvokeType getDefault() {
		return SYNC;
	}
}
