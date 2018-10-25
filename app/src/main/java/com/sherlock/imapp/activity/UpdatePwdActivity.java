package com.sherlock.imapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.presenter.UpdatePwdPresenter;
import com.sherlock.imapp.view.UpdatePwdView;

public class UpdatePwdActivity extends BaseActivity implements UpdatePwdView,View.OnClickListener{
    private UpdatePwdPresenter presenter;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_pwd);
        findViewById(R.id.btn_update_pwd).setOnClickListener(this);
        presenter = new UpdatePwdPresenter(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_update_pwd: {
                String account = ((TextView)findViewById(R.id.account)).getText().toString();
                String oldPwd = ((TextView)findViewById(R.id.old_pwd)).getText().toString();
                String newPwd = ((TextView)findViewById(R.id.new_pwd)).getText().toString();
                String retypeNewPwd = ((TextView)findViewById(R.id.retype_new_pwd)).getText().toString();
                presenter.updatePwd(account, oldPwd, newPwd, retypeNewPwd);

                break;
            }
        }
    }

    @Override
    public void updatePwdSuccess() {
        ToastService.toastMsg("修改成功");
        finish();
    }
}
