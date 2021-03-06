package com.sherlock.imapp.manager;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.callback.RegisterCallback;
import com.sherlock.imapp.db.AccountDBService;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.utils.StringUtil;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class RegisterManager {
    private RegisterCallback callback;
    public RegisterManager(RegisterCallback callback){
        this.callback = callback;
    }

    public void register(final String account, final String pwd, final String name,final int sex, final File headPic) {
        Runnable runnable = new Runnable() {
            public void run(){
                if (StringUtil.isBlank(account)) {
                    throw new ServiceException("账号不能为空");
                }
                if (StringUtil.isBlank(pwd)) {
                    throw new ServiceException("密码不能为空");
                }
                if (StringUtil.isBlank(name)) {
                    throw new ServiceException("名称不能为空");
                }
                Map<String,Object> params = new HashMap<>();
                params.put("account",account);
                params.put("pwd",pwd);
                params.put("name",name);
                params.put("sex",sex);
                params.put("headPic", headPic);
                String result = HttpProxy.post(UrlConstant.user_register, params,null);
                UserVO user = JSONObject.parseObject(result,UserVO.class);
                AccountDBService.upsertAccount(user.getId(),user.getAccount(),pwd,user.getHeadPic());
                callback.registerSuccess(user);
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
