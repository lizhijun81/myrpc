package com.myrpc.serialize;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

public class MessageDecoder extends ByteToMessageDecoder {

	private Serialize serialize;

	private Class<?> genericClass;

	public MessageDecoder(Serialize serialize, Class<?> genericClass) {
		this.serialize = serialize;
		this.genericClass = genericClass;
	}

	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
		if (in.readableBytes() < 4) {
			return;
		}

		in.markReaderIndex();
		int messageLength = in.readInt();

		if (messageLength < 0) {
			ctx.close();
		}

		if (in.readableBytes() < messageLength) {
			in.resetReaderIndex();
			return;
		}
		byte[] data = new byte[messageLength];
		in.readBytes(data);

		Object obj = serialize.deserialize(data, genericClass);
		out.add(obj);
	}
}
