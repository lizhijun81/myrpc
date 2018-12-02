package com.frameworkrpc.serialize.Jdk;

import com.frameworkrpc.exception.SerializeException;
import com.frameworkrpc.serialize.Serialize;

import java.io.*;

public class JdkSerialize implements Serialize {

	@Override
	public <T> byte[] serialize(T object) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		try {
			ObjectOutputStream out = null;
			out = new ObjectOutputStream(os);
			out.writeObject(object);
		} catch (IOException e) {
			throw new SerializeException(e.getMessage(), e);
		}
		return os.toByteArray();
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T>... cls) {
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		try {
			ObjectInputStream in = new ObjectInputStream(is);
			return (T) in.readObject();
		} catch (ClassNotFoundException e) {
			throw new SerializeException(e.getMessage(), e);
		} catch (IOException e) {
			throw new SerializeException(e.getMessage(), e);
		}
	}
}
