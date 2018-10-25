package com.sherlock.imapp.activity;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.view.MenuItem;
import android.view.View;

import com.sherlock.imapp.R;
import com.sherlock.imapp.fragment.ContactFragment;
import com.sherlock.imapp.fragment.ConversationListFragment;
import com.sherlock.imapp.fragment.MeFragment;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2018/5/5 0005.
 */

public class ImActivity extends BaseActivity {

    private ConversationListFragment conversationListFragment;
    private ContactFragment contactFragment;
    private MeFragment meFragment;

    private QBadgeView unreadBadeView;

    private QBadgeView contactUnreadBadeView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_im);

        FragmentManager fragmentManager = getSupportFragmentManager();
        contactFragment = (ContactFragment) fragmentManager.findFragmentById(R.id.fragment_contact);
        contactFragment.setActivity(this);
        conversationListFragment = (ConversationListFragment) fragmentManager.findFragmentById(R.id.fragment_conversation);
        meFragment = (MeFragment)fragmentManager.findFragmentById(R.id.fragment_me);

        conversationListFragment.setActivity(this);
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.getMenu().getItem(1).setChecked(true);
        //未读数显示
        unreadBadeView = new QBadgeView(this);
        unreadBadeView.bindTarget(findViewById(R.id.navigation_conversationlist));
        unreadBadeView.setGravityOffset(30,0,true);

        //未读数显示
        contactUnreadBadeView = new QBadgeView(this);
        contactUnreadBadeView.bindTarget(findViewById(R.id.navigation_contactlist));
        contactUnreadBadeView.setGravityOffset(30,0,true);

        //默认联系人页面
        showContactList();
        //获取总未读和会话未读
        conversationListFragment.getConversationList();
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_conversationlist:
                    showConversationList();
                    return true;
                case R.id.navigation_contactlist:
                    showContactList();
                    return true;
                case R.id.navigation_notifications:
                    showFragment(meFragment);
                    return true;
            }
            return false;
        }

    };
    private void showConversationList(){
        showFragment(conversationListFragment);
        conversationListFragment.getConversationList();
    }
    private void showContactList(){
        showFragment(contactFragment);
    }

    private void showFragment(Fragment f) {
        contactFragment.getView().setVisibility(View.INVISIBLE);
        conversationListFragment.getView().setVisibility(View.INVISIBLE);
        meFragment.getView().setVisibility(View.INVISIBLE);
        if (f!=null) {
            f.getView().setVisibility(View.VISIBLE);
        }
    }

    public void showConversationTotalUnreadCount(int totalUnreadCount){
        unreadBadeView.setBadgeNumber(totalUnreadCount);
    }
    public void showTotalContactUnreadCount(int totalUnreadCount){
        contactUnreadBadeView.setBadgeNumber(totalUnreadCount);
    }
    public void addTotalContactUnreadCount(int addCount){
        contactUnreadBadeView.setBadgeNumber(contactUnreadBadeView.getBadgeNumber()+addCount);
    }

    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }

    @Override
    protected void onStart() {
        //刷新红点数
        conversationListFragment.getConversationList();
        super.onStart();
    }
}
