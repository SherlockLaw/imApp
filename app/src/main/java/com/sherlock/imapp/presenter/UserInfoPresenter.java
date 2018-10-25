package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.UserInfoCallback;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.manager.UserInfoManager;
import com.sherlock.imapp.view.UserInfoView;

public class UserInfoPresenter implements UserInfoCallback{
    private UserInfoManager manager;
    private UserInfoView view;
    public UserInfoPresenter(UserInfoView view){
        this.view = view;
        manager = new UserInfoManager(this);
    }
    public void deleteFriend(UserVO userVO){
        manager.deleteFriend(userVO);
    }

    @Override
    public void deleteFriendSuccess(UserVO userVO) {
        view.deleteFriendSuccess(userVO);
    }
}
