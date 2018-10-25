package com.sherlock.imapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.activity.AddFriendsActivity;
import com.sherlock.imapp.activity.ConversationActivity;
import com.sherlock.imapp.activity.ImActivity;
import com.sherlock.imapp.activity.NewFriendsActivity;
import com.sherlock.imapp.activity.UserInfoActivity;
import com.sherlock.imapp.adapter.FriendListAdapter;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.FriendListPresenter;
import com.sherlock.imapp.view.FriendListView;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class FriendListFragment extends Fragment implements FriendListView,View.OnClickListener {
    private ListView listView;

    private FriendListPresenter presenter;

    private View view;
    private QBadgeView badgeView;

    private ImActivity activity;
    public void setActivity(ImActivity activity){
        this.activity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_friend, container, false);
        presenter = new FriendListPresenter(this);
        MyApplication.getInstance().friendListFragment = new WeakReference<FriendListFragment>(this);

        view.findViewById(R.id.addFriendsBtn).setOnClickListener(this);
        TextView newFriendsView = view.findViewById(R.id.new_friends);

        badgeView = new QBadgeView(view.getContext());
        LinearLayout newFriendsContainer = view.findViewById(R.id.newFriendsContainer);
        badgeView.bindTarget(newFriendsContainer);
        badgeView.setGravityOffset(260,0,true);
        badgeView.setForegroundGravity(Gravity.LEFT);
        newFriendsView.setOnClickListener(this);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        presenter.getNewFriendCount();
        showFriendList();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.addFriendsBtn:
                Intent intent = new Intent(getActivity(), AddFriendsActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
                break;
            case R.id.new_friends:
                changePage2NewFriends();
                break;
        }
    }
    private void changePage2NewFriends() {
        Intent intent = new Intent(getActivity(), NewFriendsActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    private void changePage2UserInfo(UserVO userVO) {
        Intent intent = new Intent(activity, UserInfoActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(UserInfoActivity.userVOKey, Parcels.wrap(userVO));
        intent.putExtras(mBundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }
    @Override
    public void getFriendListSuccess(List<UserVO> list) {
        Activity activity = getActivity();
        if (activity!=null) {
            activity.runOnUiThread( new Runnable() {
                private List<UserVO> userList;
                @Override
                public void run() {
                    FriendListAdapter adapter = new FriendListAdapter(getActivity(), R.layout.item_user,userList);
                    listView = (ListView) view.findViewById(R.id.list_user);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //获得选中项的HashMap对象
                            UserVO item = (UserVO) listView.getItemAtPosition(position);
                            changePage2UserInfo(item);
                        }
                    });
                }
                public Runnable setUserList(List<UserVO> userList) {
                    this.userList = userList;
                    return this;
                }
            }.setUserList(list));
        }
    }
    @Override
    public void getNewFriendCountSuccess(int count) {
        badgeView.setBadgeNumber(count);
        activity.showTotalContactUnreadCount(count);
    }
    /**
     * 显示联系人列表
     */
    public void showFriendList() {
        presenter.getFriendList();
    }

    public void addNewFriendCount(final int count){
        if (activity!=null) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    badgeView.setBadgeNumber(badgeView.getBadgeNumber()+count);
                    activity.addTotalContactUnreadCount(count);
                }
            });
        }
    }
}
