package com.sherlock.imapp.manager;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.UpdatePwdCallback;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.utils.StringUtil;

import java.util.HashMap;
import java.util.Map;

public class UpdatePwdManager {
    private UpdatePwdCallback callback;
    public UpdatePwdManager(UpdatePwdCallback callback){
        this.callback = callback;
    }
    public void updatePwd(final String account, final String oldPwd, final String newPwd, final String retypeNewPwd){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                Map<String,Object> params = new HashMap<>();
                if (StringUtil.isBlank(account)||StringUtil.isBlank(oldPwd)||StringUtil.isBlank(newPwd)||StringUtil.isBlank(retypeNewPwd)) {
                    throw new ServiceException("请输入完整信息");
                }
                if (oldPwd.equals(newPwd)) {
                    throw new ServiceException("请输入与旧密码不同的密码");
                }
                if (!newPwd.equals(retypeNewPwd)) {
                    throw new ServiceException("两次输入的新密码不一致");
                }
                params.put("account", account);
                params.put("oldPwd", oldPwd);
                params.put("newPwd", newPwd);
                String result = HttpProxy.post(UrlConstant.user_updatePwd, params,null);
                callback.updatePwdSuccess();
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
