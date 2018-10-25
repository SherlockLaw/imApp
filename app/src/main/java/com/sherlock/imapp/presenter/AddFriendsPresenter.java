package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.AddFriendsCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.manager.AddFriendsManager;
import com.sherlock.imapp.view.AddFriendsView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class AddFriendsPresenter implements AddFriendsCallback {

    private AddFriendsManager manager;

    private AddFriendsView view;

    public AddFriendsPresenter(AddFriendsView view){
        this.view = view;
        this.manager = new AddFriendsManager(this);
    }
    public void addFriends(UserVO toUser) {
        manager.addFriends(toUser);
    }

    public void searchUsers(String keyword) {
        manager.searchUsers(keyword);
    }

    @Override
    public void searchUsersSuccess(List<UserVO> userVOList) {
        view.searchUsersSuccess(userVOList);
    }

    @Override
    public void addFriendsSuccess(UserVO user) {
        view.addFriendsSuccess();
    }
}
