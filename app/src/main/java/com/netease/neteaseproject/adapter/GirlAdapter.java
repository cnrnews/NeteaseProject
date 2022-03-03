package com.netease.neteaseproject.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.netease.neteaseproject.R;
import com.netease.neteaseproject.bean.Girl;

import java.util.List;

public class GirlAdapter extends BaseAdapter {

    private LayoutInflater inflater;
    private List<Girl> girls;

    public GirlAdapter(Context context, List<Girl> girls) {
        // this.inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        this.inflater = LayoutInflater.from(context);
        this.girls = girls;
    }

    @Override
    public int getCount() {
        return girls.size();
    }

    @Override
    public Object getItem(int position) {
        return girls.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = inflater.inflate(R.layout.item, null);

        Girl girl = girls.get(position);

        ImageView iv_icon = view.findViewById(R.id.iv_icon);
        iv_icon.setImageResource(girl.icon);

        TextView tv_like = view.findViewById(R.id.tv_like);
        tv_like.setText(girl.style);

        return view;
    }
}
