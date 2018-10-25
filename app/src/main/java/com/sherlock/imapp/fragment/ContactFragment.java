package com.sherlock.imapp.fragment;

import android.app.Activity;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.activity.ConversationActivity;
import com.sherlock.imapp.activity.ImActivity;
import com.sherlock.imapp.adapter.MyFragmentPagerAdapter;
import com.sherlock.imapp.entity.Conversation;

import org.parceler.Parcels;

import java.util.ArrayList;

/**
 * Created by Administrator on 2018/6/3 0003.
 */

public class ContactFragment extends Fragment implements ViewPager.OnPageChangeListener,View.OnClickListener{
    private FriendListFragment friendListFragment;
    private GroupListFragment groupListFragment;

    public void setActivity(ImActivity activity){
        friendListFragment.setActivity(activity);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_contact, container, false);
        initViews(view);
        return view;
    }

    private ViewPager vpager_four;
    private ImageView img_cursor;
    private TextView tv_user;
    private TextView tv_group;

    private ArrayList<Fragment> listViews;
    private int offset = 0;//移动条图片的偏移量
    private int currIndex = 0;//当前页面的编号
    private int bmpWidth;// 移动条图片的长度
    private int one = 0; //移动条滑动一页的距离
    private int two = 0; //滑动条移动两页的距离

    private void initViews(View view) {
        vpager_four = (ViewPager) view.findViewById(R.id.vpager);
        tv_user = (TextView) view.findViewById(R.id.tv_user);
        tv_group = (TextView) view.findViewById(R.id.tv_group);
        img_cursor = (ImageView) view.findViewById(R.id.img_cursor);

        //下划线动画的相关设置：
        bmpWidth = BitmapFactory.decodeResource(getResources(), R.drawable.line).getWidth();// 获取图片宽度
        DisplayMetrics dm = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
        int screenW = dm.widthPixels;// 获取分辨率宽度
        offset = (screenW / 2 - bmpWidth) / 2;// 计算偏移量
        Matrix matrix = new Matrix();
        matrix.postTranslate(offset, 0);
        img_cursor.setImageMatrix(matrix);// 设置动画初始位置
        //移动的距离
        one = offset * 2 + bmpWidth;// 移动一页的偏移量,比如1->2,或者2->3
        two = one * 2;// 移动两页的偏移量,比如1直接跳3

        //往ViewPager填充View，同时设置点击事件与页面切换事件
        listViews = new ArrayList<Fragment>();
        LayoutInflater mInflater = getActivity().getLayoutInflater();
        friendListFragment = new FriendListFragment();
        groupListFragment = new GroupListFragment();
        mInflater.inflate(R.layout.fragment_friend,null,false);
        listViews.add(friendListFragment);
        listViews.add(groupListFragment);

        vpager_four.setAdapter(new MyFragmentPagerAdapter(getFragmentManager(),listViews));
        vpager_four.setCurrentItem(0);          //设置ViewPager当前页，从0开始算

        tv_user.setOnClickListener(this);
        tv_group.setOnClickListener(this);

        vpager_four.addOnPageChangeListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.tv_user:
                vpager_four.setCurrentItem(0);
                break;
            case R.id.tv_group:
                vpager_four.setCurrentItem(1);
                break;
        }
    }

    @Override
    public void onPageSelected(int index) {
        Animation animation = null;
        switch (index) {
            case 0:
                if (currIndex == 1) {
                    animation = new TranslateAnimation(one, 0, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, 0, 0, 0);
                }
                break;
            case 1:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, one, 0, 0);
                } else if (currIndex == 2) {
                    animation = new TranslateAnimation(two, one, 0, 0);
                }
                break;
            case 2:
                if (currIndex == 0) {
                    animation = new TranslateAnimation(offset, two, 0, 0);
                } else if (currIndex == 1) {
                    animation = new TranslateAnimation(one, two, 0, 0);
                }
                break;
        }
        currIndex = index;
        animation.setFillAfter(true);// true表示图片停在动画结束位置
        animation.setDuration(300); //设置动画时间为300毫秒
        img_cursor.startAnimation(animation);//开始动画
    }

    @Override
    public void onPageScrollStateChanged(int i) {

    }

    @Override
    public void onPageScrolled(int i, float v, int i1) {

    }

    /**
     * 切换到会话页面
     * @param conversation
     */
    public static void changePage2Conversation (Activity activity, Conversation conversation){
        Intent intent = new Intent(activity, ConversationActivity.class);
        Bundle mBundle = new Bundle();
        mBundle.putParcelable(ConversationActivity.conversionKey, Parcels.wrap(conversation));
        intent.putExtras(mBundle);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        activity.startActivity(intent);
    }
}
