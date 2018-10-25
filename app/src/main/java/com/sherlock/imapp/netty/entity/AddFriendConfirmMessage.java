package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;
import com.sherlock.imapp.entity.UserVO;

public class AddFriendConfirmMessage extends OrderMessage{
	private Integer addFriendReqId;
	private Integer status;
	private UserVO userInfo;
	public AddFriendConfirmMessage(){
		this.msgType = Configure.MSG_ADDFRIEND_CONFIRM;
	}
	public Integer getAddFriendReqId() {
		return addFriendReqId;
	}
	public void setAddFriendReqId(Integer addFriendReqId) {
		this.addFriendReqId = addFriendReqId;
	}
	public Integer getStatus() {
		return status;
	}
	public void setStatus(Integer status) {
		this.status = status;
	}

	public UserVO getUserInfo() {
		return userInfo;
	}

	public void setUserInfo(UserVO userInfo) {
		this.userInfo = userInfo;
	}
}
