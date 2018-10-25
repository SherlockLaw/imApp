package com.sherlock.imapp.netty.codec;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.Configure;
import com.sherlock.imapp.netty.entity.AbstractMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

public class ImEncoder extends MessageToByteEncoder<Object>{

	@Override
	protected void encode(ChannelHandlerContext ctx, Object msg, ByteBuf out) throws Exception {
		AbstractMessage message = (AbstractMessage) msg;
		//报文头部
		out.writeInt(Configure.START_FLAG);
		out.writeByte(Configure.MSG_VER);
		out.writeByte(message.getMsgType());
		out.writeInt(0);
		out.writeCharSequence(JSONObject.toJSONString(message), Configure.CHARSET);
		out.setInt(Configure.POSITION_LENGTH, out.readableBytes());
	}
}
