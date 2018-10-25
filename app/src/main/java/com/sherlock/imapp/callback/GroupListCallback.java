package com.sherlock.imapp.callback;

import com.sherlock.imapp.entity.Group;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public interface GroupListCallback {
    public void getGroupListSuccess(List<Group> list);
}
