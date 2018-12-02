package com.frameworkrpc.serialize;

public enum SerializeEnum {

	Hessian("hessian"), Jdk("jdk"), Protostuff("protostuff");

	private String serializename;

	public String getSerializename() {
		return serializename;
	}

	public void setSerializename(String serializename) {
		this.serializename = serializename;
	}


	SerializeEnum(String serializename) {
		this.serializename = serializename;
	}

	public static SerializeEnum getSerializeEnum(String serializename) {
		for (SerializeEnum serializeEnum : SerializeEnum.values()) {
			if (serializeEnum.getSerializename().equals(serializename)) {
				return serializeEnum;
			}
		}
		return null;
	}

}
