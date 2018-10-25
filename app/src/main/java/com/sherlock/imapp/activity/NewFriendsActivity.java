package com.sherlock.imapp.activity;

import android.os.Bundle;
import android.widget.ListView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.adapter.NewFriendsAdapter;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.FriendReqVO;
import com.sherlock.imapp.presenter.NewFriendsPresenter;
import com.sherlock.imapp.view.NewFriendsView;

import java.util.ArrayList;
import java.util.List;

public class NewFriendsActivity extends BaseActivity implements NewFriendsView{
    private NewFriendsPresenter presenter;
    private ListView listView;
    private List<FriendReqVO> list = new ArrayList<>();
    private NewFriendsAdapter adapter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_friends);
        presenter = new NewFriendsPresenter(this);
        listView = findViewById(R.id.list_new_friends);
        adapter = new NewFriendsAdapter(NewFriendsActivity.this,R.layout.item_new_friends,list,this);
        listView.setAdapter(adapter);
        presenter.getList();
    }
    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void getListSuccess(final List<FriendReqVO> newlist) {
        list.clear();
        list.addAll(newlist);
        DBService.resetNewFriendsCount();
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    public void acceptRequest(int id, MessageConstant.AddFriendConfirmStatusEnum status){
        presenter.acceptRequest(id, status);
    }
    @Override
    public void acceptRequestSuccess(int id, MessageConstant.AddFriendConfirmStatusEnum status) {
        for (FriendReqVO reqVO : list) {
            if (reqVO.getId() == id) {
                reqVO.setStatus(status.getIndex());
                break;
            }
        }
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
}
