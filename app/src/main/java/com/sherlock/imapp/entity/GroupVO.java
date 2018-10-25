package com.sherlock.imapp.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class GroupVO extends Group{
    private List<Integer> memberIds;

    private List<UserVO> memberList;

    public List<Integer> getMemberIds() {
        return memberIds;
    }

    public void setMemberIds(List<Integer> memberIds) {
        this.memberIds = memberIds;
    }

    public List<UserVO> getMemberList() {
        return memberList;
    }

    public void setMemberList(List<UserVO> memberList) {
        this.memberList = memberList;
    }

}
