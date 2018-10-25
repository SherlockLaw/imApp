package com.sherlock.imapp.adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.presenter.BitMapPresenter;
import com.sherlock.imapp.utils.BitMapUtil;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import q.rorbin.badgeview.QBadgeView;

/**
 * Created by Administrator on 2018/5/10 0010.
 */

public class ConversationListAdapter extends ArrayAdapter {

    private final int resourceId;

    private static final SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");

    public ConversationListAdapter(Context context, int resourceId, List<Conversation> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Conversation item= (Conversation) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);

        ImageView imageView = (ImageView)view.findViewById(R.id.headPic);
        ((TextView) view.findViewById(R.id.name)).setText(item.getName());
        ((TextView) view.findViewById(R.id.lastMsg)).setText(item.getLastMsg());
        ((TextView) view.findViewById(R.id.lastMsgTime)).setText(sdf.format(new Date(item.getLastMsgTime())));
        QBadgeView badgeView = new QBadgeView(view.getContext());
        badgeView.bindTarget(imageView).setBadgeNumber(item.getUnreadCount());
        badgeView.setForegroundGravity(Gravity.LEFT);

        BitMapPresenter.showUserHeadPic(MessageConstant.getStr(item.getGtype(),item.getGid()),item.getPic(),imageView);
        return view;
    }
}
