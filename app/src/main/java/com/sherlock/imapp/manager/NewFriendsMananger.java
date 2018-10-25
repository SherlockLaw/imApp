package com.sherlock.imapp.manager;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.NewFriendsCallback;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.FriendReqVO;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewFriendsMananger {
    private NewFriendsCallback callback;
    public NewFriendsMananger(NewFriendsCallback callback){
        this.callback = callback;
    }

    public void getList(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Map<String,Object> params = new HashMap<>();
                params.put("userId", MyApplication.getUserId());
                String result = HttpProxy.get(UrlConstant.friend_getFriendRequests,params,null);
                List<FriendReqVO> list = JSONObject.parseArray(result, FriendReqVO.class);
                callback.getListSuccess(list);
            }
        };
        ThreadPoolService.execute(runnable);
    }
    public void acceptRequest(final int id, final MessageConstant.AddFriendConfirmStatusEnum status) {
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Map<String,Object> params = new HashMap<>();
                params.put("id", id);
                params.put("status", status.getIndex());
                String result = HttpProxy.post(UrlConstant.friend_confirmFriendRequest,params,null);
                callback.acceptRequestSuccess(id,status);
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
