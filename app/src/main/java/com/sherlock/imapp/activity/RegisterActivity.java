package com.sherlock.imapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.sherlock.imapp.R;
import com.sherlock.imapp.adapter.SexAdapter;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.constant.OtherConstant;
import com.sherlock.imapp.entity.Sex;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.RegisterPresenter;
import com.sherlock.imapp.utils.BitMapUtil;
import com.sherlock.imapp.utils.FileUtil;
import com.sherlock.imapp.utils.StringUtil;
import com.sherlock.imapp.view.RegisterView;

import java.io.File;

/**
 * Created by Administrator on 2018/5/11 0011.
 */

public class RegisterActivity extends ImgBaseActivity implements RegisterView,View.OnClickListener {

    private Spinner spinner;
    //头像框
    private ImageView iv_photo;
    //头像图片
    private Bitmap headPic;

    SexAdapter adapter;

    private RegisterPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        spinner = (Spinner) findViewById(R.id.sex);
        iv_photo = (ImageView) findViewById(R.id.headPic);
        iv_photo.setOnClickListener(this);

        adapter = new SexAdapter(this, R.layout.item_sex, OtherConstant.sexArray);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(new SexOnItemSelectedListener());
        presenter = new RegisterPresenter(this);

        findViewById(R.id.resister_btn).setOnClickListener(this);
        FileUtil.createFileIfNotExist(FileUtil.picPath , FileUtil.regisgerHeadPicName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headPic:
                showChangeHeadPicDialog();
                break;
            case R.id.btn_resister: {
                TextView accountView = (TextView) findViewById(R.id.account);
                TextView pwdView = (TextView) findViewById(R.id.old_pwd);
                TextView retypePwdView = (TextView) findViewById(R.id.new_pwd);
                TextView nameView = (TextView) findViewById(R.id.name);
                String account = accountView.getText().toString();
                String pwd = pwdView.getText().toString();
                String retypePwd = retypePwdView.getText().toString();
                String name = nameView.getText().toString();
                Sex sex = (Sex) spinner.getSelectedItem();
                if (headPic == null) {
                    ToastService.toastMsg("请选择头像");
                    return ;
                }
                if (StringUtil.isBlank(pwd) || StringUtil.isBlank(retypePwd)) {
                    ToastService.toastMsg("请输入密码x2");
                    return;
                }
                if (!pwd.equals(retypePwd)) {
                    ToastService.toastMsg("两次输入密码不一致");
                    return;
                }
                presenter.register(account, pwd, name, sex.getSex(),new File(FileUtil.regisgerHeadPicNamePath));
                break;
            }
        }
    }

    public static class SexOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    @Override
    public void registerSuccess(UserVO user) {
        runOnUiThread(new Runnable() {
            private UserVO user;

            @Override
            public void run() {
                Toast.makeText(RegisterActivity.this, "注册成功token:" + user.getToken() + ",id:" + user.getId(), Toast.LENGTH_LONG).show();
                changePage2Im();
            }

            public Runnable setUser(UserVO user) {
                this.user = user;
                return this;
            }
        }.setUser(user));
        finish();
    }

    private void changePage2Im() {
        Intent intent = new Intent(RegisterActivity.this, ImActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }

    @Override
    protected void showImage(){
        headPic = BitMapUtil.decodeByFile(FileUtil.regisgerHeadPicNamePath);
        iv_photo.setImageBitmap(headPic);
    }

}
