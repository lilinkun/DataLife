package com.datalife.datalife.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.dao.NewsInfo;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

/**
 * Created by LG on 2018/2/3.
 */

public class HealthNewsAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<NewsInfo> newsInfos;


    public HealthNewsAdapter(Context context,ArrayList<NewsInfo> newsInfos){
        this.context = context;
        this.newsInfos = newsInfos;
    }

    @Override
    public int getCount() {
        return newsInfos.size();
    }

    @Override
    public Object getItem(int position) {
        return newsInfos.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;

        if (convertView == null){
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(context).inflate(R.layout.newsadapter,null);
            viewHolder.contentTv = (TextView) convertView.findViewById(R.id.tv_content);
            viewHolder.dateTv = (TextView) convertView.findViewById(R.id.tv_date);
            viewHolder.newsIv = (ImageView) convertView.findViewById(R.id.iv_news);
            convertView.setTag(viewHolder);

        }else{
            viewHolder = (ViewHolder) convertView.getTag();
        }

        viewHolder.contentTv.setText(newsInfos.get(position).getArticle_title());
        viewHolder.dateTv.setText(newsInfos.get(position).getAdd_time());
        Picasso.with(viewHolder.newsIv.getContext()).load(newsInfos.get(position).getThumbnail()).placeholder(R.mipmap.ic_loading).error(R.mipmap.ic_loading).into(viewHolder.newsIv);

        return convertView;
    }

    class ViewHolder{
        TextView contentTv;
        TextView dateTv;
        ImageView newsIv;

    }
}
