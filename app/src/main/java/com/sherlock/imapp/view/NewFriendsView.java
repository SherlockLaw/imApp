package com.sherlock.imapp.view;

import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.FriendReqVO;

import java.util.List;

public interface NewFriendsView {
    public void getListSuccess(List<FriendReqVO> list);
    public void acceptRequestSuccess(int id, MessageConstant.AddFriendConfirmStatusEnum status);
}
