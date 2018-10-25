package com.sherlock.imapp.presenter;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.callback.LoginCallback;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.manager.LoginManager;
import com.sherlock.imapp.view.LoginView;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class LoginPresenter implements LoginCallback {
    private LoginView view;
    private LoginManager manager;

    public LoginPresenter(LoginView view) {
        this.view = view;
        manager = new LoginManager(this);
    }

    public void login(String account, String pwd) {
        manager.login(account, pwd);
    }

    @Override
    public void loginSuccess(UserVO user) {
        MyApplication.afterLoginSuccess(user);
        view.loginSuccess(user);
    }
}
