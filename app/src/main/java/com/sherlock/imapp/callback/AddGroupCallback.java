package com.sherlock.imapp.callback;

import com.sherlock.imapp.entity.UserVO;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public interface AddGroupCallback {
    public void getFriendListSuccess(List<UserVO> list);
    public void addGroupSuccess();
}
