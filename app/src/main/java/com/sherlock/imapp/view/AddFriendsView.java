package com.sherlock.imapp.view;

import com.sherlock.imapp.entity.UserVO;

import java.util.List;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public interface AddFriendsView {
    public void searchUsersSuccess(List<UserVO> list);

    public void addFriendsSuccess();
}
