package com.sherlock.imapp.constant;

import java.util.HashSet;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class UrlConstant {
    public static final String user_register =  "/user/register";
    public static final String user_login =  "/user/login";
    public static final String user_search =  "/user/search";
    public static final String user_getUserById =  "/user/getUserById";
    public static final String user_getUserListByIds =  "/user/getUserListByIds";

    public static final String friend_addFriendRequest = "/friend/addFriendRequest";
    public static final String friend_delFriend = "/friend/delFriend";
    public static final String friend_getFriends = "/friend/getFriends";
    public static final String friend_getFriendRequests = "/friend/getFriendRequests";
    public static final String friend_confirmFriendRequest = "/friend/confirmFriendRequest";

    public static final String group_getById = "/group/getById";
    public static final String group_getGroups = "/group/getGroups";
    public static final String group_addGroup = "/group/addGroup";

    public static final String message_sendMessage =  "/message/sendMessage";
    public static final String message_getUnreadCountMap =  "/message/getUnreadCountMap";
    public static final String message_deleteUnreadCountMap =  "/message/deleteUnreadCountMap";
    public static final String message_getConversationOfflineMessage =  "/message/getConversationOfflineMessage";
    public static final String message_deleteConversationOfflineMessage =  "/message/deleteConversationOfflineMessage";
    public static final String message_getOfflineOrderMessage =  "/message/getOfflineOrderMessage";
    public static final String message_delOfflineOrderMessage =  "/message/delOfflineOrderMessage";

    public static final String file_uploadImage = "/file/uploadImage";

    private static Set<String> notNeedToken = new HashSet<>();
    static {
        notNeedToken.add(user_register);
        notNeedToken.add(user_login);
    }
    public static boolean isUrlNeedToken(String url){
        return !notNeedToken.contains(url);
    }
}
