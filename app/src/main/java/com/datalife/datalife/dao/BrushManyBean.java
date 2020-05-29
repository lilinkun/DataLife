package com.datalife.datalife.dao;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by LG on 2018/10/12.
 */
@Entity
public class BrushManyBean {

    private int CheckValue1;
    private int CheckValue2;
    private int CheckValue3;
    private int CheckValue4;
    private int CheckValue5;
    private int CheckValue6;
    private int CheckValue7;
    private String MachineBindId;
    private String Machine_Sn;
    private String Project_Id;
    private String Member_Id;
    private String CreateDate;
    private String Machine_Id;

    @Generated(hash = 44624459)
    public BrushManyBean(int CheckValue1, int CheckValue2, int CheckValue3,
            int CheckValue4, int CheckValue5, int CheckValue6, int CheckValue7,
            String MachineBindId, String Machine_Sn, String Project_Id,
            String Member_Id, String CreateDate, String Machine_Id) {
        this.CheckValue1 = CheckValue1;
        this.CheckValue2 = CheckValue2;
        this.CheckValue3 = CheckValue3;
        this.CheckValue4 = CheckValue4;
        this.CheckValue5 = CheckValue5;
        this.CheckValue6 = CheckValue6;
        this.CheckValue7 = CheckValue7;
        this.MachineBindId = MachineBindId;
        this.Machine_Sn = Machine_Sn;
        this.Project_Id = Project_Id;
        this.Member_Id = Member_Id;
        this.CreateDate = CreateDate;
        this.Machine_Id = Machine_Id;
    }

    @Generated(hash = 402337661)
    public BrushManyBean() {
    }

    public int getCheckValue1() {
        return CheckValue1;
    }

    public void setCheckValue1(int checkValue1) {
        CheckValue1 = checkValue1;
    }

    public int getCheckValue2() {
        return CheckValue2;
    }

    public void setCheckValue2(int checkValue2) {
        CheckValue2 = checkValue2;
    }

    public int getCheckValue3() {
        return CheckValue3;
    }

    public void setCheckValue3(int checkValue3) {
        CheckValue3 = checkValue3;
    }

    public int getCheckValue4() {
        return CheckValue4;
    }

    public void setCheckValue4(int checkValue4) {
        CheckValue4 = checkValue4;
    }

    public int getCheckValue5() {
        return CheckValue5;
    }

    public void setCheckValue5(int checkValue5) {
        CheckValue5 = checkValue5;
    }

    public int getCheckValue6() {
        return CheckValue6;
    }

    public void setCheckValue6(int checkValue6) {
        CheckValue6 = checkValue6;
    }

    public int getCheckValue7() {
        return CheckValue7;
    }

    public void setCheckValue7(int checkValue7) {
        CheckValue7 = checkValue7;
    }

    public String getMachineBindId() {
        return MachineBindId;
    }

    public void setMachineBindId(String machineBindId) {
        MachineBindId = machineBindId;
    }

    public String getMachine_Sn() {
        return Machine_Sn;
    }

    public void setMachine_Sn(String machine_Sn) {
        Machine_Sn = machine_Sn;
    }

    public String getProject_Id() {
        return Project_Id;
    }

    public void setProject_Id(String project_Id) {
        Project_Id = project_Id;
    }

    public String getMember_Id() {
        return Member_Id;
    }

    public void setMember_Id(String member_Id) {
        Member_Id = member_Id;
    }

    public String getCreateDate() {
        return CreateDate;
    }

    public void setCreateDate(String createDate) {
        CreateDate = createDate;
    }

    public String getMachine_Id() {
        return Machine_Id;
    }

    public void setMachine_Id(String machine_Id) {
        Machine_Id = machine_Id;
    }
}
