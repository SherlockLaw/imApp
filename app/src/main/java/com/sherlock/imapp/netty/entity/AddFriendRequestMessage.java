package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;

public class AddFriendRequestMessage extends OrderMessage{
	private Integer fromUserId;
	private String name;
	private Integer sex;
	private String headPic;
	public AddFriendRequestMessage(){
		this.msgType = Configure.MSG_ADDFRIEND_REQUEST;
	}
	public Integer getFromUserId() {
		return fromUserId;
	}
	public void setFromUserId(Integer fromUserId) {
		this.fromUserId = fromUserId;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Integer getSex() {
		return sex;
	}
	public void setSex(Integer sex) {
		this.sex = sex;
	}
	public String getHeadPic() {
		return headPic;
	}
	public void setHeadPic(String headPic) {
		this.headPic = headPic;
	}
}
