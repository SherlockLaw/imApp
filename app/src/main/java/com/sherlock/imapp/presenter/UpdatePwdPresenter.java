package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.UpdatePwdCallback;
import com.sherlock.imapp.manager.UpdatePwdManager;
import com.sherlock.imapp.view.UpdatePwdView;

public class UpdatePwdPresenter implements UpdatePwdCallback {
    private UpdatePwdView view;
    private UpdatePwdManager manager;
    public UpdatePwdPresenter(UpdatePwdView view){
        this.view = view;
        manager = new UpdatePwdManager(this);
    }
    public void updatePwd(final String account, final String oldPwd, final String newPwd, final String retypeNewPwd){
        manager.updatePwd(account, oldPwd, newPwd, retypeNewPwd);
    }
    @Override
    public void updatePwdSuccess() {
        view.updatePwdSuccess();
    }
}
