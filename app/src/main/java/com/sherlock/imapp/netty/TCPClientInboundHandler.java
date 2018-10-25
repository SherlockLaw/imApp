package com.sherlock.imapp.netty;

import com.alibaba.fastjson.JSONObject;
import com.sherlock.imapp.Configure;
import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.db.DBService;
import com.sherlock.imapp.common.ServiceException;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.FriendReqVO;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.manager.CommonManager;
import com.sherlock.imapp.netty.entity.AddFriendConfirmMessage;
import com.sherlock.imapp.netty.entity.AddFriendRequestMessage;
import com.sherlock.imapp.netty.entity.AddGroupMessage;
import com.sherlock.imapp.netty.entity.AuthBackMessage;
import com.sherlock.imapp.netty.entity.ClientAuthMessage;
import com.sherlock.imapp.netty.entity.ClientHeartMessage;
import com.sherlock.imapp.netty.entity.DeleteFriendMessage;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.netty.entity.OrderMessage;

import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.ChannelInboundHandlerAdapter;
import io.netty.handler.timeout.IdleStateEvent;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class TCPClientInboundHandler extends ChannelInboundHandlerAdapter {
    //心跳Timer
    private static Timer heartBeatTimer;
    private static Lock heartBeatTimerLock = new ReentrantLock();

    @Override
    public void channelRead(ChannelHandlerContext ctx, Object msg)
            throws Exception {
        System.out.println("收到服务端消息:"+msg.getClass().getSimpleName()+ JSONObject.toJSONString(msg));
        //注意服务端可能根据http接口代发消息 fromUserId 只代表消息发送人，toUserId只代表当时发送的目标
        if (msg instanceof ImMessage) {
            //会话消息
            ImMessage message = (ImMessage) msg;
            Integer userId = MyApplication.getUserId();
            //存消息
            DBService.saveServerImMessage(message);
            int unreadCount=0;
            //如果在会话页面,显示会话信息
            if (!MyApplication.getInstance().showServerImMessage(message)) {
                unreadCount=1;
            }
            //存会话
            DBService.upsertConversation(message.getGtype(), message.getGid(), unreadCount,message.getLastMsg(),message.getTime());
            MyApplication.getInstance().showConversationListFragment();
        } else if (msg instanceof AuthBackMessage) {
            AuthBackMessage message = (AuthBackMessage) msg;
            if (AuthBackMessage.failure==message.getState()) {
                throw new ServiceException("TCP认证失败，请重新登录");
            } else if (AuthBackMessage.success==message.getState()){
                //设置心跳
                startHeartBeatThread(ctx);
                //拉取离线的信息
                TCPClient.operationsAfterConnectSuccess();
            } else {
                throw new ServiceException("服务端返回认证信息有误");
            }
        } else if (msg instanceof OrderMessage) {
            //请求添加好友消息
            OrderMessage message = (OrderMessage) msg;
            orderMessageHandler(message);
        }
    }
    public static void orderMessageHandler(OrderMessage msg){
        if (msg instanceof AddFriendRequestMessage) {
            //请求添加好友消息
            AddFriendRequestMessage message = (AddFriendRequestMessage) msg;
            FriendReqVO vo = new FriendReqVO();
            vo.setId(message.getFromUserId());
            vo.setName(message.getName());
            vo.setSex(message.getSex());
            vo.setHeadPic(message.getHeadPic());
            int count = 1;
            MyApplication.getInstance().addNewFriendsCount(count);
            DBService.incrNewFriendsCount(count);
        } else if (msg instanceof AddFriendConfirmMessage) {
            AddFriendConfirmMessage message = (AddFriendConfirmMessage) msg;
            int count = 1;
            MyApplication.getInstance().addNewFriendsCount(count);
            DBService.incrNewFriendsCount(count);
            if (MessageConstant.AddFriendConfirmStatusEnum.agree.getIndex() == message.getStatus()) {
                UserVO po = message.getUserInfo();
                po.setIsFriend(OtherConstant.IsFriendEnum.friend.getIndex());
                DBService.upsertUser(po);
                //刷新好友列表
                MyApplication.getInstance().showFriendListFragment();
            }

        } else if (msg instanceof AddGroupMessage) {
            AddGroupMessage message = (AddGroupMessage) msg;
            int groupId = message.getGroupId();
            CommonManager.getGroupFromServerAndSave(groupId);
            //刷新群组列表
            MyApplication.getInstance().showGroupListFragment();
        } else if (msg instanceof DeleteFriendMessage) {
            DeleteFriendMessage message = (DeleteFriendMessage) msg;
            UserVO po = new UserVO();
            po.setId(message.getFriendId());
            po.setIsFriend(OtherConstant.IsFriendEnum.notFriend.getIndex());
            DBService.upsertUser(po);
            //刷新好友列表
            MyApplication.getInstance().showFriendListFragment();
        }
    }
    @Override
    public void exceptionCaught(ChannelHandlerContext ctx, Throwable cause)
            throws Exception {
        ctx.close();
        throw new ServiceException("TCP连接发生异常");
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        //发送认证信息
        ClientAuthMessage msg = new ClientAuthMessage();
        msg.setToken(MyApplication.getToken());
        ctx.channel().write(msg);
        ctx.channel().flush();
        ToastService.toastMsg("连接成功");
        ctx.fireChannelActive();
    }
    @Override
    public void channelInactive(ChannelHandlerContext ctx) throws Exception {
        ctx.fireChannelInactive();
        ToastService.toastMsg("TCP连接断开");
    }
    @Override
    public void userEventTriggered(ChannelHandlerContext ctx, Object evt) throws Exception {
        if (evt instanceof IdleStateEvent) {
            IdleStateEvent e = (IdleStateEvent) evt;
            switch (e.state()) {
                case WRITER_IDLE:
                    ctx.close();
                    break;
                default:
                    break;
            }
        } else {
            super.userEventTriggered(ctx, evt);
        }
    }
    /*定时发送心跳数据*/
    private void startHeartBeatThread(ChannelHandlerContext ctx) {
        try {
            heartBeatTimerLock.lock();
            if (heartBeatTimer!=null) {
                heartBeatTimer.cancel();
            }
            heartBeatTimer = new Timer();
            TimerTask task = new TimerTask() {
                private ChannelHandlerContext ctx;
                public TimerTask init(ChannelHandlerContext ctx) {
                    this.ctx = ctx;
                    return this;
                }
                @Override
                public void run() {
                    //如果连接已经不可用，抛弃这个任务
                    if (ctx.isRemoved()){
                        ctx.channel().close();
                        heartBeatTimer.cancel();
                        return ;
                    }
                    ClientHeartMessage msg = new ClientHeartMessage();
                    ctx.channel().write(msg);
                    ctx.channel().flush();
                }
            }.init(ctx);
            heartBeatTimer.schedule(task, 0, Configure.HEARTBEAT_TIME*1000);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            heartBeatTimerLock.unlock();
        }
    }
}
