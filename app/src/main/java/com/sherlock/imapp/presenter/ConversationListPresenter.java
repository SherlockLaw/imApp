package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.ConversationListCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.manager.ConversationListManager;
import com.sherlock.imapp.view.ConversationListView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ConversationListPresenter implements ConversationListCallback{
    private ConversationListManager manager;

    private ConversationListView view;

    public ConversationListPresenter(ConversationListView view) {
        this.view = view;
        this.manager = new ConversationListManager(this);
    }

    public void getConversationList(){
        manager.getConversationList();
    }
    public void showConversationList() {
        List<Conversation> list = DBService.getConversationList();
        int totalUnreadCount = countTotalUnreadCount(list);
        view.getConversationListSuccess(totalUnreadCount, list);
    }

    private int countTotalUnreadCount(List<Conversation> list){
        int totalUnreadCount = 0;
        for (Conversation c : list ) {
            totalUnreadCount += c.getUnreadCount();
        }
        return totalUnreadCount;
    }

    @Override
    public void getConversationListSuccess(List<Conversation> list) {
        int totalUnreadCount = countTotalUnreadCount(list);
        view.getConversationListSuccess(totalUnreadCount, list);
    }

}
