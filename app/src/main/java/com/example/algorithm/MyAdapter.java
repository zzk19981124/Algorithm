package com.example.algorithm;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import java.util.ArrayList;
import java.util.List;

/**
 * @author hello word
 * @desc 作用描述
 * @date 2021/8/17
 */
public class MyAdapter extends BaseAdapter {

    private List<Bean> b = new ArrayList<>();
    private Context mContext;

    public MyAdapter(List<Bean> b, Context context) {
        this.b = b;
        mContext = context;
    }

    @Override
    public int getCount() {
        return b.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null){

        }
        return null;
    }
}
