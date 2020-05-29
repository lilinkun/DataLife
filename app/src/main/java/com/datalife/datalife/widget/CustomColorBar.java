package com.datalife.datalife.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import com.datalife.datalife.R;

import java.text.DecimalFormat;

/**
 * Created by LG on 2018/2/2.
 */

public class CustomColorBar extends View {
    private int mWidth;
    private int mHeight;
    private Paint mBarPaint;
    private Paint mTextPaint;
    private int mTextSize = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics());
    private int mTextColor = Color.BLACK;
    private int[] mColors;
    private float[] mRange;
    private float mEachWidth;
    private float mMinValue;
    private float mMaxValue;
    private float mCurrValue;
    private Rect mTextBound;
    private int mTriangleHeight = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 20, getResources().getDisplayMetrics());
    private int mTriangleColor = Color.BLUE;
    private String mDataStyle = "#.0";
    private DecimalFormat decimalFormat;
    private int indicatorRangeResourceId;
    private int indicatorColorsResourceId;

    public CustomColorBar(Context context){
        this(context, null);
    }

    public CustomColorBar(Context context, AttributeSet attrs){
        this(context, attrs, 0);
    }

    public void setValueAndColors(float[] values, int[] colors){
        this.mRange = values;
        this.mColors = colors;
        mMinValue = mRange[0];
        mMaxValue = mRange[mRange.length - 1];
        invalidate();
    }

    public void setTargetValue(float targetValue){
        if(targetValue < mMinValue){
            this.mCurrValue = mMinValue;
        }else if(targetValue > mMaxValue){
            this.mCurrValue = mMaxValue;
        }else {
            this.mCurrValue = targetValue;
        }

        invalidate();
    }


    public CustomColorBar(Context context, AttributeSet attrs, int defStyle){
        super(context, attrs, defStyle);

        TypedArray a = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CustomColorBar, defStyle, 0);

        int n = a.getIndexCount();

        for(int i = 0; i < n; i++){
            int attr = a.getIndex(i);
            if(attr == R.styleable.CustomColorBar_indicatorRange){
//                indicatorRangeResourceId = a.getInt(attr,0);
//                    mRange = context.getResources().getStringArray(indicatorRangeResourceId);
            }else if(attr == R.styleable.CustomColorBar_indicatorColors){
//                indicatorColorsResourceId = a.getInt(attr,0);
//                    mColors = context.getResources().getIntArray(indicatorColorsResourceId);
            }else if(attr == R.styleable.CustomColorBar_textColor){
                mTextColor = a.getColor(attr, Color.BLACK);
            }else if(attr == R.styleable.CustomColorBar_textSize){
                mTextSize = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
            }else if(attr == R.styleable.CustomColorBar_colorTriangle){
                mTriangleColor = a.getColor(attr,Color.BLUE);
            }else if(attr == R.styleable.CustomColorBar_triangleHeight){
                mTriangleHeight = a.getDimensionPixelSize(attr, (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 30, getResources().getDisplayMetrics()));
            }else if(attr == R.styleable.CustomColorBar_dataStyle){
                mDataStyle = a.getString(attr);
            }
        }

        a.recycle();

        mRange = new float[]{0.0f,1.0f};
        mColors = new int[]{Color.RED};
        mMinValue = mRange[0];
        mMaxValue = mRange[mRange.length - 1];
        mBarPaint = new Paint();
        mBarPaint.setStyle(Paint.Style.FILL);
        mBarPaint.setAntiAlias(true);
        mTextPaint = new Paint();
        mTextPaint.setTextSize(mTextSize);
        mTextPaint.setColor(mTextColor);
        mTextPaint.setAntiAlias(true);
        mTextBound = new Rect();


        mCurrValue = 0.0f;
        decimalFormat = new DecimalFormat();
        decimalFormat.applyPattern(mDataStyle);
        String text = decimalFormat.format(mCurrValue);
        mTextPaint.getTextBounds(text, 0, text.length(), mTextBound);

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int specMode = MeasureSpec.getMode(widthMeasureSpec);
        int specSize = MeasureSpec.getSize(widthMeasureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            mWidth = specSize;
        }else {
            int desireByImg = getPaddingLeft() + getPaddingRight();
            if(specMode == MeasureSpec.AT_MOST){
                mWidth = Math.min(desireByImg,specSize);
            }else {
                mWidth = desireByImg;
            }
        }

        specMode = MeasureSpec.getMode(heightMeasureSpec);
        specSize = MeasureSpec.getSize(heightMeasureSpec);

        if(specMode == MeasureSpec.EXACTLY){
            mHeight = specSize;
        }else {
            int desire = getPaddingTop() + getPaddingBottom() + mTextBound.height() + mTriangleHeight + 30;
            if(specMode == MeasureSpec.AT_MOST){
                mHeight = Math.min(desire, specSize);
            }else {
                mHeight = desire;
            }
        }
        setMeasuredDimension(mWidth, mHeight);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        int paddingRight = getPaddingRight();
        int paddingBottom = getPaddingBottom();
        int rectWidth = mWidth - paddingLeft - paddingRight;
        mEachWidth = rectWidth / (mMaxValue - mMinValue);
        float offsetWidth = paddingLeft;

        String valueText = decimalFormat.format(mRange[0]);
        mTextPaint.getTextBounds(valueText, 0, valueText.length(), mTextBound);
