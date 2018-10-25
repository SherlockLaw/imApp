package com.sherlock.imapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.Group;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.BitMapPresenter;

import java.util.List;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class GroupListAdapter extends ArrayAdapter {

    private final int resourceId;

    public GroupListAdapter(Context context, int resourceId, List<Group> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Group item = (Group) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ((TextView) view.findViewById(R.id.name)).setText(item.getName());
        ImageView imageView = (ImageView) view.findViewById(R.id.headPic);
        BitMapPresenter.showUserHeadPic(item.getHeadPic(), imageView);
        return view;
    }
}