package com.sherlock.imapp.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.adapter.AddGroupUserListAdapter;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.AddGroupPresenter;
import com.sherlock.imapp.utils.BitMapUtil;
import com.sherlock.imapp.utils.FileUtil;
import com.sherlock.imapp.view.AddGroupView;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by Administrator on 2018/6/3 0003.
 */

public class AddGroupActivity extends BaseActivity implements AddGroupView, View.OnClickListener{
    private ListView listView;
    private AddGroupPresenter presenter;

    private AddGroupUserListAdapter adapter;

    private CountDownLatch countDownLatch = new CountDownLatch(1);

    private ImageView iv_photo;
    private Bitmap headPic;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_group);
        presenter = new AddGroupPresenter(this);
        presenter.getFriendList();
        Button button = (Button) findViewById(R.id.add_group);
        button.setOnClickListener(this);
        iv_photo = (ImageView) findViewById(R.id.headPic);
        iv_photo.setOnClickListener(this);
        FileUtil.createFileIfNotExist(FileUtil.picPath , FileUtil.regisgerGroupHeadPicName);
    }
    private static final int selectImageReuest=1;
    private static final int captureRequest=2;
    private static final int showImageRequest=3;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.add_group:
                try {
                    countDownLatch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String name = ((TextView)findViewById(R.id.name)).getText().toString();
                List<Integer> userIdList =  new ArrayList<>(adapter.getSelectUserIdset());
                presenter.addGroup(new File(FileUtil.regisgerGroupHeadPicNamePath), name, userIdList);
                break;
            case R.id.headPic:
                Intent intent1 = new Intent(Intent.ACTION_PICK, null);
                intent1.setDataAndType(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, "image/*");
                startActivityForResult(intent1, selectImageReuest);
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case selectImageReuest:
                if (resultCode == RESULT_OK) {
                    cropPhoto(data.getData());// 裁剪图片
                }

                break;
            case showImageRequest:
                if (data != null) {
                    headPic = BitMapUtil.decodeByFile(FileUtil.regisgerGroupHeadPicNamePath);
                    iv_photo.setImageBitmap(headPic);
                }
                break;
            default:
                break;

        }
        super.onActivityResult(requestCode, resultCode, data);
    }
    public void cropPhoto(Uri uri) {
        startActivityForResult(Intent.createChooser(RegisterActivity.getCropPhotoIntent(uri,FileUtil.regisgerGroupHeadPicNamePath), "裁剪图片"), showImageRequest);
    }
    @Override
    public void getFriendListSuccess(final List<UserVO> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter = new AddGroupUserListAdapter(AddGroupActivity.this, R.layout.item_addgroupuserlist, list);
                listView = (ListView) findViewById(R.id.user_list);
                listView.setAdapter(adapter);
//                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                    @Override
//                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                        //获得选中项的HashMap对象
//                        UserVO toUser = (UserVO) listView.getItemAtPosition(position);
////                        presenter.addFriends(toUser);
//                    }
//                });
            }
        });
        countDownLatch.countDown();
    }

    @Override
    public void addGroupSuccess() {
        ToastService.toastMsg("添加成功");
        this.finish();
    }
}
