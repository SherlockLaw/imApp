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

    public void register(String account, String pwd, String name,int sex, File headPic) {
        Runnable runnable = new Runnable() {
            private String account;
            private String pwd;
            private String name;
            private int sex;
            private File headPic;
            public Runnable init(String account, String pwd,String name,int sex, File headPic){
                this.account = account;
                this.pwd = pwd;
                this.name = name;
                this.sex = sex;
                this.headPic = headPic;
                return this;
            }
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
        }.init(account, pwd,name,sex, headPic);
        ThreadPoolService.execute(runnable);
    }
}
