package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.AddGroupCallback;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.manager.AddGroupManager;
import com.sherlock.imapp.view.AddGroupView;

import java.io.File;
import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class AddGroupPresenter implements AddGroupCallback{
    private AddGroupView view;
    private AddGroupManager manager;
    public AddGroupPresenter(AddGroupView view){
        this.view = view;
        manager = new AddGroupManager(this);
    }
    public void getFriendList(){
        manager.getFriendList();
    }
    public void addGroup(File headPic, String name, List<Integer> userIds){
        manager.addGroup(headPic, name, userIds);
    }

    @Override
    public void getFriendListSuccess(List<UserVO> list) {
        view.getFriendListSuccess(list);
    }

    @Override
    public void addGroupSuccess() {
        view.addGroupSuccess();
    }
}
