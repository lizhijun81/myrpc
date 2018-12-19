package com.myrpc.serialize.hessian;

import com.caucho.hessian.io.HessianInput;
import com.caucho.hessian.io.HessianOutput;
import com.myrpc.extension.RpcComponent;
import com.myrpc.exception.MyRpcSerializeException;
import com.myrpc.serialize.Serialize;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

@RpcComponent(name = "hessian")
public class HessianSerialize implements Serialize {

	@Override
	public <T> byte[] serialize(T object) {
		ByteArrayOutputStream os = new ByteArrayOutputStream();
		HessianOutput ho = new HessianOutput(os);
		try {
			ho.writeObject(object);
		} catch (IOException e) {
			throw new MyRpcSerializeException(e.getMessage(), e);
		}
		return os.toByteArray();
	}

	@Override
	public <T> T deserialize(byte[] data, Class<T>... cls) {
		ByteArrayInputStream is = new ByteArrayInputStream(data);
		HessianInput hi = new HessianInput(is);
		try {
			return (T) hi.readObject();
		} catch (IOException e) {
			throw new MyRpcSerializeException(e.getMessage(), e);
		}
	}
}
