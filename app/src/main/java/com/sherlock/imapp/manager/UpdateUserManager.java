package com.sherlock.imapp.manager;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.UpdateUserCallback;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.db.AccountDBService;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.utils.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class UpdateUserManager {
    private UpdateUserCallback callback;
    public UpdateUserManager(UpdateUserCallback callback) {
        this.callback = callback;
    }

    public void update(final String name,final int sex, final File headPic){
        Runnable runnable = new Runnable() {
            public void run(){
                Map<String,Object> params = new HashMap<>();
                params.put("id", MyApplication.getUserId());
                params.put("name",name);
                params.put("sex",sex);
                if (headPic!=null) {
                    params.put("headPic", headPic);
                }
                String result = HttpProxy.post(UrlConstant.user_update, params,null);
                UserVO user = JSONObject.parseObject(result,UserVO.class);

                MyApplication.updateUser(user);
                callback.updateSuccess();
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
