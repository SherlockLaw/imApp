package com.sherlock.imapp.manager;

import com.sherlock.imapp.callback.ConversationListCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.entity.Conversation;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ConversationListManager {
    private ConversationListCallback callback;
    public  ConversationListManager(ConversationListCallback callback){
        this.callback = callback;
    }

    public void getConversationList() {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                List<Conversation> list = DBService.getConversationList();
                callback.getConversationListSuccess(list);
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
