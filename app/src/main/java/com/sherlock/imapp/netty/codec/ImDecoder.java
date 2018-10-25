package com.sherlock.imapp.netty.codec;

import java.util.List;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.Configure;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.netty.entity.AbstractMessage;
import com.sherlock.imapp.netty.entity.AddFriendRequestMessage;
import com.sherlock.imapp.netty.entity.AddGroupMessage;
import com.sherlock.imapp.netty.entity.AuthBackMessage;

import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

public class ImDecoder extends ByteToMessageDecoder{
//	private static Log log = LogFactory.getLog(ImDecoder.class);
	
	@Override
	protected void decode(ChannelHandlerContext ctx, ByteBuf in, List<Object> out) throws Exception {
//		if (!Configure.IS_BIG_EDIAN) {
//			in.order(ByteOrder.LITTLE_ENDIAN);
//		}
		if (in.readableBytes() < Configure.HEAD_LENGTH) {
			return;
		}
		in.markReaderIndex();
		//报文头部
		int startFlag = in.readInt();//开始标志
		if (Configure.START_FLAG!=startFlag) {
			throw new RuntimeException("消息开始标志不正确");
		}
		byte version = in.readByte();//消息版本号
		if (Configure.MSG_VER != version) {
			throw new RuntimeException("消息版本号不正确");
		}
		byte msgType = in.readByte();//消息类型
		int msgLen = in.readInt();
		//分包处理
		if (in.writerIndex()<msgLen) {
			in.resetReaderIndex();
			return;
		}
		String str = in.readCharSequence(msgLen-Configure.HEAD_LENGTH, Configure.CHARSET).toString();
		AbstractMessage msg = MessageConstant.getMessage(msgType, str);
		if (msg != null) {
			out.add(msg);
		}
	}
}
