package com.sherlock.imapp.presenter;

import com.sherlock.imapp.callback.GroupListCallback;
import com.sherlock.imapp.entity.Group;
import com.sherlock.imapp.manager.GroupListManager;
import com.sherlock.imapp.view.GroupListView;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class GroupListPresenter implements GroupListCallback{
    private GroupListManager manager;
    private GroupListView view;

    public GroupListPresenter(GroupListView view){
        manager = new GroupListManager(this);
        this.view = view;
    }
    public void getGroupList(){
        manager.getGroupList();
    }

    @Override
    public void getGroupListSuccess(List<Group> list) {
        view.getGroupListSuccess(list);
    }
}
