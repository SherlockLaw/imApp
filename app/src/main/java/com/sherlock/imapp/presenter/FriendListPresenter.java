package com.sherlock.imapp.presenter;

import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.callback.FriendListCallback;
import com.sherlock.imapp.manager.FriendListManager;
import com.sherlock.imapp.view.FriendListView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class FriendListPresenter implements FriendListCallback {
    private FriendListView view;
    private FriendListManager manager;

    public FriendListPresenter(FriendListView view) {
        manager = new FriendListManager(this);
        this.view = view;
    }

    public void getNewFriendCount() {
        manager.getNewFriendCount();
    }
    public void getFriendList() {
        manager.getFriendList();
    }

    @Override
    public void getFriendListSuccess(List<UserVO> list) {
        view.getFriendListSuccess(list);
    }

    @Override
    public void getNewFriendCountSuccess(int count) {
        view.getNewFriendCountSuccess(count);
    }

}
