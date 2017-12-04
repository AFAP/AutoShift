package com.afap.autoshift.model;


public class Coin {
    private String alias; // 别名，所有平台中同一个币种，名字一样
    private String name; // 各个平台中币的真实名字
    private int resId; // 图标ID

    public Coin(String alias, String name, int resId) {
        this.alias = alias;
        this.name = name;
        this.resId = resId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }
}
