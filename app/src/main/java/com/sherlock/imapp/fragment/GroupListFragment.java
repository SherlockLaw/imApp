package com.sherlock.imapp.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.activity.AddFriendsActivity;
import com.sherlock.imapp.activity.AddGroupActivity;
import com.sherlock.imapp.adapter.GroupListAdapter;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.Group;
import com.sherlock.imapp.presenter.GroupListPresenter;
import com.sherlock.imapp.view.GroupListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2018/6/3 0003.
 */

public class GroupListFragment extends Fragment implements GroupListView, View.OnClickListener{
    private GroupListPresenter presenter;
    private ListView listView;
    private View view;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_group, container, false);
        this.view = view;
        Button addFriendBtn = (Button) view.findViewById(R.id.add_group);
        addFriendBtn.setOnClickListener(this);
        presenter = new GroupListPresenter(this);
        MyApplication.getInstance().groupListFragment = new WeakReference<GroupListFragment>(this);
        showGroupList();
        return view;
    }

    public void showGroupList(){
        presenter.getGroupList();
    }
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group:
                Intent intent = new Intent(getActivity(), AddGroupActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void getGroupListSuccess(List<Group> list) {
        Activity activity = getActivity();
        if (activity!=null) {
            activity.runOnUiThread( new Runnable() {
                private List<Group> list;
                @Override
                public void run() {
                    GroupListAdapter adapter = new GroupListAdapter(getActivity(), R.layout.item_group,list);
                    listView = (ListView) view.findViewById(R.id.list_group);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //获得选中项的HashMap对象
                            Group item = (Group) listView.getItemAtPosition(position);
                            Conversation conversation = new Conversation();
                            conversation.setName(item.getName());
                            conversation.setGtype(MessageConstant.GTypeEnum.group.getIndex());
                            conversation.setGid(item.getId());
                            conversation.setPic(item.getHeadPic());
                            ContactFragment.changePage2Conversation (getActivity(),conversation);
                        }
                    });
                }
                public Runnable setUserList(List<Group> list) {
                    this.list = list;
                    return this;
                }
            }.setUserList(list));
        }
    }
}
