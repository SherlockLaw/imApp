package com.sherlock.imapp.callback;

import com.sherlock.imapp.entity.UserVO;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public interface AddFriendsCallback {
    public void searchUsersSuccess(List<UserVO> userVOList);

    public void addFriendsSuccess(UserVO user);
}
