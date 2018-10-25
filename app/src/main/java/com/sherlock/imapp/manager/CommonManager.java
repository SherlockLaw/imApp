package com.sherlock.imapp.manager;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.HttpProxy;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.constant.UrlConstant;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.GroupMem;
import com.sherlock.imapp.entity.GroupVO;
import com.sherlock.imapp.entity.UnreadVO;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.TCPClientInboundHandler;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.netty.entity.OrderMessage;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * Created by Administrator on 2018/5/13 0013.
 */

public class CommonManager {

    /**
     * 从服务器拉取所有联系人，在联系人窗口时，刷新页面
     */
    public static void getFriendListFromServerAndSave() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", MyApplication.getUserId());
        String result = HttpProxy.get(UrlConstant.friend_getFriends, params, null);
        List<UserVO> list = JSONObject.parseArray(result, UserVO.class);
        //删除所有的联系人
        DBService.delAllUser();
        //存储最新的联系人
        for (UserVO po : list) {
            po.setIsFriend(OtherConstant.IsFriendEnum.friend.getIndex());
            DBService.upsertUser(po);
        }
        //刷新页面
        MyApplication.getInstance().showFriendListFragment();
    }

    /**
     * 获取用户并且存储在数据库
     */
    public static void getFriendListFromServerAndSave(List<Integer> userIds) {
        Map<String, Object> params = new HashMap<>();
        params.put("userIds", HttpProxy.getParamStrByArray(userIds));
        String result = HttpProxy.get(UrlConstant.user_getUserListByIds, params, null);
        List<UserVO> list = JSONObject.parseArray(result, UserVO.class);
        //存储最新的联系人
        for (UserVO po : list) {
            po.setIsFriend(OtherConstant.IsFriendEnum.notFriend.getIndex());
            DBService.upsertUser(po);
        }
    }

    /**
     * 从服务器拉取所有联系人，在联系人窗口时，刷新页面
     */
    public static void getGroupListFromServerAndSave() {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", MyApplication.getUserId());
        String result = HttpProxy.get(UrlConstant.group_getGroups, params, null);
        List<GroupVO> list = JSONObject.parseArray(result, GroupVO.class);
        //删除所有的联系人
        DBService.delAllGroup();
        for (GroupVO po : list) {
            DBService.upsertGroup(po);
            List<Integer> memberIds = po.getMemberIds();
            //存储用户信息
            getFriendListFromServerAndSave(memberIds);
            //成员
            for (int memberId : memberIds) {
                GroupMem mem = new GroupMem();
                mem.setGroupId(po.getId());
                mem.setUserId(memberId);
                DBService.upsertGroupMem(mem);
            }
        }
        //刷新页面
        MyApplication.getInstance().showGroupListFragment();
    }
    /**
     * 从服务器拉取所有联系人，在联系人窗口时，刷新页面
     */
    public static void getGroupFromServerAndSave(int groupId) {
        Map<String, Object> params = new HashMap<>();
        params.put("groupId", MyApplication.getUserId());
        String result = HttpProxy.get(UrlConstant.group_getById, params, null);
        GroupVO po = JSONObject.parseObject(result, GroupVO.class);
        if (po!=null) {
            DBService.upsertGroup(po);
            List<UserVO> members = po.getMemberList();
            //成员
            for (UserVO member : members) {
                member.setIsFriend(OtherConstant.IsFriendEnum.notFriend.getIndex());
                DBService.upsertUser(member);

                GroupMem mem = new GroupMem();
                mem.setGroupId(po.getId());
                mem.setUserId(member.getId());
                DBService.upsertGroupMem(mem);
            }
        }
        //刷新页面
        MyApplication.getInstance().showGroupListFragment();
    }

    /**
     * 连接成功后从服务器拉取会话未读数，在页面时刷新总未读和会话列表
     */
    public static void getUnreadCountMapFromServerAndSave() {
        Integer userId = MyApplication.getUserId();
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        String result = HttpProxy.get(UrlConstant.message_getUnreadCountMap, params, null);
        JSONObject job = JSONObject.parseObject(result);
        Set<Map.Entry<String, Object>> entrySet = job.entrySet();
        for (Map.Entry<String, Object> entry : entrySet) {
            String hashKey =  String.valueOf(entry.getKey());
            String[] v = hashKey.split("_");
            Integer gtype = Integer.valueOf(v[0]);
            Integer gid = Integer.valueOf(v[1]);
            String value = entry.getValue().toString();
            UnreadVO unreadVO = JSONObject.parseObject(value, UnreadVO.class);
            DBService.upsertConversation(gtype,gid, unreadVO.getCount(),unreadVO.getLastMsg(),unreadVO.getLastTime());
        }
        //刷新页面
        MyApplication.getInstance().showConversationListFragment();
        deleteUnreadCountMap(userId, result);
    }
    private static void deleteUnreadCountMap(int userId, String map) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("map", map);
        String result = HttpProxy.post(UrlConstant.message_deleteUnreadCountMap, params, null);
    }
    /**
     * 从服务器拉取离线消息，只有在会话窗口中才会拉取
     */
    public static void getConversationOffLineMsgAndSave(boolean shownow){
        Conversation conversation = MyApplication.getInstance().getConversation();
        if (conversation!=null) {
            //从本地获取未读消息数，事先从服务器拉取
            Conversation con = DBService.getConversation(conversation.getGtype(),conversation.getGid());
            Integer userId = MyApplication.getUserId();
            //从服务器拉取
            Map<String, Object> params = new HashMap<>();
            params.put("userId", userId);
            params.put("gtype", conversation.getGtype());
            params.put("gid", conversation.getGid());
            String result = HttpProxy.get(UrlConstant.message_getConversationOfflineMessage, params, null);
            JSONArray array = JSONArray.parseArray(result);
            long time = -1l;
            for (int i=0; i<array.size(); i++) {
                String str = array.getString(i);
                ImMessage msg = MessageConstant.getImMessage(str);
                DBService.saveServerImMessage(msg);
                if (shownow) {
                    //即时消息显示
                    MyApplication.getInstance().showServerImMessage(msg);
                }
                if (time < msg.getTime()) {
                    time = msg.getTime();
                }
            }
            if (time>0) {
                deleteConversationOfflineMessage(userId, conversation.getGtype(), conversation.getGid(), time);
            }
        }
    }
    private static void deleteConversationOfflineMessage(Integer userId, int gtype, int gid, long lastMsgTime) {
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("gtype", gtype);
        params.put("gid", gid);
        params.put("lastMsgTime", lastMsgTime);
        String result = HttpProxy.post(UrlConstant.message_deleteConversationOfflineMessage, params, null);
    }

    public static void getOfflineOrderMessageAndSave() {
        //从服务器拉取
        Map<String, Object> params = new HashMap<>();
        Integer userId = MyApplication.getUserId();
        params.put("userId", userId);
        String result = HttpProxy.get(UrlConstant.message_getOfflineOrderMessage, params, null);
        JSONArray array = JSONArray.parseArray(result);
        long time = -1l;
        for (int i = 0; i < array.size(); i++) {
            String str = array.getString(i);
            JSONObject job = JSONObject.parseObject(str);
            Byte msgType = job.getByte("msgType");
            OrderMessage msg = (OrderMessage) MessageConstant.getMessage(msgType, str);
            TCPClientInboundHandler.orderMessageHandler(msg);
            if (time < msg.getTime()) {
                time = msg.getTime();
            }
        }
        if (time >= 0) {
            delOfflineOrderMessage(userId, time);
        }
    }
    private static void delOfflineOrderMessage(Integer userId, long time){
        Map<String, Object> params = new HashMap<>();
        params.put("userId", userId);
        params.put("lastMsgTime", time);
        String result = HttpProxy.post(UrlConstant.message_delOfflineOrderMessage, params, null);
    }
}
