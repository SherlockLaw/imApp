package com.sherlock.imapp.manager;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.UserInfoCallback;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.entity.UserVO;

import java.util.HashMap;
import java.util.Map;

public class UserInfoManager {
    private UserInfoCallback callback;
    public UserInfoManager(UserInfoCallback callback){
        this.callback = callback;
    }

    public void deleteFriend(final UserVO userVO){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Map<String,Object> params = new HashMap<>();
                params.put("fromUserId", MyApplication.getUserId());
                params.put("toUserId", userVO.getId());
                String result = HttpProxy.post(UrlConstant.friend_delFriend, params,null);
                callback.deleteFriendSuccess(userVO);
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
