package com.datalife.datalife.calendar.adapter;

import android.content.Context;
import android.content.Intent;
import android.os.AsyncTask;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;


import com.datalife.datalife.R;
import com.datalife.datalife.bean.Schedule;
import com.datalife.datalife.calendar.widget.StrikeThruTextView;
import com.datalife.datalife.util.JeekUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Jimmy on 2016/10/8 0008.
 */
public class ScheduleAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int SCHEDULE_TYPE = 1;
    private int SCHEDULE_CENTER = 2;
    private int SCHEDULE_FINISH_TYPE = 3;
    private int SCHEDULE_BOTTOM = 4;

    private Context mContext;
    private List<Schedule> mSchedules;
    private List<Schedule> mFinishSchedules;

    private boolean mIsShowFinishTask = false;

    public ScheduleAdapter(Context context) {
        mContext = context;
        initData();
    }

    private void initData() {
        mSchedules = new ArrayList<>();
        mFinishSchedules = new ArrayList<>();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            return new ScheduleViewHolder(LayoutInflater.from(mContext).inflate(R.layout.item_schedule, parent, false));
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ScheduleViewHolder) {
            final Schedule schedule = mSchedules.get(position);
            final ScheduleViewHolder viewHolder = (ScheduleViewHolder) holder;
            viewHolder.vScheduleHintBlock.setBackgroundResource(JeekUtils.getScheduleBlockView(schedule.getColor()));
            viewHolder.tvScheduleTitle.setText(schedule.getTitle());
            if (schedule.getTime() != 0) {
                viewHolder.tvScheduleTime.setText(JeekUtils.timeStamp2Time(schedule.getTime()));
            } else {
                viewHolder.tvScheduleTime.setText("");
            }
            if (schedule.getState() == 0) {
                viewHolder.tvScheduleState.setBackgroundResource(R.drawable.start_schedule_hint);
                viewHolder.tvScheduleState.setText(mContext.getString(R.string.start));
                viewHolder.tvScheduleState.setTextColor(mContext.getResources().getColor(R.color.color_schedule_start));
            } else {
                viewHolder.tvScheduleState.setTextColor(mContext.getResources().getColor(R.color.color_schedule_finish));
            }
            viewHolder.tvScheduleState.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    changeScheduleState(schedule);
                }
            });
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
//                        mContext.startActivity(new Intent(mContext, MainActivity.class)
//                                .putExtra(ScheduleDetailActivity.SCHEDULE_OBJ, schedule)
//                                .putExtra(ScheduleDetailActivity.CALENDAR_POSITION, ((ScheduleFragment) mBaseFragment).getCurrentCalendarPosition()));
                }
            });
        } else if (holder instanceof ScheduleFinishViewHolder) {
            final Schedule schedule = mFinishSchedules.get(position - mSchedules.size() - 1);
            ScheduleFinishViewHolder viewHolder = (ScheduleFinishViewHolder) holder;
            viewHolder.tvScheduleTitle.setText(schedule.getTitle());
            if (mIsShowFinishTask) {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
                params.height = LinearLayout.LayoutParams.WRAP_CONTENT;
                params.bottomMargin = mContext.getResources().getDimensionPixelSize(R.dimen.space_3dp);
                viewHolder.itemView.setLayoutParams(params);
            } else {
                RecyclerView.LayoutParams params = (RecyclerView.LayoutParams) viewHolder.itemView.getLayoutParams();
                params.height = 0;
                params.bottomMargin = 0;
                viewHolder.itemView.setLayoutParams(params);
            }
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                public void onClick(View v) {
//                    showDeleteScheduleDialog(schedule);
                }
            });
        } else if (holder instanceof ScheduleCenterViewHolder) {
            ScheduleCenterViewHolder viewHolder = (ScheduleCenterViewHolder) holder;
            if (mFinishSchedules.size() > 0) {
                viewHolder.tvChangeTaskList.setEnabled(true);
            } else {
                viewHolder.tvChangeTaskList.setEnabled(false);
            }
             viewHolder.tvFinishHint.setVisibility(mIsShowFinishTask && mFinishSchedules.size() > 0 ? View.VISIBLE : View.GONE);
            viewHolder.tvChangeTaskList.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mIsShowFinishTask = !mIsShowFinishTask;
                    notifyDataSetChanged();
                }
            });
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (position < mSchedules.size()) {
            return SCHEDULE_TYPE;
        } else if (position == mSchedules.size()) {
            return SCHEDULE_CENTER;
        } else if (position == getItemCount() - 1) {
            return SCHEDULE_BOTTOM;
        } else {
            return SCHEDULE_FINISH_TYPE;
        }
    }

    @Override
    public int getItemCount() {
        return mSchedules.size() + mFinishSchedules.size() + 2;
    }

    protected class ScheduleViewHolder extends RecyclerView.ViewHolder {

        protected View vScheduleHintBlock;
        protected TextView tvScheduleState;
        protected TextView tvScheduleTitle;
        protected TextView tvScheduleTime;

        public ScheduleViewHolder(View itemView) {
            super(itemView);
            vScheduleHintBlock = itemView.findViewById(R.id.vScheduleHintBlock);
            tvScheduleState = (TextView) itemView.findViewById(R.id.tvScheduleState);
            tvScheduleTitle = (TextView) itemView.findViewById(R.id.tvScheduleTitle);
            tvScheduleTime = (TextView) itemView.findViewById(R.id.tvScheduleTime);
        }

    }

    protected class ScheduleFinishViewHolder extends RecyclerView.ViewHolder {

        protected StrikeThruTextView tvScheduleTitle;
        protected TextView tvScheduleTime;

        public ScheduleFinishViewHolder(View itemView) {
            super(itemView);
            tvScheduleTitle = (StrikeThruTextView) itemView.findViewById(R.id.tvScheduleTitle);
            tvScheduleTime = (TextView) itemView.findViewById(R.id.tvScheduleTime);
        }
    }

    protected class ScheduleCenterViewHolder extends RecyclerView.ViewHolder {

        protected TextView tvChangeTaskList;
        protected TextView tvFinishHint;

        public ScheduleCenterViewHolder(View itemView) {
            super(itemView);
        }
    }

    protected class ScheduleBottomViewHolder extends RecyclerView.ViewHolder {

        public ScheduleBottomViewHolder(View itemView) {
            super(itemView);
        }
    }

    public void changeAllData(List<Schedule> schedules) {
        distinguishData(schedules);
    }

    public void insertItem(Schedule schedule) {
        mSchedules.add(schedule);
        notifyItemInserted(mSchedules.size() - 1);
    }

    public void removeItem(Schedule schedule) {
        if (mSchedules.remove(schedule)) {
            notifyDataSetChanged();
        } else if (mFinishSchedules.remove(schedule)) {
            notifyDataSetChanged();
        }
    }

    private void changeScheduleItem(Schedule schedule) {
        int i = mSchedules.indexOf(schedule);
        if (i != -1) {
            notifyItemChanged(i);
        }
    }

    private void changeScheduleState(final Schedule schedule) {
        switch (schedule.getState()) {
            case 0:
                break;
            case 1:
                schedule.setState(2);

                break;
        }
    }

    private void distinguishData(List<Schedule> schedules) {
        mSchedules.clear();
        mFinishSchedules.clear();
        for (int i = 0, count = schedules.size(); i < count; i++) {
            Schedule schedule = schedules.get(i);
            if (schedule.getState() == 2) {
                mFinishSchedules.add(schedule);
            } else {
                mSchedules.add(schedule);
            }
        }
        notifyDataSetChanged();
    }

}
