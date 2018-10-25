package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.NewFriendsCallback;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.FriendReqVO;
import com.sherlock.imapp.manager.NewFriendsMananger;
import com.sherlock.imapp.view.NewFriendsView;

import java.util.List;

public class NewFriendsPresenter implements NewFriendsCallback {
    private NewFriendsMananger mananger;
    private NewFriendsView view;
    public NewFriendsPresenter(NewFriendsView view){
        this.view = view;
        mananger = new NewFriendsMananger(this);
    }
    public void getList(){
        mananger.getList();
    }

    @Override
    public void getListSuccess(List<FriendReqVO> list) {
        view.getListSuccess(list);
    }

    @Override
    public void acceptRequestSuccess(int id, MessageConstant.AddFriendConfirmStatusEnum status) {
        view.acceptRequestSuccess(id, status);
    }

    public void acceptRequest(final int id, final MessageConstant.AddFriendConfirmStatusEnum status) {
        mananger.acceptRequest(id, status);
    }
}
