package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.UpdateUserCallback;
import com.sherlock.imapp.manager.UpdatePwdManager;
import com.sherlock.imapp.manager.UpdateUserManager;
import com.sherlock.imapp.view.UpdatePwdView;
import com.sherlock.imapp.view.UpdateUserView;

import java.io.File;

public class UpdateUserPresenter implements UpdateUserCallback{
    private UpdateUserView view;
    private UpdateUserManager manager;

    public UpdateUserPresenter(UpdateUserView view){
        this.view = view;
        manager = new UpdateUserManager(this);
    }
    public void update(String name, int sex,  File headPic) {
        manager.update(name, sex, headPic);
    }
    @Override
    public void updateSuccess() {
        view.updateSuccess();
    }
}
