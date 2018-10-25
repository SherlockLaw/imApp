package com.sherlock.imapp.view;

import com.sherlock.imapp.entity.UserVO;

import java.util.List;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public interface FriendListView {

    public void getFriendListSuccess(List<UserVO> list);

    public void getNewFriendCountSuccess(int count);
}
