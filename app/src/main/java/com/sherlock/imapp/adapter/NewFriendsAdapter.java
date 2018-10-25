package com.sherlock.imapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Switch;
import android.widget.TextView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.activity.NewFriendsActivity;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.FriendReqVO;
import com.sherlock.imapp.presenter.BitMapPresenter;

import java.util.List;

public class NewFriendsAdapter extends ArrayAdapter {
    private int resourceId;

    private NewFriendsActivity activity;
    public NewFriendsAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<FriendReqVO> objects, NewFriendsActivity activity) {
        super(context, resource, objects);
        resourceId = resource;
        this.activity = activity;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final FriendReqVO item = (FriendReqVO) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ImageView imageView = (ImageView) view.findViewById(R.id.headPic);
        BitMapPresenter.showUserHeadPic(MessageConstant.getUserStr(item.getUserId()),item.getHeadPic(),imageView);
        ((TextView) view.findViewById(R.id.name)).setText(item.getName());
        ((TextView) view.findViewById(R.id.sex)).setText(OtherConstant.getSex(item.getSex()).getName());

        if (item.getFromUserId()!= MyApplication.getUserId()
                && item.getStatus()== MessageConstant.AddFriendConfirmStatusEnum.unverified.getIndex()){
            view.findViewById(R.id.btn_accept).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.acceptRequest(item.getId(), MessageConstant.AddFriendConfirmStatusEnum.agree);
                }
            });
            view.findViewById(R.id.btn_reject).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.acceptRequest(item.getId(), MessageConstant.AddFriendConfirmStatusEnum.reject);
                }
            });
        } else {
            String content = item.getStatus()==MessageConstant.AddFriendConfirmStatusEnum.agree.getIndex() ? "验证通过"
                    : (item.getStatus()==MessageConstant.AddFriendConfirmStatusEnum.reject.getIndex()?"已拒绝" : "等待验证");
            LinearLayout layout = view.findViewById(R.id.layout_status);
            layout.removeAllViews();
            TextView view1 = new TextView(getContext());
            view1.setText(content);
            layout.addView(view1);
        }

        return view;
    }
}
