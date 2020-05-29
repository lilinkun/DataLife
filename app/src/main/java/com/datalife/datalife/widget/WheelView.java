package com.datalife.datalife.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Handler;
import android.os.Message;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.Interpolator;
import android.widget.Scroller;

import com.datalife.datalife.adapter.NumericWheelAdapter;
import com.datalife.datalife.adapter.WheelAdapter;
import com.datalife.datalife.interf.OnWheelChangedListener;
import com.datalife.datalife.interf.OnWheelScrollListener;
import com.datalife.datalife.util.DrawUtil;

import java.util.LinkedList;
import java.util.List;

/**
 * Created by LG on 2018/1/28.
 */

public class WheelView  extends View {
    /**
     * Scrolling duration
     */
    private static final int SCROLLING_DURATION = 400;

    /**
     * Minimum delta for scrolling
     */
    private static final int MIN_DELTA_FOR_SCROLLING = 1;


    /**
     * Text size
     */
    private int textSize = 35;

    /**
     * Label offset
     */
    private static final int LABEL_OFFSET = 40;

    /**
     * Default count of visible items
     */
    private static final int DEF_VISIBLE_ITEMS = 5;

    // Wheel Values
    private WheelAdapter adapter = null;
    private int currentItem = 0;

    // ѡ��Ŀ��
    private int itemsWidth = 0;


    // �ɼ�item��
    private int visibleItems = DEF_VISIBLE_ITEMS;

    // item�ĸ߶�
    private int itemHeight = 0;

    // ѡ���
    private TextPaint itemsPaint;

    // ��ʾ��λ
    private String label;

    // �Ƿ����� ����
    private boolean isScrollingPerformed;
    // ����ƫ�ƾ���
    private float scrollingOffset;

    // ��������
    private GestureDetector gestureDetector;
    private Scroller scroller;
    private float lastScrollY;

    // �Ƿ�ѭ������
    boolean isCyclic = false;
    // �����߻���
    private Paint centerPaint;

    // Listeners
    private List<OnWheelChangedListener> changingListeners = new LinkedList<OnWheelChangedListener>();
    private List<OnWheelScrollListener> scrollingListeners = new LinkedList<OnWheelScrollListener>();


    private boolean canScroll = true;

