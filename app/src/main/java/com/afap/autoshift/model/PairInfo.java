package com.afap.autoshift.model;

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
}
