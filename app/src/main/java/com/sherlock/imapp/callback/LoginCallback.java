package com.sherlock.imapp.callback;

import com.sherlock.imapp.entity.UserVO;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public interface LoginCallback {
    public void loginSuccess(UserVO user);
}
