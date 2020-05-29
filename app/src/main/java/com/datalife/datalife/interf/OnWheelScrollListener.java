package com.datalife.datalife.interf;

import com.datalife.datalife.widget.WheelView;

/**
 * Created by LG on 2018/1/28.
 */

public interface OnWheelScrollListener {
    /**
     * Callback method to be invoked when scrolling started.
     *
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingStarted(WheelView wheel);

    /**
     * Callback method to be invoked when scrolling ended.
     *
     * @param wheel the wheel view whose state has changed.
     */
    void onScrollingFinished(WheelView wheel);
}
