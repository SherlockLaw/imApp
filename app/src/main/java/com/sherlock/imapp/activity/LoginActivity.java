package com.sherlock.imapp.activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Environment;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import android.widget.Toast;

import com.sherlock.imapp.R;
import com.sherlock.imapp.adapter.AccountAdapter;
import com.sherlock.imapp.db.AccountDBService;
import com.sherlock.imapp.entity.AccountVO;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.LoginPresenter;
import com.sherlock.imapp.utils.FileUtil;
import com.sherlock.imapp.view.LoginView;

import java.util.ArrayList;
import java.util.List;

public class LoginActivity extends BaseActivity implements LoginView,View.OnClickListener {

    private LoginPresenter presenter;

    private AutoCompleteTextView autoCompleteTextView;
    private AccountAdapter adapter;

    private List<AccountVO> list = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        presenter = new LoginPresenter(this);
        ActivityCompat.requestPermissions(this, new String[]{android
                .Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);

        autoCompleteTextView = (AutoCompleteTextView) findViewById(R.id.auto_account);
        autoCompleteTextView.setThreshold(1);
//        adapter = new AccountAdapter(this,R.layout.item_account, list);
//        autoCompleteTextView.setAdapter(adapter);
        findViewById(R.id.login_btn).setOnClickListener(this);
        findViewById(R.id.resister_btn).setOnClickListener(this);
        findViewById(R.id.update_pwd).setOnClickListener(this);
    }
    @Override
    protected void onStart(){
        super.onStart();
        adapter = new AccountAdapter(this,R.layout.item_account, AccountDBService.getAccountList());
        autoCompleteTextView.setAdapter(adapter);
//        list.clear();
//        list.addAll(AccountDBService.getAccountList());
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (grantResults.length==0) return;

        switch (requestCode) {
            case 1:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    //创建项目基础文件夹
                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
                        FileUtil.createDir(FileUtil.basePath);
                    }
                    break;
                }
        }
    }
    @Override
    public void onClick(View v){
        TextView accountView = (TextView)findViewById(R.id.auto_account);
        TextView pwdView = (TextView)findViewById(R.id.old_pwd);
        String account = accountView.getText().toString();
        String pwd = pwdView.getText().toString();
        switch(v.getId()) {
            case R.id.login_btn:
                presenter.login(account,pwd);
                break;
            case R.id.resister_btn:
                changePage2Register();
                break;
            case R.id.update_pwd:
                changePage2UpdatePwd();
                break;
        }
    }

    @Override
    public void showProgress() {
        this.setVisible(true);
    }

    @Override
    public void hideProgress() {
        this.setVisible(false);
    }

    @Override
    public void loginSuccess(final UserVO user) {
        runOnUiThread( new Runnable() {
            @Override
            public void run() {
                Toast.makeText(LoginActivity.this,"登录成功token:"+user.getToken()+",id:"+user.getId(),Toast.LENGTH_LONG).show();
                changePage2Im();
            }
        });
    }

    private void changePage2Register (){
        Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    private void changePage2UpdatePwd (){
        Intent intent = new Intent(LoginActivity.this, UpdatePwdActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    private void changePage2Im (){
        Intent intent = new Intent(LoginActivity.this, ImActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    @Override
    public void onBackPressed() {
//        super.onBackPressed();
    }
}
