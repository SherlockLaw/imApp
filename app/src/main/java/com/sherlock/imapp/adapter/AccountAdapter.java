package com.sherlock.imapp.adapter;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.sherlock.imapp.R;
import com.sherlock.imapp.entity.AccountVO;
import com.sherlock.imapp.utils.StringUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2018/6/15 0015.
 */

public class AccountAdapter extends ArrayAdapter {
    private int resourceId;
    private List<AccountVO> data = new ArrayList<>();
    private List<AccountVO> list;

    public AccountAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List<AccountVO> objects) {
        super(context, resource, objects);
        resourceId = resource;
        list = objects;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        AccountVO item = (AccountVO) getItem(position);
        View view = LayoutInflater.from(getContext()).inflate(resourceId, null);
        ((TextView) view.findViewById(R.id.account)).setText(item.getAccount());
        return view;
    }

    @Override
    public Filter getFilter() {
        Filter filter = new Filter() {
            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();
                List<AccountVO> filterData = new ArrayList<>();
                String content = constraint==null ? "" : constraint.toString();
                if (!StringUtil.isBlank(content)){
                    for(AccountVO vo : list){
                        if (vo.getAccount().startsWith(content)) {
                            filterData.add(vo);
                        }
                    }
                }
                results.values = filterData;
                results.count = filterData.size();
                return results;
            }

            @Override
            protected void publishResults(CharSequence constraint, FilterResults results) {
                data = (ArrayList)results.values;
                notifyDataSetChanged();
            }
            @Override
            public CharSequence convertResultToString(Object resultValue) {
                if (resultValue==null) {
                    return "";
                }
                AccountVO vo = (AccountVO)resultValue;
                return vo.getAccount();
            }
        };
        return filter;
    }
    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }
}
