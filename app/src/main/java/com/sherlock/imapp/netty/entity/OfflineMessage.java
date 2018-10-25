package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;

public abstract class OfflineMessage extends RoutMessage{
	protected long time;//发送时间
	public OfflineMessage(){
		this.msgType = Configure.MSG_OFFLINE;
	}

	public long getTime() {
		return time;
	}
	public void setTime(long time) {
		this.time = time;
	}
}
