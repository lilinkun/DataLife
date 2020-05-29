package com.datalife.datalife.widget;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.List;

/**
 * Created by LG on 2018/3/14.
 */

public class CustomXValueFormatter implements IAxisValueFormatter{
    private List<String> labels;

    /**
     * @param labels 要显示的标签字符数组
     */
    public CustomXValueFormatter(List<String> labels) {
        this.labels = labels;
    }

    @Override
    public String getFormattedValue(float value, AxisBase axis) {

        return labels.get((int) value % labels.size());
    }
}
