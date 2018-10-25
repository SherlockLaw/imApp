package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;

public class ClientAuthMessage extends AbstractMessage{
	private String token;

	public ClientAuthMessage(){
		this.msgType = Configure.MSG_AUTH;
	}
	public String getToken() {
		return token;
	}

	public void setToken(String token) {
		this.token = token;
	}
	
	
}
