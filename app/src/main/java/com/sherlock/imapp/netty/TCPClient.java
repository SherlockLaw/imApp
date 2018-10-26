package com.sherlock.imapp.netty;

import android.util.Log;

import com.sherlock.imapp.Configure;
import com.sherlock.imapp.common.ThreadPoolService;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.manager.CommonManager;
import com.sherlock.imapp.netty.codec.ImMessageCodec;

import java.net.InetSocketAddress;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelInitializer;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioSocketChannel;
import io.netty.handler.timeout.IdleStateHandler;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class TCPClient {
    private static TCPClient instance = new TCPClient();
    private Bootstrap b = new Bootstrap();
    private EventLoopGroup group = new NioEventLoopGroup();

    private volatile boolean connecting;
    private Semaphore semaphore = new Semaphore(1);
    private Lock lock = new ReentrantLock();
    private static BlockingQueue<Integer> threadQueue = new ArrayBlockingQueue<>(1);

    public static TCPClient getInstance() {
        return instance;
    }

    private TCPClient() {
        init();
    }

    public void init() {
        b.group(group).channel(NioSocketChannel.class).remoteAddress(new InetSocketAddress(Configure.getInstance().getIp(), Configure.getInstance().getSocketPort()))
                .handler(new ChannelInitializer<SocketChannel>() {
                    @Override
                    protected void initChannel(SocketChannel ch) throws Exception {
                        ch.pipeline()
                                .addLast(new ImMessageCodec())
                                //设置心跳检测
                                .addLast(new IdleStateHandler(0, Configure.MAX_IDLETIME, 0, TimeUnit.SECONDS))
                                .addLast(new TCPClientInboundHandler())
                                .addLast(new TCPClientOutboundHandler());
                    }
                });

        //可采用事件监听的方式监听连接结果 ChannelFutureListener
        Runnable runnable = new Runnable() {
            private int[] reConnectTime = new int[] {3000,3000,3000,3000};
            private int reConnectTimes;
            @Override
            public void run() {
                while (true) {
                    try {
                        threadQueue.take();
                        //之前已经有线程在连接过程中
                        lock.lock();
                        if(connecting){
                            lock.unlock();
                            return;
                        }
                        connecting = true;
                        lock.unlock();
                        while (connecting) {
                            try {
                                f = b.connect().sync();
                                //成功连接之后设置重连次数为0
                                reConnectTimes = 0;
                                f.channel().closeFuture().sync();
                            } catch (Exception e) {
                                Log.e("连接断开","连接无法开启", e);
                                e.printStackTrace();
                                //如果连接无法打开是因为主动断开连接，不显示信息
                                if (connecting) {
                                    ToastService.toastMsg("无法打开TCP连接");
                                }
                            } finally {
                                try {
                                    Thread.sleep(reConnectTime[reConnectTimes]);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                                //重连次数超过三次不再增加
                                if (reConnectTimes==3) {
                                    continue;
                                }
                                reConnectTimes++;
                            }
                        }
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        ThreadPoolService.execute(runnable);
    }

    private ChannelFuture f;

    public void connect() {
        threadQueue.clear();
        //断开之前的连接
        ChannelFuture future = null;
        //采用循环的方式保证断开
        do {
            future = disconnect0();
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        } while (future != null && !future.isSuccess());

        threadQueue.add(1);
    }

    private ChannelFuture disconnect0(){
        lock.lock();
        threadQueue.clear();
        connecting = false;
        lock.unlock();
        ChannelFuture future = null;
        if (f!=null && f.channel().isOpen()) {
            future = f.channel().close();
        }
        return future;
    }
    public void disconnect() {
        disconnect0();
//        if (group!=null){
//            try {
//                group.shutdownGracefully().sync();
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 登录成功后的操作
     */
    public static void operationsAfterLoginSuccess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //1、联系人刷新
                CommonManager.getFriendListFromServerAndSave();
                CommonManager.getGroupListFromServerAndSave();
            }
        };
        ThreadPoolService.execute(runnable);
    }
    /**
     * 连接成功之后的一系列操作,必须确保不抛出异常
     */
    public static void operationsAfterConnectSuccess(){
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //2、未读刷新
                CommonManager.getUnreadCountMapFromServerAndSave();
                //3、会话刷新（在会话页面时）
                CommonManager.getConversationOffLineMsgAndSave(true);
                //4、指令消息
                CommonManager.getOfflineOrderMessageAndSave();
            }
        };
        ThreadPoolService.execute(runnable);
    }
}
