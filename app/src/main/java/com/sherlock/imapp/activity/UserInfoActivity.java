package com.sherlock.imapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.fragment.ContactFragment;
import com.sherlock.imapp.presenter.BitMapPresenter;
import com.sherlock.imapp.presenter.UserInfoPresenter;
import com.sherlock.imapp.view.UserInfoView;

import org.parceler.Parcels;

public class UserInfoActivity extends BaseActivity implements UserInfoView,View.OnClickListener{
    private UserInfoPresenter presenter;
    private UserVO userVO;
    public static final String userVOKey = "userVO";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_info);
        presenter = new UserInfoPresenter(this);
        userVO= Parcels.unwrap(getIntent().getParcelableExtra(userVOKey));
        ((TextView)findViewById(R.id.account)).setText(userVO.getAccount());
        ((TextView)findViewById(R.id.name)).setText(userVO.getName());
        ((TextView)findViewById(R.id.sex)).setText(OtherConstant.getSex(userVO.getSex()).getName());
        findViewById(R.id.btn_send_msg).setOnClickListener(this);
        findViewById(R.id.btn_delete_friend).setOnClickListener(this);

        ImageView headPicView = (ImageView)findViewById(R.id.headPic);
        //加载图片
        BitMapPresenter.showUserHeadPic(MessageConstant.getUserStr(userVO.getId()),userVO.getHeadPic(),headPicView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_send_msg:
                changePage2Conversation();
                break;
            case R.id.btn_delete_friend:
                presenter.deleteFriend(userVO);
                break;
        }
    }

    private void changePage2Conversation() {
        Conversation conversation = new Conversation();
        conversation.setName(userVO.getName());
        conversation.setGtype(MessageConstant.GTypeEnum.user.getIndex());
        conversation.setGid(userVO.getId());
        conversation.setPic(userVO.getHeadPic());
        ContactFragment.changePage2Conversation (UserInfoActivity.this,conversation);
    }

    @Override
    public void deleteFriendSuccess(UserVO userVO) {
        ToastService.toastMsg("删除成功");
        finish();
    }
}
