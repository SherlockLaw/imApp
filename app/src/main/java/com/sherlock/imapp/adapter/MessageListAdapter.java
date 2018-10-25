package com.sherlock.imapp.adapter;

import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.netty.entity.PicMessage;
import com.sherlock.imapp.netty.entity.TextMessage;
import com.sherlock.imapp.presenter.BitMapPresenter;
import com.sherlock.imapp.utils.BitMapUtil;

import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class MessageListAdapter extends ArrayAdapter {
    private final int resourceId;

    private UserVO me = MyApplication.getUser();
    private Map<Integer,UserVO> userVOMap;

    public MessageListAdapter(Context context, int resourceId, List<ImMessage> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }
    public void setUserVOMap(Map<Integer,UserVO> userVOMap) {
        this.userVOMap = userVOMap;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ImMessage  msg = (ImMessage) getItem(position);

        View view;
        if (convertView == null) {
            view = LayoutInflater.from(getContext()).inflate(resourceId, parent, false);
        } else {
            view = convertView;
        }
        ImageView headPicView = (ImageView) view.findViewById(R.id.headPic);
        TextView blankTextView = (TextView) view.findViewById(R.id.blank);
        int padding = 30;
        //本人发的消息
        UserVO curUser;
        if (me.getId()==msg.getFromUserId()) {
            curUser = me;
            headPicView.setForegroundGravity(Gravity.RIGHT);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.message_layoutId);
            //编排顺序
            layout.removeAllViews();
            layout.addView(blankTextView);
            addMessage(layout, R.drawable.mechat, msg);
            headPicView.setPadding(padding,0,0,0);
            layout.addView(headPicView);
        } else {
            curUser = userVOMap.get(msg.getFromUserId());
            headPicView.setForegroundGravity(Gravity.LEFT);
            LinearLayout layout = (LinearLayout) view.findViewById(R.id.message_layoutId);
            //编排顺序
            layout.removeAllViews();
            layout.addView(headPicView);
            headPicView.setPadding(0,0,padding,0);
            addMessage(layout, R.drawable.tochat, msg);
            layout.addView(blankTextView);
        }

        BitMapPresenter.showUserHeadPic(MessageConstant.getUserStr(curUser.getId()),curUser.getHeadPic(),headPicView);
        return view;
    }
    private void addMessage(LinearLayout layout, int chatImgId,ImMessage msg){
        if (msg instanceof TextMessage) {
            TextMessage message = (TextMessage) msg;
            TextView messageView = new TextView(getContext());
            messageView.setBackgroundResource(chatImgId);
            messageView.setText(message.getContent());
            layout.addView(messageView);
        } else  if (msg instanceof PicMessage) {
            PicMessage message = (PicMessage) msg;
            ImageView imageView = new ImageView(getContext());
            String fileName = message.getMid()+".jpg";
            String url = BitMapUtil.getShowPicUrl(message.getUrl(),message.getWidth(),message.getHeigth(),800);
            BitMapPresenter.showImPic(fileName,url,imageView);
            layout.addView(imageView);
        }
    }
}
