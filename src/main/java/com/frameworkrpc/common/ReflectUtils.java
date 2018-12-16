package com.frameworkrpc.common;

import static com.frameworkrpc.common.Preconditions.checkNotNull;

public final class ReflectUtils {

	public static Object getTypeDefaultValue(Class<?> clazz) {
		checkNotNull(clazz, "clazz");

		if (clazz.isPrimitive()) {
			if (clazz == byte.class) {
				return (byte) 0;
			}
			if (clazz == short.class) {
				return (short) 0;
			}
			if (clazz == int.class) {
				return 0;
			}
			if (clazz == long.class) {
				return 0L;
			}
			if (clazz == float.class) {
				return 0F;
			}
			if (clazz == double.class) {
				return 0D;
			}
			if (clazz == char.class) {
				return (char) 0;
			}
			if (clazz == boolean.class) {
				return false;
			}
		}
		return null;
	}
}
