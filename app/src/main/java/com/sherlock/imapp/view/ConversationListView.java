package com.sherlock.imapp.view;

import com.sherlock.imapp.entity.Conversation;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public interface ConversationListView {
    public void getConversationListSuccess(int totalUnreadCount, List<Conversation> list);
//    public void getUnreadCountFromServerAndSaveSuccess();
}
