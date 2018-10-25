package com.sherlock.imapp.netty.entity;

import com.sherlock.imapp.Configure;

/***
 * 客户端需要显示的消息
 * @author Administrator
 *
 */
public abstract class ImMessage extends ConversationOfflineMessage implements Cloneable{
	
	public ImMessage(){
		this.msgType = Configure.MSG_IM;
	}
	
	@Override  
    public ImMessage clone() {  
		ImMessage stu = null;  
        try{  
            stu = (ImMessage)super.clone();  
        }catch(CloneNotSupportedException e) {  
            e.printStackTrace();  
        }  
        return stu;  
    }
	public String getLastMsg(){
		if (this instanceof TextMessage) {
			TextMessage msg = (TextMessage) this;
			return msg.getContent();
		}
		if (this instanceof PicMessage) {
			return "[图片]";
		}
		if (this instanceof RetractMessage) {
			RetractMessage msg = (RetractMessage) this;
			return "[消息撤回]";
		}
		return null;
	}
	
	

}
