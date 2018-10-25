package com.sherlock.imapp.callback;

import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.FriendReqVO;

import java.util.List;

public interface NewFriendsCallback {
    public void getListSuccess(List<FriendReqVO> list);
    public void acceptRequestSuccess(int id, MessageConstant.AddFriendConfirmStatusEnum status);
}
