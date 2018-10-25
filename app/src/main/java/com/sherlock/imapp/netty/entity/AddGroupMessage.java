package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;

public class AddGroupMessage extends OrderMessage{

	private Integer groupId;
	
	public AddGroupMessage(){
		this.msgType = Configure.MSG_ADDGROUP;
	}
	
	public Integer getGroupId() {
		return groupId;
	}

	public void setGroupId(Integer groupId) {
		this.groupId = groupId;
	}
	
}
