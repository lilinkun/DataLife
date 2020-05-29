package com.datalife.datalife.bean;

/**
 * Created by LG on 2018/10/12.
 */

public class BrushUseCount {

    private int MachineUseTotal;
    private int DayUse;
    private String ToothbrushName;

    public int getMachineUseTotal() {
        return MachineUseTotal;
    }

    public void setMachineUseTotal(int machineUseTotal) {
        MachineUseTotal = machineUseTotal;
    }

    public int getDayUse() {
        return DayUse;
    }

    public void setDayUse(int dayUse) {
        DayUse = dayUse;
    }

    public String getToothbrushName() {
        return ToothbrushName;
    }

    public void setToothbrushName(String toothbrushName) {
        ToothbrushName = toothbrushName;
    }
}
