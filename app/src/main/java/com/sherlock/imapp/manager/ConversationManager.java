package com.sherlock.imapp.manager;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.ConversationCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.UploadVO;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.netty.entity.PicMessage;
import com.sherlock.imapp.netty.entity.TextMessage;
import com.sherlock.imapp.utils.ListUtil;
import com.sherlock.imapp.utils.StringUtil;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class ConversationManager {
    private ConversationCallback callback;
    public  ConversationManager(ConversationCallback callback){
        this.callback = callback;
    }

    public void getServerImMessage(final int gtype, final int gid) {
        Runnable runnable = new Runnable() {
            private int gtype;
            private int gid;
            public Runnable init(int gtype,int gid){
                this.gtype = gtype;
                this.gid = gid;
                return this;
            }
            @Override
            public void run() {
                //如果有未读消息，从服务器获取未读
                Conversation c = DBService.getConversation(gtype,gid);
                if (c !=null && c.getUnreadCount()>0) {
                    CommonManager.getConversationOffLineMsgAndSave(false);
                }
                //从数据库拉取一份
                List<ImMessage> list = DBService.getServerImMessage(gtype, gid);
                DBService.clearUnreadCount(gtype,gid);
                callback.getServerImMessageSuccess(list);
            }
        }.init(gtype,gid);
        ThreadPoolService.execute(runnable);
    }

    /**
     * 发送文本消息
     * @param gtype
     * @param gid
     * @param msg
     */
    public void sendTextMsg(int gtype, int gid,String msg) {
        Runnable runnable = new Runnable() {
            private int fromUserId;
            private int gtype;
            private int gid;
            private String content;
            public Runnable init(int fromUserId,int gtype,int gid,String content){
                this.fromUserId = fromUserId;
                this.gtype = gtype;
                this.gid = gid;
                this.content = content;
                return this;
            }
            @Override
            public void run() {
                if (StringUtil.isBlank(content)) {
                    throw new ServiceException("请输入内容");
                }
                int messageType = MessageConstant.MSGTYPE_TEXT;
                TextMessage msg = new TextMessage();
                msg.setFromUserId(fromUserId);
                msg.setGtype(gtype);
                msg.setGid(gid);
                msg.setMessageType(messageType);
                msg.setContent(content);
                msg.setTime(System.currentTimeMillis());
                Map<String,Object> params = new HashMap<>();
                params.put("fromUserId",fromUserId);
                params.put("gtype",gtype);
                params.put("gid",gid);
                params.put("messageType",messageType);
                params.put("content",content);
                String result = HttpProxy.post(UrlConstant.message_sendMessage,params,null);
                String mid = result;
                msg.setMid(mid);
                callback.sendTextMsgResult(msg);
            }
        }.init(MyApplication.getUserId(), gtype, gid,msg);
        ThreadPoolService.execute(runnable);
    }

    /**
     * 发送图片消息
     * @param gtype
     * @param gid
     * @param path
     */
    public void sendImgMsg(final int gtype, final int gid,final String path) {
        Runnable runnable = new Runnable() {
            private int fromUserId;
            private int gtype;
            private int gid;
            private String path;
            public Runnable init(int fromUserId,int gtype,int gid,String path){
                this.fromUserId = fromUserId;
                this.gtype = gtype;
                this.gid = gid;
                this.path = path;
                return this;
            }
            @Override
            public void run() {
                UploadVO uploadVO = uploadImage(path);
                int messageType = MessageConstant.MSGTYPE_PIC;
                PicMessage msg = new PicMessage();
                msg.setFromUserId(fromUserId);
                msg.setGtype(gtype);
                msg.setGid(gid);
                msg.setMessageType(messageType);
                msg.setUrl(uploadVO.getUrl());
                msg.setWidth(uploadVO.getWidth());
                msg.setHeigth(uploadVO.getHeigth());
                msg.setTime(System.currentTimeMillis());
                //发送消息
                Map<String,Object> params = new HashMap<>();
                params.put("fromUserId",fromUserId);
                params.put("gtype",gtype);
                params.put("gid",gid);
                params.put("messageType",messageType);
                params.put("imageUrl",uploadVO.getUrl());
                params.put("width",uploadVO.getWidth());
                params.put("height",uploadVO.getHeigth());
                String result = HttpProxy.post(UrlConstant.message_sendMessage,params,null);
                String mid = result;
                msg.setMid(mid);
                callback.sendTextMsgResult(msg);
            }
        }.init(MyApplication.getUserId(),gtype,gid,path);
        ThreadPoolService.execute(runnable);
    }

    private UploadVO uploadImage(String path){
        File image = new File(path);
        Map<String,Object> params = new HashMap<>();
        params.put("image",image);
        String result = HttpProxy.post(UrlConstant.file_uploadImage,params,null);
        UploadVO vo = JSONObject.parseObject(result, UploadVO.class);
        //根据原图获取缩略图的链接
//        String smallPicUrl = BitMapUtil.getSmallPicUrl(vo.getUrl());
//        vo.setUrl(smallPicUrl);
        return vo;
    }

    public void getUserVOMap (Conversation conversation) {
        Runnable runnable = new Runnable() {
            private int gtype;
            private int gid;
            public Runnable init(int gtype,int gid){
                this.gtype = gtype;
                this.gid = gid;
                return this;
            }
            @Override
            public void run() {
                List<UserVO> userVOList;
                if (MessageConstant.GTypeEnum.user.getIndex()==gtype) {
                    UserVO userVO = DBService.getUser(gid);
                    userVOList = new ArrayList<UserVO>();
                    userVOList.add(userVO);
                } else if (MessageConstant.GTypeEnum.group.getIndex()==gtype) {
                    userVOList = DBService.getGroupMember(gid);
                } else {
                    throw new ServiceException("会话组类型gtype不正确");
                }
                Map<Integer,UserVO> userVOMap = ListUtil.list2Map(userVOList,"getId",UserVO.class);
                callback.getUserVOMapSuccess(userVOMap);
            }
        }.init(conversation.getGtype(),conversation.getGid());
        ThreadPoolService.execute(runnable);
    }
}
