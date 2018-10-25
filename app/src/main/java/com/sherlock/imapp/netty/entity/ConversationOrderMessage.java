package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;

/**
 * 会话组离线指令
 * @author Administrator
 *
 */
public abstract class ConversationOrderMessage extends ConversationOfflineMessage{

	public ConversationOrderMessage(){
		this.msgType = Configure.MSG_CONVERSATION_ORDER;
	}	
}
