package com.sherlock.imapp.db;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.Group;
import com.sherlock.imapp.entity.GroupMem;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.netty.entity.PicMessage;
import com.sherlock.imapp.netty.entity.TextMessage;
import com.sherlock.imapp.utils.ListUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class DBService extends SQLiteOpenHelper{
    private SQLiteDatabase db;

    private static DBService dBService;
    private static Context context;

    private DBService(int userId) {
        super(context,"user_"+userId+".db",null,1);
        db = getWritableDatabase();
    }
    public static void setContext(Context context) {
        DBService.context = context;
    }
    public static void init(int userId){
        dBService = new DBService(userId);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        System.out.println("数据库创建");
        this.db = db;
        //创建用户表
        db.execSQL("create table if not exists im_user(id integer primary key,account text not null, sex integer,name text, headPic text,isFriend integer)");
        //创建群组表
        db.execSQL("create table if not exists im_group(id integer primary key,name text,headPic text)");
        //创建群组成员表
        db.execSQL("create table if not exists im_group_mem(id integer primary key autoincrement,groupId integer not null,userId integer not null)");
        //创建会话列表
        db.execSQL("create table if not exists im_conversation(id integer primary key autoincrement," +
                "gtype integer not null,gid integer not null,unreadCount integer,lastMsg text,lastMsgTime integer)");
        //创建消息列表
        db.execSQL("create table if not exists im_message(id integer primary key autoincrement," +
                "gtype integer not null,gid integer not null,messageType integer not null,mid text unique not null,time integer not null,msg text)");
        //创建新的好友的表格
        db.execSQL("create table if not exists im_new_friends_count(count integer not null)");
        String sql = "insert into im_new_friends_count values(0)";
        db.execSQL(sql, new String[]{});
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        System.out.println("数据库升级");
    }

    /**
     * 删除所有联系人
     */
    public static void delAllUser(){
        dBService.db.execSQL("delete from im_user");
    }
    public static int getNewFriendsCount(){
        String sql = "select * from im_new_friends_count";
        Cursor cursor =dBService.db.rawQuery(sql, new String[]{});
        int count = 0;
        if (cursor.getCount()>0) {
            int countIdx = cursor.getColumnIndex("count");
            cursor.moveToNext();
            count = cursor.getInt(countIdx);
        }
        return count;
    }

    /**
     * 增加一个联系人
     * @param user
     */
    private static Lock userLock = new ReentrantLock();
    public static void upsertUser(UserVO user,boolean updateFriendRel){
        try {
            userLock.lock();
            UserVO po = getUser(user.getId());
            if (po == null) {
                String sql = "insert into im_user values(?,?,?,?,?,?)";
                dBService.db.execSQL(sql, new String[]{user.getId()+"", user.getAccount(), user.getSex()+"", user.getName(), user.getHeadPic(), user.getIsFriend()+""});
                return ;
            }
            if (updateFriendRel &&
                    (user.getIsFriend()== OtherConstant.IsFriendEnum.friend.getIndex()
                            || user.getIsFriend()== OtherConstant.IsFriendEnum.notFriend.getIndex())
                    ) {
                String sql = "update im_user set isFriend=? where id=?";
                dBService.db.execSQL(sql,new String[]{user.getIsFriend()+"",user.getId()+""});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            userLock.unlock();
        }
    }

    /**
     * 查询联系人
     * @param id
     */
    public static UserVO getUser(int id){
        String sql = "select * from im_user where id=?";
        Cursor cursor =dBService.db.rawQuery(sql, new String[]{id+""});
        UserVO po = null;
        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int sexIdx = cursor.getColumnIndex("sex");
            int nameIdx = cursor.getColumnIndex("name");
            int headPicIdx = cursor.getColumnIndex("headPic");
            int isFriendIdx = cursor.getColumnIndex("isFriend");
            cursor.moveToNext();
            po = new UserVO();
            po.setId(cursor.getInt(idIdx));
            po.setSex(cursor.getInt(sexIdx));
            po.setName(cursor.getString(nameIdx));
            po.setHeadPic(cursor.getString(headPicIdx));
            po.setIsFriend(cursor.getInt(isFriendIdx));
        }
        return po;
    }
    /**
     * 联系人列表
     */
    public static List<UserVO> getFriendList(){
        String sql = "select * from im_user where isFriend=? order by name";
        Cursor cursor =dBService.db.rawQuery(sql, new String[]{OtherConstant.IsFriendEnum.friend.getIndex()+""});
        List<UserVO> list = new ArrayList<>(cursor.getCount());

        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int accountIdx = cursor.getColumnIndex("account");
            int sexIdx = cursor.getColumnIndex("sex");
            int nameIdx = cursor.getColumnIndex("name");
            int headPicIdx = cursor.getColumnIndex("headPic");
            while (cursor.moveToNext()) {
                UserVO po = new UserVO();
                po.setId(cursor.getInt(idIdx));
                po.setAccount(cursor.getString(accountIdx));
                po.setSex(cursor.getInt(sexIdx));
                po.setName(cursor.getString(nameIdx));
                po.setHeadPic(cursor.getString(headPicIdx));
                list.add(po);
            }
        }
        return list;
    }
    /**
     * 查询联系人
     */
    public static List<UserVO> getFriendList(List<Integer> uids){
        if (uids.isEmpty()) {
            return new ArrayList<>();
        }
        StringBuilder sb = new StringBuilder();
        sb.append("select * from im_user where id in (");

        for (Integer uid : uids) {
            sb.append(uid).append(',');
        }
        if (sb.length()>0) {
            sb.deleteCharAt(sb.length()-1);
        }
        sb.append(")");
        String sql = sb.toString();

        Cursor cursor =dBService.db.rawQuery(sql, new String[]{});
        List<UserVO> list = new ArrayList<>(cursor.getCount());
        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int sexIdx = cursor.getColumnIndex("sex");
            int nameIdx = cursor.getColumnIndex("name");
            int headPicIdx = cursor.getColumnIndex("headPic");
            while (cursor.moveToNext()) {
                UserVO po = new UserVO();
                po.setId(cursor.getInt(idIdx));
                po.setSex(cursor.getInt(sexIdx));
                po.setName(cursor.getString(nameIdx));
                po.setHeadPic(cursor.getString(headPicIdx));
                list.add(po);
            }
        }
        return list;
    }

    /**
     * 增加新的好友请求数
     * @param count
     */
    public static void incrNewFriendsCount(int count){
        String sql = "update im_new_friends_count set count=count+?";
        dBService.db.execSQL(sql,  new String[]{count+""});
    }
    public static void resetNewFriendsCount(){
        String sql = "update im_new_friends_count set count=0";
        dBService.db.execSQL(sql,  new String[]{});
    }

    private static Lock groupLock = new ReentrantLock();
    public static void delAllGroup(){
        dBService.db.execSQL("delete from im_group");
    }
    /**
     * 增加一个组
     * @param group
     */
    public static void upsertGroup(Group group){
        try {
            groupLock.lock();
            Group po = getGroup(group.getId());
            if (po == null) {
                String sql = "insert into im_group values(?,?,?)";
                dBService.db.execSQL(sql, new String[]{group.getId()+"",group.getName(),group.getHeadPic()});
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            groupLock.unlock();
        }
    }
    public static Group getGroup(int id){
        String sql = "select * from im_group where id=?";
        Cursor cursor =dBService.db.rawQuery(sql, new String[]{id+""});
        Group po = null;
        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int nameIdx = cursor.getColumnIndex("name");
            int headPicIdx = cursor.getColumnIndex("headPic");
            cursor.moveToNext();
            po = new Group();
            po.setId(cursor.getInt(idIdx));
            po.setName(cursor.getString(nameIdx));
            po.setHeadPic(cursor.getString(headPicIdx));
        }
        return po;
    }

    public static List<Group> getGroupList() {
        String sql = "select * from im_group order by name";
        Cursor cursor =dBService.db.rawQuery(sql, new String[]{});
        List<Group> list = new ArrayList<>(cursor.getCount());

        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int nameIdx = cursor.getColumnIndex("name");
            int headPicIdx = cursor.getColumnIndex("headPic");
            while (cursor.moveToNext()) {
                Group po = new Group();
                po.setId(cursor.getInt(idIdx));
                po.setName(cursor.getString(nameIdx));
                po.setHeadPic(cursor.getString(headPicIdx));
                list.add(po);
            }
        }
        return list;
    }

    public static void upsertGroupMem(GroupMem po){
        GroupMem item = getGroupMem(po.getGroupId(),po.getUserId());
        if (item == null) {
            String sql = "insert into im_group_mem(groupId,userId) values(?,?)";
            dBService.db.execSQL(sql, new String[]{po.getGroupId()+"",po.getUserId()+""});
        }
    }

    public static GroupMem getGroupMem(int groupId,int userId){
        String sql = "select * from im_group_mem where groupId=? and userId=?";
        Cursor cursor =dBService.db.rawQuery(sql, new String[]{groupId+"",userId+""});
        GroupMem po = null;
        if (cursor.getCount()>0) {
            int idIdx = cursor.getColumnIndex("id");
            int groupIdIdx = cursor.getColumnIndex("groupId");
            int userIdIdx = cursor.getColumnIndex("userId");
            cursor.moveToNext();
            po = new GroupMem();
            po.setId(cursor.getInt(idIdx));
            po.setGroupId(cursor.getInt(groupIdIdx));
            po.setUserId(cursor.getInt(userIdIdx));
        }
        return po;
    }
    /**
     * 获取群成员信息
     * @param groupId
     * @return
     */
    public static List<UserVO> getGroupMember(int groupId) {
        String sql = "select b.* from im_group_mem a left join im_user b on a.userId=b.id where a.groupId=?";
        Cursor cursor = dBService.db.rawQuery(sql,new String[]{groupId+""});
        int count = cursor.getCount();
        List<UserVO> list = new ArrayList<>(count);
        if (count>0) {
            int idIdx = cursor.getColumnIndex("id");
            int sexIdx = cursor.getColumnIndex("sex");
            int nameIdx = cursor.getColumnIndex("name");
            int headPicIdx = cursor.getColumnIndex("headPic");
            while (cursor.moveToNext()) {
                UserVO po = new UserVO();
                po.setId(cursor.getInt(idIdx));
                po.setSex(cursor.getInt(sexIdx));
                po.setName(cursor.getString(nameIdx));
                po.setHeadPic(cursor.getString(headPicIdx));
                list.add(po);
            }
        }
        return list;
    }
    /**
     * 保存会话
     * @param gtype
     * @param gid
     * @param unreadCount
     * @param lastMsg
     * @param lastMsgTime
     */
    public static void upsertConversation(int gtype, int gid, int unreadCount,String lastMsg,long lastMsgTime){
        Conversation po = getConversation(gtype,gid);
        if (po==null) {
            String sql = "insert into im_conversation values(null,?,?,?,?,?)";
            dBService.db.execSQL(sql,new String[]{gtype+"", gid+"",unreadCount+"",lastMsg,lastMsgTime+""});
        } else {
            String sql = "update im_conversation set unreadCount=unreadCount+?,lastMsg=?,lastMsgTime=? where gtype=? and gid=?";
            dBService.db.execSQL(sql,new String[]{unreadCount+"",lastMsg,lastMsgTime+"",gtype+"", gid+""});
        }
    }
    public  static void clearUnreadCount(int gtype,int gid){
        String sql = "update im_conversation set unreadCount=0 where gtype=? and gid=?";
        dBService.db.execSQL(sql,new String[]{gtype+"",gid+""});
    }
    public static Conversation getConversation(int gtype, int gid){
        String sql = "select * from im_conversation where gtype=? and gid=?";
        Cursor cursor = dBService.db.rawQuery(sql,new String[]{gtype+"",gid+""});
        Conversation po = null;
        if (cursor.getCount()>0) {
            int gtypeIdx = cursor.getColumnIndex("gtype");
            int gidIdx = cursor.getColumnIndex("gid");
            int unreadCountIdx = cursor.getColumnIndex("unreadCount");
            int lastMsgTimeIdx = cursor.getColumnIndex("lastMsgTime");
            cursor.moveToNext();
            po = new Conversation();
            po.setGtype(cursor.getInt(gtypeIdx));
            po.setGid(cursor.getInt(gidIdx));
            po.setUnreadCount(cursor.getInt(unreadCountIdx));
            po.setLastMsgTime(cursor.getInt(lastMsgTimeIdx));
        }
        return po;
    }

    public static List<Conversation> getConversationList(){
        String sql1 = "select * from im_conversation order by lastMsgTime desc";
        Cursor cursor = dBService.db.rawQuery(sql1,new String[]{});
        List<Conversation> result = new ArrayList<>(cursor.getCount());
        int count = cursor.getCount();
        if (count>0) {
            int gtypeIdx = cursor.getColumnIndex("gtype");
            int gidIdx = cursor.getColumnIndex("gid");
            int unreadCountIdx = cursor.getColumnIndex("unreadCount");
            int lastMsgIdx = cursor.getColumnIndex("lastMsg");
            int lastMsgTimeIdx = cursor.getColumnIndex("lastMsgTime");
            List<Integer> uids = new ArrayList<>();
//            List<Integer> groupIds = new ArrayList<>();
            while (cursor.moveToNext()) {
                Conversation po = new Conversation();
                po.setGtype(cursor.getInt(gtypeIdx));
                po.setGid(cursor.getInt(gidIdx));
                po.setUnreadCount(cursor.getInt(unreadCountIdx));
                po.setLastMsg(cursor.getString(lastMsgIdx));
                po.setLastMsgTime(cursor.getLong(lastMsgTimeIdx));
                result.add(po);
                if (MessageConstant.GTypeEnum.user.getIndex() == po.getGtype()) {
                    uids.add(po.getGid());
                }
            }
            Map<Integer, UserVO> userMap =  ListUtil.list2Map(getFriendList(uids),"getId",UserVO.class);
            Map<Integer, Group> groupMap =  ListUtil.list2Map(getGroupList(),"getId",Group.class);
            for (Conversation po : result) {
                if (MessageConstant.GTypeEnum.user.getIndex() == po.getGtype()) {
                    UserVO user = userMap.get(po.getGid());
                    if (user!=null) {
                        po.setName(user.getName());
                        po.setPic(user.getHeadPic());
                    }
                } else if (MessageConstant.GTypeEnum.group.getIndex() == po.getGtype()) {
                    Group group = groupMap.get(po.getGid());
                    if (group!=null) {
                        po.setName(group.getName());
                        po.setPic(group.getHeadPic());
                    }
                }
            }
        }
        return result;
    }
    /**
     * 保存聊天记录
     * @param msg
     */
    public static void saveServerImMessage(ImMessage msg){
        String sql = "insert into im_message values(null,?,?,?,?,?,?)";
        dBService.db.execSQL(sql,new String[]{msg.getGtype()+"",msg.getGid()+"",msg.getMessageType()+"",msg.getMid(),msg.getTime()+"",JSONObject.toJSONString(msg)});
    }

    /**
     * 获取会话组信息
     * @param gtype
     * @param gid
     * @return
     */
    public static List<ImMessage> getServerImMessage(int gtype, int gid){
        String sql = "select * from im_message where gtype=? and gid=? order by time";
        Cursor cursor = dBService.db.rawQuery(sql,new String[]{gtype+"",gid+""});
        List<ImMessage> result = new ArrayList<>(cursor.getCount());
        if (cursor.getCount()>0) {
            int msgIdx = cursor.getColumnIndex("msg");
            int messageTypeIdx = cursor.getColumnIndex("messageType");
            while(cursor.moveToNext()) {
                String msg = cursor.getString(msgIdx);
                int messageType = cursor.getInt(messageTypeIdx);
                switch (messageType) {
                    case MessageConstant.MSGTYPE_TEXT: {
                        TextMessage message = JSONObject.parseObject(msg, TextMessage.class);
                        result.add(message);
                        break;
                    }
                    case  MessageConstant.MSGTYPE_PIC: {
                        PicMessage message = JSONObject.parseObject(msg, PicMessage.class);
                        result.add(message);
                        break;
                    }
                }
            }
        }
        return result;
    }
}
