package com.sherlock.imapp;

import android.app.Application;
import android.content.Context;
import android.os.Handler;
import android.os.Looper;

import com.sherlock.imapp.activity.ConversationActivity;
import com.sherlock.imapp.db.AccountDBService;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.ExceptionHandler;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.fragment.FriendListFragment;
import com.sherlock.imapp.fragment.ConversationListFragment;
import com.sherlock.imapp.fragment.GroupListFragment;
import com.sherlock.imapp.netty.TCPClient;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.entity.ImMessage;

import java.lang.ref.WeakReference;
import java.util.HashMap;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class MyApplication extends Application {
    private static MyApplication instance;
    private HashMap<String,Object> storage = new HashMap<>();

    private Handler handler = new Handler(Looper.getMainLooper());

    public final static String KEY_USER = "user";

    private final static float HEAP_UTILIZATION = 0.75f;
    private final static int MIN_HEAP_SIZE = 6* 1024* 1024 ;

    public WeakReference<ConversationActivity> conversationActivity;
    public WeakReference<ConversationListFragment> conversationListFragment;
    public WeakReference<FriendListFragment> friendListFragment;
    public WeakReference<GroupListFragment> groupListFragment;

    @Override
    public void onCreate() {
        super.onCreate();
        ExceptionHandler exceptionHandler = ExceptionHandler.getInstance();
        Context ctx = getApplicationContext();
        // 注册crashHandler
        exceptionHandler.init(ctx);
        ToastService.init(ctx);
        AccountDBService.setContext(ctx);
        DBService.setContext(ctx);
        instance = this;
    }

    public static void afterLoginSuccess(UserVO user){
        //设置user
        setUser(user);
        DBService.init(user.getId());
        TCPClient.getInstance().connect();
        TCPClient.operationsAfterLoginSuccess();
    }

    public boolean showFriendListFragment(){
        if (friendListFragment ==null) {
            return false;
        }
        FriendListFragment fragment = friendListFragment.get();
        if (fragment==null) {
            return false;
        }
        fragment.showFriendList();
        return true;
    }

    public boolean addNewFriendsCount(int count){
        if (friendListFragment ==null) {
            return false;
        }
        FriendListFragment fragment = friendListFragment.get();
        if (fragment==null) {
            return false;
        }
        fragment.addNewFriendCount(count);
        return true;
    }

    public boolean showGroupListFragment(){
        if (groupListFragment ==null) {
            return false;
        }
        GroupListFragment fragment = groupListFragment.get();
        if (fragment==null) {
            return false;
        }
        fragment.showGroupList();
        return true;
    }
    public boolean showConversationListFragment(){
        if (conversationListFragment==null) {
            return false;
        }
        ConversationListFragment fragment = conversationListFragment.get();
        if (fragment==null) {
            return false;
        }
        fragment.showConversationList();
        return true;
    }

    public Conversation getConversation(){
        if (conversationActivity!=null) {
            ConversationActivity activity = conversationActivity.get();
            if (activity!=null) {
                return activity.getConversation();
            }
        }
        return null;
    }
    public boolean showServerImMessage(ImMessage msg){
        if (conversationActivity==null) {
            return false;
        }
        ConversationActivity activity = conversationActivity.get();
        if (activity==null) {
            return false;
        }
        activity.showImMessage(msg);
        return true;
    }

    public static MyApplication getInstance(){
        return instance;
    }

    private Object getProperty(String key) {
        return storage.get(key);
    }
    private Object setProperty(String key,Object value) {
        return storage.put(key,value);
    }

    /**
     * 设置当前用户信息
     * @param user
     */
    public static void setUser(UserVO user){
        getInstance().setProperty(KEY_USER,user);
    }

    /**
     * 获得当前用户
     * @return
     */
    public static UserVO getUser(){
        return (UserVO) getInstance().getProperty(KEY_USER);
    }

    /**
     * 获得当前用户Id
     * @return
     */
    public static Integer getUserId(){
        UserVO user = (UserVO) getInstance().getProperty(KEY_USER);
        if (user!=null) {
            return user.getId();
        }
        return null;
    }

    /**
     * 获得当前用户token
     * @return
     */
    public static String getToken(){
        UserVO user = (UserVO) getInstance().getProperty(KEY_USER);
        if (user!=null) {
            return user.getToken();
        }
        return null;
    }

    /**
     * 登出
     */
    public void logout(){
        storage.clear();
    }

    /**
     * 在主线程中运行
     * @param runnable
     */
    public void runOnUiThread(Runnable runnable){
        handler.post(runnable);
    }
}
