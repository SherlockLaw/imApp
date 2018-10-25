package com.sherlock.imapp.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.adapter.SexAdapter;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.constant.MessageConstant;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.Sex;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.BitMapPresenter;
import com.sherlock.imapp.presenter.UpdatePwdPresenter;
import com.sherlock.imapp.presenter.UpdateUserPresenter;
import com.sherlock.imapp.utils.BitMapUtil;
import com.sherlock.imapp.utils.FileUtil;
import com.sherlock.imapp.view.UpdateUserView;

import java.io.File;

public class UpdateUserActivity extends ImgBaseActivity implements UpdateUserView, View.OnClickListener{
    private Spinner spinner;
    //头像框
    private ImageView headPicView;
    //头像图片
    private Bitmap headPic;

    SexAdapter adapter;
    private UpdateUserPresenter presenter;
    private EditText nameView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_user);
        spinner = (Spinner) findViewById(R.id.sex);
        headPicView = (ImageView) findViewById(R.id.headPic);
        headPicView.setOnClickListener(this);

        UserVO userVO = MyApplication.getUser();
        nameView = findViewById(R.id.name);
        nameView.setText(userVO.getName());

        adapter = new SexAdapter(this, R.layout.item_sex, OtherConstant.sexArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setSelection(OtherConstant.getSexIndex(userVO.getSex()));

        findViewById(R.id.btn_update_user).setOnClickListener(this);
        presenter = new UpdateUserPresenter(this);

        //加载图片
        BitMapPresenter.showUserHeadPic(userVO.getHeadPic(),headPicView);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headPic:
                showChangeHeadPicDialog();
                break;
            case R.id.btn_update_user: {
                String name = nameView.getText().toString();
                Sex sex = (Sex) spinner.getSelectedItem();
                presenter.update(name, sex.getSex(), new File(FileUtil.regisgerHeadPicNamePath));
                break;
            }
        }
    }

    @Override
    public void updateSuccess() {
        ToastService.toastMsg("修改成功");
        finish();
    }

    @Override
    protected void showImage() {
        headPic = BitMapUtil.decodeByFile(FileUtil.regisgerHeadPicNamePath);
        headPicView.setImageBitmap(headPic);
    }
}
