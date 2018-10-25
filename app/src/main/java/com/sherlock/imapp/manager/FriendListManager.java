package com.sherlock.imapp.manager;

import com.sherlock.imapp.callback.FriendListCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.entity.UserVO;

import java.util.List;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class FriendListManager {
    private FriendListCallback callback;

    public FriendListManager(FriendListCallback callback) {
        this.callback = callback;
    }
    public void getNewFriendCount() {
        Runnable runnable = new Runnable() {
            public void run(){
                int count = DBService.getNewFriendsCount();
                callback.getNewFriendCountSuccess(count);
            }
        };
        ThreadPoolService.execute(runnable);
    }
    public void getFriendList() {
        Runnable runnable = new Runnable() {
            public void run(){
                List<UserVO> list = DBService.getFriendList();
                callback.getFriendListSuccess(list);
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
