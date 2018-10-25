package com.sherlock.imapp.manager;

import com.sherlock.imapp.callback.GroupListCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.entity.Group;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class GroupListManager {
    private GroupListCallback callback;

    public GroupListManager(GroupListCallback callback) {
        this.callback = callback;
    }

    public void getGroupList() {
        Runnable runnable = new Runnable() {
            public void run(){
                List<Group> list = DBService.getGroupList();
                callback.getGroupListSuccess(list);
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
