package com.sherlock.imapp.activity;

import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.common.ToastService;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.netty.entity.ImMessage;
import com.sherlock.imapp.presenter.ConversationPresenter;
import com.sherlock.imapp.adapter.MessageListAdapter;
import com.sherlock.imapp.view.ConversationView;

import org.parceler.Parcels;

import java.lang.ref.WeakReference;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/5/8 0008.
 */

public class ConversationActivity extends BaseActivity implements View.OnClickListener, ConversationView {
    private ConversationPresenter presenter;

    public static final String conversionKey = "conversation";

    private Conversation conversation;

    private List<ImMessage> msgList = new LinkedList<>();

    private MessageListAdapter adapter;

    private EditText msgText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversation);
        presenter = new ConversationPresenter(this);
        conversation = Parcels.unwrap(getIntent().getParcelableExtra(conversionKey));
        if (conversation !=null) {
            TextView textView = (TextView) findViewById(R.id.conversation_toUserName);
            textView.setText(conversation.getName());
        }
        ((Button)findViewById(R.id.sendmsg_btn)).setOnClickListener(this);
        ((Button)findViewById(R.id.sendImg_btn)).setOnClickListener(this);
        msgText = (EditText) findViewById(R.id.sendmsg_edit);

        //设置消息内容显示
        adapter = new MessageListAdapter(ConversationActivity.this, R.layout.item_message, msgList);

        presenter.getUserVOMap(conversation);
        ListView listView = (ListView) findViewById(R.id.list_message);
        listView.setAdapter(adapter);
        listView.setTranscriptMode(ListView.TRANSCRIPT_MODE_ALWAYS_SCROLL);
        //设置本活动框的弱引用，以供消息接受时，判断是否直接显示在这个页面
        MyApplication.getInstance().conversationActivity = new WeakReference<ConversationActivity>(this);
    }

    private static final int selectImgRequest=1;
    private static final int sendImgRequest=2;
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.sendmsg_btn:
                String content = msgText.getText().toString();
                presenter.sendTextMsg(conversation.getGtype(),conversation.getGid(),content);
                break;
            case R.id.sendImg_btn:
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                startActivityForResult(i, sendImgRequest);
                break;
            default:
                break;
        }
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        switch (requestCode) {
            case sendImgRequest:
                if (resultCode == RESULT_OK) {
                    Uri selectedImage = data.getData();
                    String[] filePathColumn = { MediaStore.Images.Media.DATA };
                    Cursor cursor = getContentResolver().query(selectedImage,filePathColumn, null, null, null);
                    cursor.moveToFirst();
                    int columnIndex = cursor.getColumnIndex(filePathColumn[0]);
                    String picturePath = cursor.getString(columnIndex);
                    cursor.close();
                    //上传图片
                    presenter.sendImgMsg(conversation.getGtype(),conversation.getGid(),picturePath);
                }
                break;
        }
    }

    @Override
    public void getServerImMessageSuccess(List<ImMessage> list) {
        msgList.addAll(list);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void sendTextMsgSuccess(ImMessage msg) {
        showImMessage(msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                msgText.setText("");
            }
        });
    }

    @Override
    public void sendTextMsgfailure(ImMessage msg) {
        ToastService.toastMsg("发送失败");
    }

    @Override
    public void showImMessage(ImMessage msg) {
        msgList.add(msg);
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public void getUserVOMapSuccess(Map<Integer, UserVO> userVOMap) {
        adapter.setUserVOMap(userVOMap);
        //从数据库/服务器获取历史消息
        presenter.getServerImMessage(conversation.getGtype(),conversation.getGid());
    }

    public Conversation getConversation(){
        return conversation;
    }
}