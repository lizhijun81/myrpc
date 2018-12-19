package com.myrpc.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class MessageEncoder extends MessageToByteEncoder<Object> {

	private Serialize serialize;

	private Class<?> genericClass;

	public MessageEncoder(Serialize serialize, Class<?> genericClass) {
		this.serialize = serialize;
		this.genericClass = genericClass;
	}

	@Override
	protected void encode(ChannelHandlerContext ctx, Object in, ByteBuf out) throws Exception {
		if (genericClass.isInstance(in)) {
			byte[] data = serialize.serialize(in);
			out.writeInt(data.length);
			out.writeBytes(data);
		}
	}
}
