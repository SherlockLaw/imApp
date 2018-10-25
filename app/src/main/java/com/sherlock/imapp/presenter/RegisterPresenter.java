package com.sherlock.imapp.presenter;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.RegisterCallback;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.manager.RegisterManager;
import com.sherlock.imapp.view.RegisterView;

import java.io.File;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class RegisterPresenter implements RegisterCallback {
    private RegisterManager manager;
    private RegisterView view;
    public RegisterPresenter(RegisterView view){
        this.view = view;
        manager = new RegisterManager(this);
    }
    public void register(String account, String pwd, String name, int sex, File headImage) {
        manager.register(account, pwd, name, sex,headImage);
    }
    @Override
    public void registerSuccess(UserVO user) {
        MyApplication.afterLoginSuccess(user);
        view.registerSuccess(user);
    }
}
