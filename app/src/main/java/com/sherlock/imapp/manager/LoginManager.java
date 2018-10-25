package com.sherlock.imapp.manager;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.callback.LoginCallback;
import com.sherlock.imapp.db.AccountDBService;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class LoginManager {

    private LoginCallback callback;

    public LoginManager(LoginCallback callback) {
        this.callback = callback;
    }

    public void login(String account, String pwd) {
        Runnable runnable = new Runnable() {
            private String account;
            private String pwd;
            public Runnable init(String account, String pwd){
                this.account = account;
                this.pwd = pwd;
                return this;
            }
            public void run(){
                if (StringUtil.isBlank(account)) {
                    throw new ServiceException("账号不能为空");
                }
                if (StringUtil.isBlank(pwd)) {
                    throw new ServiceException("密码不能为空");
                }
                Map<String,Object> params = new HashMap<>();
                params.put("account",account);
                params.put("pwd",pwd);
                String result = HttpProxy.post(UrlConstant.user_login,params,null);
                UserVO user = JSONObject.parseObject(result,UserVO.class);
                AccountDBService.upsertAccount(user.getId(),user.getAccount(),pwd,user.getHeadPic());
                callback.loginSuccess(user);
            }
        }.init(account, pwd);
        ThreadPoolService.execute(runnable);
    }
}
