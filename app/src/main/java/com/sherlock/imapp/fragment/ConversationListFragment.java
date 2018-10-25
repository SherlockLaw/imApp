package com.sherlock.imapp.fragment;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.sherlock.imapp.MyApplication;
import com.sherlock.imapp.R;
import com.sherlock.imapp.activity.ImActivity;
import com.sherlock.imapp.adapter.ConversationListAdapter;
import com.sherlock.imapp.entity.Conversation;
import com.sherlock.imapp.presenter.ConversationListPresenter;
import com.sherlock.imapp.view.ConversationListView;

import java.lang.ref.WeakReference;
import java.util.List;

/**
 * Created by Administrator on 2018/5/9 0009.
 */

public class ConversationListFragment extends Fragment implements ConversationListView{

    private ListView listView;
    private View view;
    private ConversationListPresenter presenter;

    private ImActivity activity;
    public void setActivity(ImActivity activity){
        this.activity = activity;
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_conversation, container, false);

        listView = (ListView) view.findViewById(R.id.list_conversation);
        presenter = new ConversationListPresenter(this);
        MyApplication.getInstance().conversationListFragment = new WeakReference<ConversationListFragment>(this);
        return view;
    }

    public void getConversationList(){
        presenter.getConversationList();
    }

    public void showConversationList(){
        presenter.showConversationList();
    }

    @Override
    public void getConversationListSuccess(final int totalUnreadCount, List<Conversation> list) {
        Activity activity1 = getActivity();
        if (activity1!=null) {
            activity1.runOnUiThread( new Runnable() {
                private List<Conversation> list;
                @Override
                public void run() {
                    ConversationListAdapter adapter = new ConversationListAdapter(getActivity(), R.layout.item_conversation,list);
                    listView = (ListView) view.findViewById(R.id.list_conversation);
                    listView.setAdapter(adapter);
                    listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            //获得选中项的HashMap对象
                            Conversation conversation = (Conversation) listView.getItemAtPosition(position);
                            ContactFragment.changePage2Conversation(getActivity(),conversation);
                        }
                    });
                    //显示总未读数
                    activity.showConversationTotalUnreadCount(totalUnreadCount);
                }
                public Runnable setUserList(List<Conversation> list) {
                    this.list = list;
                    return this;
                }
            }.setUserList(list));
        }
    }
}
