package com.sherlock.imapp.activity;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.sherlock.imapp.Configure;
import com.sherlock.imapp.R;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.utils.StringUtil;

public class ChangeEnvironmentActivity extends BaseActivity implements View.OnClickListener{
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_environment);

        ((EditText)findViewById(R.id.ip)).setText(Configure.getInstance().getIp());
        ((EditText)findViewById(R.id.http_port)).setText(Configure.getInstance().getHttpPort()+"");
        ((EditText)findViewById(R.id.socket_port)).setText(Configure.getInstance().getSocketPort()+"");

        findViewById(R.id.confirm).setOnClickListener(this);
        findViewById(R.id.cancel).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.confirm:
                String ip = ((EditText)findViewById(R.id.ip)).getText().toString();
                String httpPort = ((EditText)findViewById(R.id.http_port)).getText().toString();
                String socketPort = ((EditText)findViewById(R.id.socket_port)).getText().toString();
                if (StringUtil.isBlank(ip) || StringUtil.isBlank(httpPort) || StringUtil.isBlank(socketPort)) {
                    ToastService.toastMsg("请填写完整");
                    return;
                }
                Configure.setHttpBaseUrl(ip, Integer.valueOf(httpPort), Integer.valueOf(socketPort));
                finish();
                break;
            case R.id.cancel:
                finish();
                break;
        }
    }
}
