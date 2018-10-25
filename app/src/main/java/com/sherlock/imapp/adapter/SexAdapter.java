package com.sherlock.imapp.adapter;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.entity.Sex;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class SexAdapter extends ArrayAdapter{
    private int resourceId;
    public SexAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull Sex[] objects) {
        super(context, resource, objects);
        resourceId= resource;
    }
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Sex item = (Sex) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ((TextView)view.findViewById(R.id.sexText)).setText(item.getName());
        return view;
    }
}
