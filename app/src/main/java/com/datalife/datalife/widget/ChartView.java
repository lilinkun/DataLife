package com.datalife.datalife.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import com.datalife.datalife.R;
import com.datalife.datalife.util.UToast;

/**
 * Created by LG on 2018/7/9.
 */

public class ChartView extends View {

    private int position;
    private Paint mPaint, mChartPaint,mTextPaint;
    private Rect mBound;
    private int mStartWidth, mHeight, mWidth, mChartWidth, mSize;
    private String date,time;
    private int mValue;
    private ChartView.getPositionListener listener;
    private Context mContext;

    public ChartView(Context context) {
        super(context);
        init(context);
    }
    public void setList(int mValue,int positon,String str,String time) {
        this.mValue = mValue;
        this.position = positon;
        this.date = str;
        this.time = time;
        invalidate();
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mContext = context;
        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mBound = new Rect();
        mChartPaint = new Paint();
        mChartPaint.setAntiAlias(true);
        mChartPaint.setColor(context.getResources().getColor(R.color.tooth_blue));
        mChartPaint.setTextSize(18);
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(context.getResources().getColor(R.color.tooth_blue));
        mTextPaint.setTextSize(32);
    }

    @Override
    protected void onLayout(boolean changed, int left, int top, int right, int bottom) {
        super.onLayout(changed, left, top, right, bottom);
        mWidth = getWidth();
        mHeight = getHeight();
        mStartWidth = getWidth() / 13;
        mSize = mWidth / 25;
        mChartWidth = 10;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int size = mHeight / 130;
        mChartPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStyle(Paint.Style.FILL);


        RectF rectF = new RectF();
        rectF.left = mChartWidth;
        rectF.right = mChartWidth + 70;
        rectF.bottom = mHeight - 100;
        rectF.top = mHeight - 100 - mValue * size;
        canvas.drawRoundRect(rectF, 0, 0, mChartPaint);
        mChartPaint.setTextSize(32);
        float sss = getFontWidth(mChartPaint,date+"");
        float ssss = getFontWidth(mChartPaint,time+"");
        canvas.drawText(mValue + "",mChartWidth+35-getTextWidth(mChartPaint,String.valueOf(mValue))/2,mHeight - 100 - mValue * size - 5,mTextPaint);
        canvas.drawText(date + "",mChartWidth-sss/2+35,mHeight - 70,mTextPaint);
        canvas.drawText(time + "",mChartWidth-ssss/2+35,mHeight - 40,mTextPaint);
    }

    public float getFontWidth(Paint paint, String text) {
        return paint.measureText(text);
    }

    /*@Override
    public boolean onTouchEvent(MotionEvent event) {
        int x = (int) event.getX();
        int y = (int) event.getY();
        int left = 0;
        int top = 0;
        int right = mWidth / 12;
        int bottom = mHeight - 100;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mChartPaint.setColor(mContext.getResources().getColor(R.color.tooth_yellow));
                listener.setPosition(position);
                invalidate();
                break;
            case MotionEvent.ACTION_UP:

                break;
        }
        return true;
    }*/

    public void setColor(int color){
        mChartPaint.setColor(color);
        invalidate();
    }

    public static int getTextWidth(Paint paint, String str) {
        int iRet = 0;
        if (str != null && str.length() > 0) {
            int len = str.length();
            float[] widths = new float[len];
            paint.getTextWidths(str, widths);
            for (int j = 0; j < len; j++) {
                iRet += (int) Math.ceil(widths[j]);
            }
        }
        return iRet;
    }



    public void setListener(ChartView.getPositionListener listener) {
        this.listener = listener;
    }

    public interface getPositionListener{
        public void setPosition(int position);
    }
}
