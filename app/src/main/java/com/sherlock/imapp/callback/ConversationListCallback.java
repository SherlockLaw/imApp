package com.sherlock.imapp.callback;

import com.sherlock.imapp.entity.Conversation;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public interface ConversationListCallback {
    public void getConversationListSuccess(List<Conversation> list);
}
