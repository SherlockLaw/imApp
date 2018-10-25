package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.ConversationCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.manager.ConversationManager;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.view.ConversationView;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class ConversationPresenter implements ConversationCallback {
    private ConversationView view;
    private ConversationManager manager;

    public ConversationPresenter(ConversationView view) {
        this.view = view;
        this.manager = new ConversationManager(this);
    }

    public void getUserVOMap (Conversation conversation){
        manager.getUserVOMap(conversation);
    }
    @Override
    public void getUserVOMapSuccess (Map<Integer,UserVO> userVOMap){
        view.getUserVOMapSuccess(userVOMap);
    }
    public void sendTextMsg(int gtype,int gid,String msg){
        manager.sendTextMsg(gtype,gid,msg);
    }

    @Override
    public void sendTextMsgResult(ImMessage msg) {
        DBService.saveServerImMessage(msg);
        DBService.upsertConversation(msg.getGtype(),msg.getGid(),0,msg.getLastMsg(),msg.getTime());
        view.sendTextMsgSuccess(msg);
    }

    public void getServerImMessage(int gtype,int gid){
        manager.getServerImMessage(gtype,gid);
    }

    @Override
    public void getServerImMessageSuccess(List<ImMessage> list) {
        view.getServerImMessageSuccess(list);
    }

    public void sendImgMsg(final int gtype, final int gid,final String path){
        manager.sendImgMsg(gtype, gid, path);
    }
}
