package com.sherlock.imapp.callback;

import com.sherlock.imapp.entity.UserVO;

import java.util.List;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public interface FriendListCallback {
    public void getFriendListSuccess(List<UserVO> userVOList);

    public void getNewFriendCountSuccess(int count);
}
