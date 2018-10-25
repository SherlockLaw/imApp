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

public class RegisterActivity extends BaseActivity implements RegisterView,View.OnClickListener {

    private Spinner spinner;
    //头像框
    private ImageView iv_photo;
    //头像图片
    private Bitmap headPic;

//    private static final String path = Environment.getExternalStorageDirectory() + "/pic/";// sd路径
//    private static final String regisgerHeadPicName = "register_headpic.jpg";

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

        FileUtil.createFileIfNotExist(FileUtil.picPath , FileUtil.regisgerHeadPicName);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.headPic:
                showChangeHeadPicDialog();
                break;
        }
    }

    private class SexOnItemSelectedListener implements AdapterView.OnItemSelectedListener {

        @Override
        public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        }

        @Override
        public void onNothingSelected(AdapterView<?> parent) {

        }
    }

    public void register(View v) {
        TextView accountView = (TextView) findViewById(R.id.account);
        TextView pwdView = (TextView) findViewById(R.id.pwd);
        TextView retypePwdView = (TextView) findViewById(R.id.retype_pwd);
        TextView nameView = (TextView) findViewById(R.id.name);
        String account = accountView.getText().toString();
        String pwd = pwdView.getText().toString();
        String retypePwd = retypePwdView.getText().toString();
        String name = nameView.getText().toString();
        Sex sex = (Sex) spinner.getSelectedItem();
        switch (v.getId()) {
            case R.id.resister:
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
    }

    private void changePage2Im() {
        Intent intent = new Intent(RegisterActivity.this, ImActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
        startActivity(intent);
    }
    private static final int selectImageReuest=1;
    private static final int captureRequest=2;
    private static final int showImageRequest=3;
    //显示选择图片对话框
    private void showChangeHeadPicDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        final AlertDialog dialog = builder.create();
        View view = View.inflate(this, R.layout.dialog_select_photo, null);
        TextView tv_select_gallery = (TextView) view.findViewById(R.id.tv_select_gallery);
        TextView tv_select_camera = (TextView) view.findViewById(R.id.tv_select_camera);
        tv_select_gallery.setOnClickListener(new View.OnClickListener() {// 在相册中选取
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, selectImageReuest);
                dialog.dismiss();
            }
        });
        tv_select_camera.setOnClickListener(new View.OnClickListener() {// 调用照相机
            @Override
            public void onClick(View v) {
                Intent intent2 = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                intent2.putExtra(MediaStore.EXTRA_OUTPUT,
                        Uri.fromFile(new File(Environment.getExternalStorageDirectory(), "headPic.jpg")));
                startActivityForResult(intent2, captureRequest);// 采用ForResult打开
                dialog.dismiss();
            }
        });
        dialog.setView(view);
        dialog.show();
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case selectImageReuest:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case captureRequest:
                if (resultCode == RESULT_OK) {
                    File temp = new File(Environment.getExternalStorageDirectory() + "/headPic.jpg");
                    cropPhoto(Uri.fromFile(temp));// 裁剪图片
                }

                break;
            case showImageRequest:
                if (data != null) {
                    headPic = BitMapUtil.decodeByFile(FileUtil.regisgerHeadPicNamePath);
                    iv_photo.setImageBitmap(headPic);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    /**
     * 调用系统的裁剪功能
     *
     * @param uri
     */
    public void cropPhoto(Uri uri) {
        startActivityForResult(Intent.createChooser(getCropPhotoIntent(uri,FileUtil.regisgerHeadPicNamePath), "裁剪图片"), showImageRequest);
    }

    public static Intent getCropPhotoIntent(Uri uri,String fileSavePath){
        Intent intent = new Intent("com.android.camera.action.CROP");
        intent.setDataAndType(uri, "image/*");
        intent.putExtra("crop", "true");
        // aspectX aspectY 是宽高的比例
        intent.putExtra("aspectX", 1);
        intent.putExtra("aspectY", 1);
        // outputX outputY 是裁剪图片宽高
        intent.putExtra("outputX", 150);
        intent.putExtra("outputY", 150);
//        intent.putExtra("scale",true);
//        intent.putExtra("return-data", true);
        intent.putExtra("return-data", false);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(new File(fileSavePath)));
        intent.putExtra("outputFormat", Bitmap.CompressFormat.JPEG.toString());
        intent.putExtra("noFaceDetection", true);
        return intent;
    }

//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        switch (requestCode) {
//            case 1:
//                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    //创建文件夹
//                    if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//                        FileUtil.createFileIfNotExist(FileUtil.picPath , FileUtil.regisgerHeadPicName);
//                    }
//                    break;
//                }
//        }
//    }
}
