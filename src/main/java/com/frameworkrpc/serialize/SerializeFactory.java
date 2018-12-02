package com.frameworkrpc.serialize;

import com.frameworkrpc.serialize.Jdk.JdkSerialize;
import com.frameworkrpc.serialize.hessian.HessianSerialize;
import com.frameworkrpc.serialize.protostuff.ProtostuffSerialize;

public class SerializeFactory {

	public static Serialize createSerialize(String serializename) {
		SerializeEnum serializeEnum = SerializeEnum.getSerializeEnum(serializename);
		switch (serializeEnum) {
			case Hessian:
				return new HessianSerialize();
			case Jdk:
				return new JdkSerialize();
			case Protostuff:
				return new ProtostuffSerialize();
			default:
				return new HessianSerialize();
		}

	}
}
