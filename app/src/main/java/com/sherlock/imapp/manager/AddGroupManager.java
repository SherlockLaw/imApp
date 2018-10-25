package com.sherlock.imapp.manager;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.AddGroupCallback;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.utils.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class AddGroupManager {
    private AddGroupCallback callback;

    public AddGroupManager(AddGroupCallback callback){
        this.callback = callback;
    }

    public void getFriendList() {
        Runnable runnable = new Runnable() {
            public void run(){
                List<UserVO> list = DBService.getFriendList();
                callback.getFriendListSuccess(list);
            }
        };
        ThreadPoolService.execute(runnable);
    }
    public void addGroup(File headPic, String name, List<Integer> userIds) {
        Runnable runnable = new Runnable() {
            private File headPic;
            private String name;
            private List<Integer> userIds;
            public Runnable init(File headPic, String name,List<Integer> userIds){
                this.headPic = headPic;
                this.name = name;
                this.userIds = userIds;
                return this;
            }
            public void run(){
                if (StringUtil.isBlank(name)) {
                    throw new ServiceException("名称不能为空");
                }
                if (userIds.isEmpty()) {
                    throw new ServiceException("成员不能为空");
                }
                Map<String,Object> params = new HashMap<>();
                params.put("name",name);
                params.put("headPic",headPic);
                params.put("creatorId",MyApplication.getUserId());
                String memberIds = HttpProxy.getParamStrByArray(userIds);
                params.put("memberIds", memberIds);
                String result = HttpProxy.post(UrlConstant.group_addGroup,params,null);
                callback.addGroupSuccess();
            }
        }.init(headPic, name,userIds);
        ThreadPoolService.execute(runnable);
    }
}
