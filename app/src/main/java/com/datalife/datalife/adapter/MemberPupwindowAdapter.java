package com.datalife.datalife.adapter;

import android.content.Context;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.MemberListBean;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/3/6.
 */

public class MemberPupwindowAdapter extends BaseAdapter{

    private Context mContext;
    private List<MemberListBean> memberListBeans;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private boolean isChoose = false;
    private Handler handler;

    public MemberPupwindowAdapter(Context context, List<MemberListBean> familyUserInfos, Handler handler){
        this.mContext = context;
        this.memberListBeans = familyUserInfos;
        this.handler = handler;
    }


    @Override
    public int getCount() {
        return memberListBeans.size();
    }

    @Override
    public Object getItem(int position) {
        return memberListBeans.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {

        ViewHolder viewHolder = null;
        if (convertView == null){

            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.adapter_bindmember,null);
            viewHolder.imgface = (ImageView) convertView.findViewById(R.id.iv_bind_head);
            viewHolder.membername = (TextView) convertView.findViewById(R.id.tv_bind_name);
            viewHolder.imgIcon = (ImageView) convertView.findViewById(R.id.iv_bind_icon);
            viewHolder.linearLayout = (LinearLayout) convertView.findViewById(R.id.iv_bind_icon_layout);

            convertView.setTag(viewHolder);
        }else{

            viewHolder = (ViewHolder) convertView.getTag();
        }

        DataLifeUtil.GetPIC(mContext,memberListBeans.get(position).getFamilyUserInfo().getMember_Portrait(),viewHolder.imgface);
        viewHolder.membername.setText(memberListBeans.get(position).getFamilyUserInfo().getMember_Name());
        imageViews.add(viewHolder.imgIcon);
        if (memberListBeans.get(position).isSelector()){
            viewHolder.imgIcon.setImageResource(R.mipmap.ic_choose);
        }else{
            viewHolder.imgIcon.setImageResource(R.mipmap.ic_unchoose);
        }


        /*viewHolder.imgIcon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Message message = new Message();
                message.what = 0x1234;
                Bundle bundle = new Bundle();
                bundle.putInt("position",memberListBeans.get(position).getFamilyUserInfo().getMember_Id());
                message.setData(bundle);
                handler.sendMessage(message);
                if (!isChoose){
                    ((ImageView)v).setImageResource(R.mipmap.ic_choose);
                }else{
                    ((ImageView)v).setImageResource(R.mipmap.ic_unchoose);
                }
                isChoose = !isChoose;
            }
        });*/

        return convertView;
    }

    class ViewHolder{
        ImageView imgface;
        TextView membername;
        ImageView imgIcon;
        LinearLayout linearLayout;
    }
}
