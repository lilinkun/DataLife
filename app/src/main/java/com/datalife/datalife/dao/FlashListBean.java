package com.datalife.datalife.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by LG on 2018/3/30.
 * 推荐广告
 */
@Entity
public class FlashListBean {
    int F_FlashID;
    String F_FlashName;
    String F_FlashPic;
    int F_FlashOrder;
    String F_FlashUrl;
    String F_addDate;
    boolean F_Display;
    int F_Style;
    String F_BackColor;

    @Generated(hash = 731919522)
    public FlashListBean(int F_FlashID, String F_FlashName, String F_FlashPic,
            int F_FlashOrder, String F_FlashUrl, String F_addDate,
            boolean F_Display, int F_Style, String F_BackColor) {
        this.F_FlashID = F_FlashID;
        this.F_FlashName = F_FlashName;
        this.F_FlashPic = F_FlashPic;
        this.F_FlashOrder = F_FlashOrder;
        this.F_FlashUrl = F_FlashUrl;
        this.F_addDate = F_addDate;
        this.F_Display = F_Display;
        this.F_Style = F_Style;
        this.F_BackColor = F_BackColor;
    }

    @Generated(hash = 1306099)
    public FlashListBean() {
    }

    public int getF_FlashID() {
        return F_FlashID;
    }

    public void setF_FlashID(int f_FlashID) {
        F_FlashID = f_FlashID;
    }

    public String getF_FlashName() {
        return F_FlashName;
    }

    public void setF_FlashName(String f_FlashName) {
        F_FlashName = f_FlashName;
    }

    public String getF_FlashPic() {
        return F_FlashPic;
    }

    public void setF_FlashPic(String f_FlashPic) {
        F_FlashPic = f_FlashPic;
    }

    public int getF_FlashOrder() {
        return F_FlashOrder;
    }

    public void setF_FlashOrder(int f_FlashOrder) {
        F_FlashOrder = f_FlashOrder;
    }

    public String getF_FlashUrl() {
        return F_FlashUrl;
    }

    public void setF_FlashUrl(String f_FlashUrl) {
        F_FlashUrl = f_FlashUrl;
    }

    public String getF_addDate() {
        return F_addDate;
    }

    public void setF_addDate(String f_addDate) {
        F_addDate = f_addDate;
    }

    public boolean isF_Display() {
        return F_Display;
    }

    public void setF_Display(boolean f_Display) {
        F_Display = f_Display;
    }

    public int getF_Style() {
        return F_Style;
    }

    public void setF_Style(int f_Style) {
        F_Style = f_Style;
    }

    public String getF_BackColor() {
        return F_BackColor;
    }

    public void setF_BackColor(String f_BackColor) {
        F_BackColor = f_BackColor;
    }

    public boolean getF_Display() {
        return this.F_Display;
    }
}