//        canvas.drawText(value1,offsetWidth  - mTextBound.width() * 1.0f / 2,paddingTop + mTextBound.height() * 1.0f / 2,mTextPaint);
        canvas.drawText(valueText,offsetWidth,paddingTop + mTextBound.height() * 1.0f / 2,mTextPaint);

        for(int i = 1; i < mRange.length; i++){
            float value = mRange[i] - mRange[i-1];
            int width = Math.round(mEachWidth * value);
            int color = mColors[i-1];
            valueText = decimalFormat.format(mRange[i]);
            mTextPaint.getTextBounds(valueText, 0, valueText.length(), mTextBound);
            if(i == mRange.length -1){
                canvas.drawText(valueText,offsetWidth + width - mTextBound.width() * 1.0f ,paddingTop + mTextBound.height() * 1.0f / 2,mTextPaint);
            }else {
                canvas.drawText(valueText,offsetWidth + width - mTextBound.width() * 1.0f / 2,paddingTop + mTextBound.height() * 1.0f / 2,mTextPaint);
            }

            mBarPaint.setColor(color);
            canvas.drawRect(offsetWidth,paddingTop + mTextBound.height() + 10,offsetWidth + width,mHeight - getPaddingBottom() - mTriangleHeight, mBarPaint);
            offsetWidth = offsetWidth + width;
        }
        int vertexX = Math.round((mCurrValue - mMinValue) * mEachWidth) + paddingLeft;
        int vertexY = mHeight - paddingBottom - mTriangleHeight;
        drawTriangle(canvas,new Point(vertexX,vertexY));
        mBarPaint.setColor(mTriangleColor);
        canvas.drawLine(paddingLeft,mHeight - paddingBottom - 2,paddingLeft + rectWidth,mHeight-paddingBottom,mBarPaint);

    }

    private void drawTriangle(Canvas canvas, Point vertex){
        Paint paint=new Paint();
        paint.setAntiAlias(true);
        paint.setColor(mTriangleColor);
        paint.setStyle(Paint.Style.FILL);
        Path path=new Path();
        path.moveTo(vertex.x,vertex.y);
        path.lineTo(vertex.x - 20,vertex.y + mTriangleHeight);
        path.lineTo(vertex.x + 20,vertex.y + mTriangleHeight);
        path.close();
        canvas.drawPath(path, paint);
    }

    private float[] convertString2Values(String strValue){
        return null;
    }

    private int[] convertString2Color(String strColors){
        return null;
    }
}
