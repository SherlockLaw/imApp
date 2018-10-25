package com.sherlock.imapp.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.BitMapPresenter;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by Administrator on 2018/6/6 0006.
 */

public class AddGroupUserListAdapter  extends ArrayAdapter {
    private final int resourceId;
    private Set<Integer> selectUserIdset = new HashSet<>();

    public AddGroupUserListAdapter(Context context, int resourceId, List<UserVO> objects) {
        super(context, resourceId, objects);
        this.resourceId = resourceId;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        UserVO item= (UserVO) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ((TextView) view.findViewById(R.id.name)).setText(item.getName());
        ((TextView) view.findViewById(R.id.sex)).setText(OtherConstant.getSex(item.getSex()).getName());

        CheckBox checkBox = (CheckBox) view.findViewById(R.id.checkBox);

        //设置选择框
        checkBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            private int id;
            public CompoundButton.OnCheckedChangeListener init(int id) {
                this.id = id;
                return this;
            }
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked){
                    selectUserIdset.add(id);
                }else{
                    selectUserIdset.remove(id);
                }
            }
        }.init(item.getId()));

        ImageView imageView = (ImageView) view.findViewById(R.id.headPic);
        BitMapPresenter.showUserHeadPic(item.getHeadPic(),imageView);
        return view;
    }

    public Set<Integer> getSelectUserIdset() {
        return selectUserIdset;
    }
}
