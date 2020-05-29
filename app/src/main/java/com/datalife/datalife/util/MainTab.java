package com.datalife.datalife.util;

import com.datalife.datalife.R;

/**
 * Created by LG on 2018/1/17.
 */

public enum MainTab {
    HOMEPAGE(0, R.string.apptab_homepage, R.mipmap.ic_launcher2),

    MALL(1, R.string.apptab_shopping_mall, R.mipmap.ic_launcher2),

    HEALTH(3, R.string.apptab_health_home, R.mipmap.ic_launcher2),

    ME(4, R.string.apptab_me, R.mipmap.ic_launcher2);

    private int idx;
    private int resName;
    private int resIcon;

    private MainTab(int idx, int resName, int resIcon) {
        this.idx = idx;
        this.resName = resName;
        this.resIcon = resIcon;
    }

    public int getIdx() {
        return idx;
    }

    public void setIdx(int idx) {
        this.idx = idx;
    }

    public int getResName() {
        return resName;
    }

    public void setResName(int resName) {
        this.resName = resName;
    }

    public int getResIcon() {
        return resIcon;
    }

    public void setResIcon(int resIcon) {
        this.resIcon = resIcon;
    }

}

