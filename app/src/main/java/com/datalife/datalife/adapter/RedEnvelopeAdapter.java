package com.datalife.datalife.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.datalife.datalife.R;
import com.datalife.datalife.bean.RedEnvelopeBean;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by LG on 2018/10/18.
 */

public class RedEnvelopeAdapter extends RecyclerView.Adapter<RedEnvelopeAdapter.ViewHolder> {

    private Context mContext;
    private ArrayList<RedEnvelopeBean> redEnvelopeBeans = null;

    public RedEnvelopeAdapter(Context context, ArrayList<RedEnvelopeBean> redEnvelopeBeans){
        this.mContext = context;
        this.redEnvelopeBeans = redEnvelopeBeans;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(mContext).inflate(R.layout.adapter_redenvelope_list,null);

        ViewHolder viewHolder = new ViewHolder(view);


        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.redEnvelopeType.setText(redEnvelopeBeans.get(position).getRedEnvelopeType());
        holder.redEnvelopeTime.setText(redEnvelopeBeans.get(position).getRedEnvelopeTime());
        holder.redEnvelopeSum.setText("+ " + redEnvelopeBeans.get(position).getRedEnvelopeSum() + "元");

    }

    @Override
    public int getItemCount() {
        return redEnvelopeBeans.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        private TextView redEnvelopeType;
        private TextView redEnvelopeTime;
        private TextView redEnvelopeSum;

        public ViewHolder(View itemView) {
            super(itemView);
            redEnvelopeType = itemView.findViewById(R.id.tv_redenvelope_type);
            redEnvelopeTime = itemView.findViewById(R.id.tv_redenvelope_time);
            redEnvelopeSum = itemView.findViewById(R.id.tv_redenvelope_sum);
        }
    }
}
