package com.afap.autoshift.model;

import android.widget.TextView;

import com.afap.autoshift.R;

/**
 * A转换B的相关信息
 */

public class PairInfo {
    String name_a;
    int icon_a;
    String yunbi_a;
    double amount_a;

    String name_b;
    int icon_b;
    String yunbi_b;
    double amount_b;

    String shift_cat;

    /**
     * 以下是动态获取
     */
    private ShiftInfo shiftInfo;
    private boolean validA;
    private boolean validB;
    private Depth depthA;
    private Depth depthB;
    private double avage_a;
    private double avage_b;
    private double cost_a;
    private double earn_a;
    private double netProfit;
    private double netProfitRate;


    public PairInfo(String name_a, int icon_a, String yunbi_a, double amount_a, String name_b, int icon_b, String yunbi_b, double amount_b, String shift_cat) {
        this.name_a = name_a;
        this.icon_a = icon_a;
        this.yunbi_a = yunbi_a;
        this.amount_a = amount_a;
        this.name_b = name_b;
        this.icon_b = icon_b;
        this.yunbi_b = yunbi_b;
        this.amount_b = amount_b;
        this.shift_cat = shift_cat;
    }


    public String getName_a() {
        return name_a;
    }

    public int getIcon_a() {
        return icon_a;
    }

    public String getYunbi_a() {
        return yunbi_a;
    }

    public double getAmount_a() {
        return amount_a;
    }

    public String getName_b() {
        return name_b;
    }

    public int getIcon_b() {
        return icon_b;
    }

    public String getYunbi_b() {
        return yunbi_b;
    }

    public double getAmount_b() {
        return amount_b;
    }

    public String getShift_cat() {
        return shift_cat;
    }

    public void setAmount_a(double amount_a) {
        this.amount_a = amount_a;
    }

    public void setAmount_b(double amount_b) {
        this.amount_b = amount_b;
    }

    @Override
    public String toString() {
        return "PairInfo{" +
                "name_a='" + name_a + '\'' +
                ", icon_a=" + icon_a +
                ", yunbi_a='" + yunbi_a + '\'' +
                ", amount_a=" + amount_a +
                ", name_b='" + name_b + '\'' +
                ", icon_b=" + icon_b +
                ", yunbi_b='" + yunbi_b + '\'' +
                ", amount_b=" + amount_b +
                ", shift_cat='" + shift_cat + '\'' +
                '}';
    }

    public ShiftInfo getShiftInfo() {
        return shiftInfo;
    }

    public void setShiftInfo(ShiftInfo shiftInfo) {
        this.shiftInfo = shiftInfo;
    }

    public boolean isValidA() {
        return validA;
    }

    public void setValidA(boolean validA) {
        this.validA = validA;
    }

    public boolean isValidB() {
        return validB;
    }

    public void setValidB(boolean validB) {
        this.validB = validB;
    }

    public Depth getDepthA() {
        return depthA;
    }

    public void setDepthA(Depth depthA) {
        this.depthA = depthA;
    }

    public Depth getDepthB() {
        return depthB;
    }

    public void setDepthB(Depth depthB) {
        this.depthB = depthB;
    }

    public double getAvage_a() {
        return avage_a;
    }

    public void setAvage_a(double avage_a) {
        this.avage_a = avage_a;
    }

    public double getAvage_b() {
        return avage_b;
    }

    public void setAvage_b(double avage_b) {
        this.avage_b = avage_b;
    }

    public double getCost_a() {
        return cost_a;
    }

    public void setCost_a(double cost_a) {
        this.cost_a = cost_a;
    }

    public double getEarn_a() {
        return earn_a;
    }

    public void setEarn_a(double earn_a) {
        this.earn_a = earn_a;
    }

    public double getNetProfit() {
        return netProfit;
    }

    public void setNetProfit(double netProfit) {
        this.netProfit = netProfit;
    }

    public double getNetProfitRate() {
        return netProfitRate;
    }

    public void setNetProfitRate(double netProfitRate) {
        this.netProfitRate = netProfitRate;
    }
}
