package com.sherlock.imapp.view;

import com.sherlock.imapp.entity.Group;
import com.sherlock.imapp.entity.UserVO;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public interface GroupListView {
    public void getGroupListSuccess(List<Group> list);
}
