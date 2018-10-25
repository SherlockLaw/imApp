package com.sherlock.imapp;

import java.nio.charset.Charset;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class Configure {
//    private static final String INTENT_IP = "192.168.0.5";
    private static final String INTENT_IP = "116.85.58.123";
    private static final int INTENT_PORT = 8088;

    private static final int HTTP_PORT = 18081;

    ///////////////////TCP的属性值------------------------
    public static final int HEAD_LENGTH = 10;
    public static final int POSITION_LENGTH = 6;

    public final static boolean IS_BIG_EDIAN = true;//ture为高尾端

    public final static int START_FLAG = 0x55aa66bb;

    public final static byte MSG_VER = 1;//协议版本1
    public final static byte SEPERATOR = '\0';//报文内容分隔标志
    public final static byte MSG_CLIENT = 1;//客户端上传信息
    public final static byte MSG_SERVER = 71;

    public final static byte MSG_AUTH = 2;//客户端注册成功消息
    public final static byte MSG_AUTH_BACK = 3;//注册成功或者失败消息

    public final static byte MSG_ADDFRIEND_REQUEST = 4;//添加好友消息
    public final static byte MSG_ADDGROUP = 5;//添加好友消息
    public final static byte MSG_ADDFRIEND_CONFIRM = 6;//添加好友消息的确认消息
    public final static byte MSG_DELFRIEND = 7;//删除好友消息
    public final static byte MSG_CLIENT_HEARTBEAT = 102;//客户端心跳消息

    public final static byte MSG_IM = 10;//im消息
    public final static byte MSG_CONVERSATION_ORDER = 11;//指令消息
    public final static byte MSG_ACK = 12;//客户端确认消息
    public final static byte MSG_READ = 13;//客户端已读确认消息

    public final static byte MSG_OFFLINE = 14;//客户端离线消息，用以通知客户端拉取离线

    public final static byte MSG_CONTROL=30;//操控主机发送的操控消息
    public final static byte MSG_PIC=50;//被操控主机返回的图片消息

    public final static String DATA_FROMAT = "YYYY-MM-dd HH:mm:SS";

    public final static String CHARSET_NAME = "UTF-8";

    public final static Charset CHARSET = Charset.forName(CHARSET_NAME);

    public final static int MAX_IDLETIME = 20;

    //客户端独有
    public final static int HEARTBEAT_TIME = 5;
    ///////////////////TCP的属性值------------------------

    private static String baseUrl = getBaseUrl(INTENT_IP, HTTP_PORT);

    public static String getBaseUrl(String ip, int port){
        String url = "http://"+ip+":"+port;
        return url;
    }
    public static String getIntentIp() {
        return INTENT_IP;
    }

    public static int getIntentPort() {
        return INTENT_PORT;
    }

    public static void setBaseUrl(String ip, int port){
        baseUrl = getBaseUrl(ip, port);
    }

    public static String getBaseUrl() {
        return baseUrl;
    }
}
