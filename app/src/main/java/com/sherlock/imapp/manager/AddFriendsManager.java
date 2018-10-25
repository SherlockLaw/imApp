package com.sherlock.imapp.manager;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.AddFriendsCallback;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.utils.StringUtil;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class AddFriendsManager {
    private AddFriendsCallback callback;

    public AddFriendsManager(AddFriendsCallback callback) {
        this.callback = callback;
    }

    public void searchUsers(String keyword) {
        Runnable runnable = new Runnable() {
            private String keyword;
            public Runnable init(String keyword){
                this.keyword = keyword;
                return this;
            }
            public void run(){
                if (StringUtil.isBlank(keyword)) {
                    throw new ServiceException("关键字不能为空");
                }
                Map<String,Object> params = new HashMap<>();
                params.put("keyword",keyword);
                String result = HttpProxy.get(UrlConstant.user_search,params,null);
                List<UserVO> contactList = JSONObject.parseArray(result,UserVO.class);
                callback.searchUsersSuccess(contactList);
            }
        }.init(keyword);
        ThreadPoolService.execute(runnable);
    }

    public void addFriends(UserVO toUser) {
        Runnable runnable = new Runnable() {
            private UserVO toUser;
            public Runnable init(UserVO toUser){
                this.toUser = toUser;
                return this;
            }
            public void run(){
                Map<String,Object> params = new HashMap<>();
                params.put("fromUserId", MyApplication.getUserId());
                params.put("toUserId",toUser.getId());
                String result = HttpProxy.post(UrlConstant.friend_addFriendRequest,params,null);
                callback.addFriendsSuccess(toUser);
            }
        }.init(toUser);
        ThreadPoolService.execute(runnable);
    }
}
