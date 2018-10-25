package com.sherlock.imapp.view;

import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.entity.ImMessage;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface ConversationView {
    public void getServerImMessageSuccess(List<ImMessage> list);
    public void sendTextMsgSuccess(ImMessage msg);
    public void sendTextMsgfailure(ImMessage msg);
    public void showImMessage(ImMessage msg);
    public void getUserVOMapSuccess (Map<Integer,UserVO> userVOMap);
}
