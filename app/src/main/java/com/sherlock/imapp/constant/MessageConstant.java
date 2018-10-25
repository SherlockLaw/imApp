package com.sherlock.imapp.constant;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.Configure;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.netty.entity.ACKMessage;
import com.sherlock.imapp.netty.entity.AbstractMessage;
import com.sherlock.imapp.netty.entity.AddFriendConfirmMessage;
import com.sherlock.imapp.netty.entity.AddFriendRequestMessage;
import com.sherlock.imapp.netty.entity.AddGroupMessage;
import com.sherlock.imapp.netty.entity.AuthBackMessage;
import com.sherlock.imapp.netty.entity.ConversationOrderMessage;
import com.sherlock.imapp.netty.entity.DeleteFriendMessage;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.netty.entity.OfflineMessage;
import com.sherlock.imapp.netty.entity.PicMessage;
import com.sherlock.imapp.netty.entity.ReadMessage;
import com.sherlock.imapp.netty.entity.RetractMessage;
import com.sherlock.imapp.netty.entity.TextMessage;

public class MessageConstant {
	public static final int MSGTYPE_TEXT = 1;
	public static final int MSGTYPE_PIC = 2;
	public static final int MSGTYPE_RETRACT = 3;
	//指令类型
	public final static byte MSG_SERVER_ACK = 51;//服务端转发客户端确认消息
	public final static byte MSG_SERVER_READ = 52;//服务端转发客户端已读消息
	public enum GTypeEnum{	
		user(1,"用户"),
		group(2,"会话组");
		
		private int index;
		private String text;
		
		GTypeEnum(int sex, String text){
			this.index = sex;
			this.text = text;
		}
		
		public static GTypeEnum getEnum(int index) {
			GTypeEnum[] l = GTypeEnum.values();
			for (GTypeEnum e: l){
				if (e.index==index) {
					return e;
				}
			}
			return null;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}	
	}
	public enum AddFriendConfirmStatusEnum{
		unverified(1,"未验证"),
		agree(2,"同意"),
		reject(3,"拒绝");

		private int index;
		private String text;

		AddFriendConfirmStatusEnum(int sex, String text){
			this.index = sex;
			this.text = text;
		}

		public static AddFriendConfirmStatusEnum getEnum(int index) {
			AddFriendConfirmStatusEnum[] l = AddFriendConfirmStatusEnum.values();
			for (AddFriendConfirmStatusEnum e: l){
				if (e.index==index) {
					return e;
				}
			}
			return null;
		}

		public int getIndex() {
			return index;
		}

		public void setIndex(int index) {
			this.index = index;
		}
	}
	public static String getUserStr(int id){
		StringBuilder sb = new StringBuilder().append(GTypeEnum.user.getIndex()).append("_").append(id);
		return sb.toString();
	}
	public static String getGroupStr(int id){
		StringBuilder sb = new StringBuilder().append(GTypeEnum.group.getIndex()).append("_").append(id);
		return sb.toString();
	}
	public static String getStr(int gtype,int gid){
		StringBuilder sb = new StringBuilder().append(gtype).append("_").append(gid);
		return sb.toString();
	}

	/**
	 * 客户端接受服务端的消息需要使用这个
	 * @param msgType
	 * @param str
	 * @return
	 */
	public static AbstractMessage getMessage(byte msgType, String str) {
		AbstractMessage msg = null;
		switch (msgType) {
			case Configure.MSG_IM:
				msg = getImMessage(str);
				break;
			case Configure.MSG_CONVERSATION_ORDER:
				msg = getConversationOrderMessage(str);
				break;
			case Configure.MSG_AUTH_BACK:
				msg = JSONObject.parseObject(str, AuthBackMessage.class);
				break;
			case Configure.MSG_ADDFRIEND_REQUEST:
				msg = JSONObject.parseObject(str, AddFriendRequestMessage.class);
				break;
			case Configure.MSG_ADDFRIEND_CONFIRM:
				msg = JSONObject.parseObject(str, AddFriendConfirmMessage.class);
				break;
			case Configure.MSG_ADDGROUP:
				msg = JSONObject.parseObject(str, AddGroupMessage.class);
				break;
			case Configure.MSG_OFFLINE:
				msg = JSONObject.parseObject(str, OfflineMessage.class);
				break;
			case Configure.MSG_DELFRIEND:
				msg = JSONObject.parseObject(str, DeleteFriendMessage.class);
				break;
			default:
				throw  new ServiceException("不支持的消息类型");
		}
		return msg;
	}
	/**
	 * 生成Im消息对象
	 * @param str
	 * @return
	 */
	public static ImMessage getImMessage(String str) {
		JSONObject job = JSONObject.parseObject(str);
		int messageType = job.getIntValue("messageType");
		ImMessage msg = getServerImMessage0(str,messageType);
		return msg;
	}

	private static ImMessage getServerImMessage0(String str,int messageType){
		ImMessage msg = null;
		switch (messageType) {
			case MessageConstant.MSGTYPE_TEXT:
				msg = JSONObject.parseObject(str, TextMessage.class);
				break;
			case MessageConstant.MSGTYPE_PIC:
				msg = JSONObject.parseObject(str, PicMessage.class);
				break;
			case MessageConstant.MSGTYPE_RETRACT:
				msg = JSONObject.parseObject(str, RetractMessage.class);

		}
		return msg;
	}
	/**
	 * 生成指令消息对象
	 * @param str
	 * @return
	 */
	public static ConversationOrderMessage getConversationOrderMessage(String str) {

		JSONObject job = JSONObject.parseObject(str);
		int messageType = job.getIntValue("messageType");
		ConversationOrderMessage msg = getConversationOrderMessage0(str,messageType);
		return msg;
	}
	private static ConversationOrderMessage getConversationOrderMessage0(String str,int messageType){
		ConversationOrderMessage msg = null;
		switch (messageType) {
			case MessageConstant.MSG_SERVER_ACK:
				msg = JSONObject.parseObject(str, ACKMessage.class);
				break;
			case MessageConstant.MSG_SERVER_READ:
				msg = JSONObject.parseObject(str, ReadMessage.class);
				break;
		}
		return msg;
	}
}
