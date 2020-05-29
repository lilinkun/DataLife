package com.datalife.datalife.adapter;

import android.content.Context;
import android.os.Handler;
import android.support.v7.widget.AppCompatButton;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.MemberItem;
import com.datalife.datalife.bean.MemberListBean;
import com.datalife.datalife.dao.FamilyUserInfo;
import com.datalife.datalife.db.DBManager;
import com.datalife.datalife.util.DataLifeUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/10/8.
 */

public class BindToothAdapter extends RecyclerView.Adapter<BindToothAdapter.ViewHolder> implements View.OnClickListener {

    private MemberPupwindowAdapter pupwindowAdapter;
    private Context context;
    private ArrayList<MemberListBean> memberListBeans = new ArrayList<>();
    List<FamilyUserInfo> familyUserInfos = null;
    private Handler myHandler;
    private ArrayList<ImageView> imageViews = new ArrayList<>();
    private OnItemClickListener mItemClickListener;
    private int posId;


    public BindToothAdapter(Context context, Handler handler,int posId,List<FamilyUserInfo> familyUserInfos){
        this.context = context;
        this.myHandler = handler;
        this.posId = posId;
        this.familyUserInfos = familyUserInfos;
    }

    public void setPosId(int posId){
        this.posId = posId;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.adapter_bindmember,null);
        ViewHolder viewHolder = new ViewHolder(view);

        view.setOnClickListener(this);

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.itemView.setTag(position);

        DataLifeUtil.GetPIC(context,familyUserInfos.get(position).getMember_Portrait(),holder.imgface);
        holder.membername.setText(familyUserInfos.get(position).getMember_Name());
        imageViews.add(holder.imgIcon);
        if (position == posId){
            holder.imgIcon.setImageResource(R.mipmap.ic_choose);
        }else{
            holder.imgIcon.setImageResource(R.mipmap.ic_unchoose);
        }


    }

    @Override
    public int getItemCount() {
        return familyUserInfos.size();
    }

    public void setItemClickListener(OnItemClickListener itemClickListener) {
        mItemClickListener = itemClickListener;
    }

    @Override
    public void onClick(View v) {
        if (mItemClickListener!=null){
            mItemClickListener.onItemClick((Integer) v.getTag());
        }
    }

    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        ImageView imgface;
        TextView membername;
        ImageView imgIcon;
        LinearLayout linearLayout;

        public ViewHolder(View itemView) {
            super(itemView);
            imgface = (ImageView) itemView.findViewById(R.id.iv_bind_head);
            membername = (TextView) itemView.findViewById(R.id.tv_bind_name);
            imgIcon = (ImageView) itemView.findViewById(R.id.iv_bind_icon);
            linearLayout = (LinearLayout) itemView.findViewById(R.id.iv_bind_icon_layout);
        }
    }

}