    /**
     * @param context
     * @param attrs
     * @param defStyle
     */
    public WheelView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        initData(context);
    }

    /**
     * @param context
     * @param attrs
     */
    public WheelView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initData(context);
    }

    /**
     * @param context
     */
    public WheelView(Context context) {
        super(context);
        initData(context);
    }

    /**
     * @param context
     */
    private void initData(Context context) {
        gestureDetector = new GestureDetector(context, gestureListener);
        gestureDetector.setIsLongpressEnabled(false);
        scroller = new Scroller(context);
        initResourcesIfNecessary();
        textSize = DrawUtil.sp2px(context, 16);
    }


    public int getCurrentValue(){
        NumericWheelAdapter numericWheelAdapter=(NumericWheelAdapter)getAdapter();
        return getCurrentItem()%(numericWheelAdapter.getMaxValue()-numericWheelAdapter.getMinValue()+1)+numericWheelAdapter.getMinValue();
    }

    /**
     * @return
     */
    public WheelAdapter getAdapter() {
        return adapter;
    }

    /**
     * @param adapter
     */
    public void setAdapter(WheelAdapter adapter) {
        this.adapter = adapter;
        invalidate();
    }

    public void setMyTextSize(int textSize) {
        this.textSize = textSize;
        invalidate();
    }

    /**
     * @param interpolator
     */
    public void setInterpolator(Interpolator interpolator) {
        scroller.forceFinished(true);
        scroller = new Scroller(getContext(), interpolator);
    }

    /**
     * @return
     */
    public int getVisibleItems() {
        return visibleItems;
    }

    /**
     * @param count
     */
    public void setVisibleItems(int count) {
        visibleItems = count;
        invalidate();
    }

    /**
     * @return
     */
    public String getLabel() {
        return label;
    }

    public boolean isCanScroll() {
        return canScroll;
    }

    public void setCanScroll(boolean canScroll) {
        this.canScroll = canScroll;
    }

    /**
     * @param newLabel
     */
    public void setLabel(String newLabel) {
        if (label == null || !label.equals(newLabel)) {
            label = newLabel;
            invalidate();
        }
    }

    /**
     * @param listener
     */
    public void addChangingListener(OnWheelChangedListener listener) {
        changingListeners.add(listener);
    }

    /**
     * @param listener
     */
    public void removeChangingListener(OnWheelChangedListener listener) {
        changingListeners.remove(listener);
    }

    /**
     * @param oldValue
     * @param newValue
     */
    protected void notifyChangingListeners(int oldValue, int newValue) {
        for (OnWheelChangedListener listener : changingListeners) {
            listener.onChanged(this, oldValue, newValue);
        }
    }

    /**
     * Adds wheel scrolling listener
     *
     * @param listener the listener
     */
    public void addScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.add(listener);
    }

    /**
     * Removes wheel scrolling listener
     *
     * @param listener the listener
     */
    public void removeScrollingListener(OnWheelScrollListener listener) {
        scrollingListeners.remove(listener);
    }

    /**
     * Notifies listeners about starting scrolling
     */
    protected void notifyScrollingListenersAboutStart() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingStarted(this);
        }
    }

    /**
     * Notifies listeners about ending scrolling
     */
    protected void notifyScrollingListenersAboutEnd() {
        for (OnWheelScrollListener listener : scrollingListeners) {
            listener.onScrollingFinished(this);
        }
    }

    /**
     * @return
     */
    public int getCurrentItem() {
        return currentItem;
    }

    /**
     * @param index
     * @param animated
     */
    public void setCurrentItem(int index, boolean animated) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return; // throw?
        }
        if (index < 0 || index >= adapter.getItemsCount()) {
            if (isCyclic) {
                while (index < 0) {
                    index += adapter.getItemsCount();
                }
                index %= adapter.getItemsCount();
            } else {
                return; // throw?
            }
        }
        if (index != currentItem) {
            if (animated) {
                scroll(index - currentItem, SCROLLING_DURATION);
            } else {

                int old = currentItem;
                currentItem = index;
                notifyChangingListeners(old, currentItem);
                invalidate();
            }
        }
    }

    /**
     * @param index
     */
    public void setCurrentItem(int index) {
        setCurrentItem(index, false);
    }

    /**
     * @return
     */
    public boolean isCyclic() {
        return isCyclic;
    }

    /**
     * @param isCyclic
     */
    public void setCyclic(boolean isCyclic) {
        this.isCyclic = isCyclic;

        invalidate();
    }


    private void initResourcesIfNecessary() {
        scrollingOffset = 0;
        if (itemsPaint == null) {
            itemsPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG
                    | Paint.FAKE_BOLD_TEXT_FLAG);
            // itemsPaint.density = getResources().getDisplayMetrics().density;
            itemsPaint.setTextSize(textSize);
        }

        setBackgroundColor(Color.WHITE);
    }

    /**
     */
    private String getTextItem(int index) {
        if (adapter == null || adapter.getItemsCount() == 0) {
            return null;
        }
        int count = adapter.getItemsCount();
        if ((index < 0 || index >= count) && !isCyclic) {
            return null;
        } else {
            while (index < 0) {
                index = count + index;
            }
        }

        index %= count;
        return adapter.getItem(index);
    }

    /**
     *
     * @param useCurrentValue
     * @return
     */
    private String[] buildText(boolean useCurrentValue) {
        int addItems = (visibleItems - 1) / 2;
        String[] items = new String[visibleItems];
        int index = 0;
        for (int i = currentItem - addItems; i <= currentItem + addItems; i++) {
            if (useCurrentValue) {
                String text = getTextItem(i);
                if (text != null) {
                    items[index] = text;
                }
                index++;
            }
        }

        return items;
    }

    private float getMaxTextWidth() {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return 0;
        }

        int adapterLength = adapter.getMaximumLength();
        if (adapterLength > 0) {
            StringBuffer sb = new StringBuffer();
            for (int i = 0; i < adapterLength; i++) {
                sb.append("1");
            }
            return DrawUtil.getTextWidth(textSize, sb.toString());
        }

        String maxText = null;
        int addItems = visibleItems / 2;
        for (int i = Math.max(currentItem - addItems, 0); i < Math.min(
                currentItem + visibleItems, adapter.getItemsCount()); i++) {
            String text = adapter.getItem(i);
            if (text != null
                    && (maxText == null || maxText.length() < text.length())) {
                maxText = text;
            }
        }

        return maxText != null ? DrawUtil.getTextWidth(textSize, maxText) : 0;
    }


    private float getItemHeight() {

        return (float) (getHeight() * 1.0 / visibleItems);
    }

    private float getTextCenterX() {
        return getWidth()
                / 2
                - ((label == null || label.length() == 0) ? 0 : (LABEL_OFFSET + DrawUtil
                .getTextWidth(textSize, label)) / 2);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize, heightSize);
        itemsWidth = (int) getMaxTextWidth();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        if (itemsWidth > 0) {
            canvas.save();
            drawItems(canvas);
            canvas.restore();
        }
        drawCenterLines(canvas);
        drawLable(canvas);
    }

    /**
     *
     * @param canvas
     */
    private void drawItems(Canvas canvas) {
        canvas.save();
        String[] item = buildText(true);
        int center = getHeight() / 2;
        float offset = getItemHeight();
        float cX = getTextCenterX();
        for (int i = 0; i < item.length; i++) {
            int off = (item.length - 1) / 2 - i;
            int itemTextSize = textSize - Math.abs(off);
            setItemPaint(i);
            if (item[i] != null) {
                canvas.drawText(item[i],
                        cX - DrawUtil.getScaleTextWidth(itemsPaint, item[i]) / 2, (float) (center + DrawUtil.getTextHeight(textSize) / 4 - off * offset * 3.0 / 4 + scrollingOffset),
                        itemsPaint);

            }
        }

        canvas.restore();
    }

    private void drawLable(Canvas canvas) {
        float cX = getTextCenterX();
        int center = getHeight() / 2;
        if (label != null && !label.equals("")) {
            itemsPaint.setColor(Color.BLACK);
            itemsPaint.setTextScaleX(1);
            itemsPaint.setTextSize(textSize);
            canvas.drawText(label, cX + getMaxTextWidth() / 2 + LABEL_OFFSET, (float) (center + DrawUtil.getTextHeight(textSize) / 4), itemsPaint);
        }
    }


    /**
     * @param i
     */
    private void setItemPaint(int i) {
        int off = (visibleItems - 1) / 2 - i;
        itemsPaint.setTextScaleX((float) (1 + Math.abs(off) * 0.15));
        itemsPaint.setColor(Color.argb(180 - Math.abs(off) * (360 / (visibleItems + 1)), 0, 0, 0));
        itemsPaint.setTextSize(textSize - Math.abs(off) * 5);
    }

    /**
     * @param canvas
     */
    public void drawCenterLines(Canvas canvas) {
        int center = getHeight() / 2;
        float offset = getItemHeight() / 2;
        centerPaint = new Paint();
        centerPaint.setAntiAlias(true);
        centerPaint = new Paint();
        centerPaint.setColor(Color.BLACK);
        centerPaint.setStyle(Paint.Style.STROKE);
        centerPaint.setStrokeWidth(1);
        canvas.drawLine(0, center - offset, getWidth(), center - offset,
                centerPaint);
        canvas.drawLine(0, center + offset, getWidth(), center + offset,
                centerPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        WheelAdapter adapter = getAdapter();
        if (adapter == null) {
            return true;
        }


        if (canScroll && !gestureDetector.onTouchEvent(event)
                && event.getAction() == MotionEvent.ACTION_UP) {
            justify();
        }
        return true;
    }

    /**
     * @param delta
     */
    private void doScroll(float delta) {
        scrollingOffset += delta;

        int count = (int) (scrollingOffset / getItemHeight());
        int pos = currentItem - count;
        if (isCyclic && adapter.getItemsCount() > 0) {
            // fix position by rotating
            while (pos < 0) {
                pos += adapter.getItemsCount();
            }
            pos %= adapter.getItemsCount();
        } else if (isScrollingPerformed) {
            //
            if (pos < 0) {
                count = currentItem;
                pos = 0;
            } else if (pos >= adapter.getItemsCount()) {
                count = currentItem - adapter.getItemsCount() + 1;
                pos = adapter.getItemsCount() - 1;
            }
        } else {
            // fix position
            pos = Math.max(pos, 0);
            pos = Math.min(pos, adapter.getItemsCount() - 1);
        }

        float offset = scrollingOffset;
        if (pos != currentItem) {
            setCurrentItem(pos, false);
        } else {
            invalidate();
        }

        // update offset
        scrollingOffset = offset - count * getItemHeight();
        if (scrollingOffset > getHeight()) {
            scrollingOffset = scrollingOffset % getHeight() + getHeight();
        }
    }


    private GestureDetector.SimpleOnGestureListener gestureListener = new GestureDetector.SimpleOnGestureListener() {
        public boolean onDown(MotionEvent e) {
            if (isScrollingPerformed) {
                scroller.forceFinished(true);
                clearMessages();
                return true;
            }
            return false;
        }

        public boolean onScroll(MotionEvent e1, MotionEvent e2,
                                float distanceX, float distanceY) {
            startScrolling();
            doScroll((int) -distanceY);
            return true;
        }

        public boolean onFling(MotionEvent e1, MotionEvent e2, float velocityX,
                               float velocityY) {
            lastScrollY = (int) (currentItem * getItemHeight() + scrollingOffset);
            int maxY = (int) (isCyclic ? 0x7FFFFFFF : adapter.getItemsCount()
                    * getItemHeight());
            int minY = isCyclic ? -maxY : 0;
            scroller.fling(0, (int) lastScrollY, 0, (int) -velocityY / 2, 0, 0, minY,
                    maxY);
            setNextMessage(MESSAGE_SCROLL);
            return true;
        }
    };

    // Messages
    private final int MESSAGE_SCROLL = 0;
    private final int MESSAGE_JUSTIFY = 1;

    /**
     * Set next message to queue. Clears queue before.
     *
     * @param message the message to set
     */
    private void setNextMessage(int message) {
        clearMessages();
        animationHandler.sendEmptyMessage(message);
    }

    /**
     * Clears messages from queue
     */
    private void clearMessages() {
        animationHandler.removeMessages(MESSAGE_SCROLL);
        animationHandler.removeMessages(MESSAGE_JUSTIFY);
    }

    // animation handler
    private Handler animationHandler = new Handler() {
        public void handleMessage(Message msg) {
            scroller.computeScrollOffset();
            int currY = scroller.getCurrY();
            float delta = lastScrollY - currY;
            lastScrollY = currY;
            if (delta != 0) {
                doScroll(delta);
            }

            // scrolling is not finished when it comes to final Y
            // so, finish it manually
            if (Math.abs(currY - scroller.getFinalY()) < MIN_DELTA_FOR_SCROLLING) {
                currY = scroller.getFinalY();
                scroller.forceFinished(true);
            }
            if (!scroller.isFinished()) {
                animationHandler.sendEmptyMessage(msg.what);
            } else if (msg.what == MESSAGE_SCROLL) {
                justify();
            } else {
                finishScrolling();
            }
        }
    };

    /**
     * Justifies wheel
     */
    private void justify() {
        if (adapter == null) {
            return;
        }

        lastScrollY = 0;
        float offset = scrollingOffset;
        float itemHeight = getItemHeight();
        boolean needToIncrease = offset > 0 ? currentItem < adapter
                .getItemsCount() : currentItem > 0;
        if ((isCyclic || needToIncrease)
                && Math.abs((float) offset) > (float) itemHeight / 2) {
            if (offset < 0)
                offset += itemHeight + MIN_DELTA_FOR_SCROLLING;
            else
                offset -= itemHeight + MIN_DELTA_FOR_SCROLLING;
        }
        if (Math.abs(offset) > MIN_DELTA_FOR_SCROLLING) {
            scroller.startScroll(0, 0, 0, (int) offset, SCROLLING_DURATION);
            setNextMessage(MESSAGE_JUSTIFY);
        } else {
            finishScrolling();
        }
    }

    /**
     * Starts scrolling
     */
    private void startScrolling() {
        if (!isScrollingPerformed) {
            isScrollingPerformed = true;
            notifyScrollingListenersAboutStart();
        }
    }

    /**
     * Finishes scrolling
     */
    void finishScrolling() {
        if (isScrollingPerformed) {
            notifyScrollingListenersAboutEnd();
            isScrollingPerformed = false;
        }

        invalidate();
    }

    /**
     * Scroll the wheel
     *
     * @param
     * @param time        scrolling duration
     */
    public void scroll(int itemsToScroll, int time) {
        scroller.forceFinished(true);
        lastScrollY = scrollingOffset;
        float offset = itemsToScroll * getItemHeight();
        scroller.startScroll(0, (int) lastScrollY, 0, (int) (offset - lastScrollY), time);
        setNextMessage(MESSAGE_SCROLL);
        startScrolling();
    }
}
