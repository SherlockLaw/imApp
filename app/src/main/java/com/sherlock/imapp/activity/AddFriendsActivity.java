package com.sherlock.imapp.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sherlock.imapp.R;
import com.sherlock.imapp.adapter.SearchUserListAdapter;
import com.sherlock.imapp.entity.UserVO;
import com.sherlock.imapp.presenter.AddFriendsPresenter;
import com.sherlock.imapp.view.AddFriendsView;

import java.util.List;

/**
 * Created by Administrator on 2018/5/6 0006.
 */

public class AddFriendsActivity extends BaseActivity implements AddFriendsView,View.OnClickListener {
    private ListView listView;
    private AddFriendsPresenter presenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_friend);
        presenter = new AddFriendsPresenter(this);
        Button button = (Button) findViewById(R.id.btn_searchusers);
        button.setOnClickListener(this);
    }

    @Override
    public void searchUsersSuccess(final List<UserVO> list) {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                SearchUserListAdapter adapter = new SearchUserListAdapter(AddFriendsActivity.this, R.layout.item_searchusers, list);
                listView = (ListView) findViewById(R.id.user_list);
                listView.setAdapter(adapter);
                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        //获得选中项的HashMap对象
                        UserVO toUser = (UserVO) listView.getItemAtPosition(position);
                        presenter.addFriends(toUser);
                    }
                });
            }
        });
    }

    @Override
    public void addFriendsSuccess() {
        runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(AddFriendsActivity.this, "已发送请求",
                        Toast.LENGTH_LONG).show();
            }
        });

    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_searchusers:
                TextView nameView = (TextView) findViewById(R.id.search_name);
                String keyword = nameView.getText().toString();
                presenter.searchUsers(keyword);
                break;
        }
    }
}
