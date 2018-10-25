package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;

public class ClientHeartMessage extends AbstractMessage{
	public ClientHeartMessage(){
		this.msgType = Configure.MSG_CLIENT_HEARTBEAT;
	}
}
