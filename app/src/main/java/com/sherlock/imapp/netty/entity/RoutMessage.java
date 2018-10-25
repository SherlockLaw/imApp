package com.sherlock.imapp.netty.entity;

public abstract class RoutMessage extends AbstractMessage{
	private int routId;//消息路由目的

	public int getRoutId() {
		return routId;
	}

	public void setRoutId(int routId) {
		this.routId = routId;
	}
}
