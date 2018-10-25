package com.sherlock.imapp.fragment;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.activity.LoginActivity;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.TCPClient;
import com.sherlock.imapp.presenter.BitMapPresenter;

/**
 * Created by Administrator on 2018/5/12 0012.
 */

public class MeFragment extends Fragment{

    private ImageView headPicView;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_me, container, false);
        UserVO userVO = MyApplication.getUser();
        headPicView = (ImageView) view.findViewById(R.id.headPic);
        ((TextView)view.findViewById(R.id.account)).setText(userVO.getAccount());
        ((TextView)view.findViewById(R.id.name)).setText(userVO.getName());
        ((TextView)view.findViewById(R.id.sex)).setText(OtherConstant.getSex(userVO.getSex()).getName());
        ((Button)view.findViewById(R.id.logout)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                logout();
            }
        });
        //加载图片
        BitMapPresenter.showUserHeadPic(MessageConstant.getUserStr(userVO.getId()),userVO.getHeadPic(),headPicView);
        return view;
    }

    public void logout(){
        //断开连接
        TCPClient.getInstance().disconnect();
        //清除缓存用户信息
        MyApplication.getInstance().logout();
        changePage2Login();
        //销毁activity
        getActivity().finish();
    }
    /**
     * 切换到登录页面
     */
    public void changePage2Login (){
        Activity activity = getActivity();
        Intent intent = new Intent(activity, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }
}
