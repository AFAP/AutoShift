package com.afap.autoshift.model;


import com.afap.autoshift.R;

import org.json.JSONObject;

import java.io.Serializable;

public class Coin implements Serializable {
    private String alias; // 别名，所有平台中同一个币种，名字一样
    private String key; // 各个平台中币的真实名字
    private double amount; // 默认数量
    private int resId; // 图标ID

    public Coin(String alias, String key, double amount, int resId) {
        this.alias = alias;
        this.key = key;
        this.amount = amount;
        this.resId = resId;
    }

    public String getAlias() {
        return alias;
    }

    public void setAlias(String alias) {
        this.alias = alias;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }

    public int getResId() {
        return resId;
    }

    public void setResId(int resId) {
        this.resId = resId;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public static Coin parseCoin(JSONObject obj) {

        String alias = obj.optString("alias");
        String key = obj.optString("key");
        double amount = obj.optDouble("amount");

        return new Coin(alias, key, amount, getIconByName(alias));
    }

    private static int getIconByName(String alias) {
        int resId = R.mipmap.coin_btc;
        switch (alias) {
            case "BTC":
                resId = R.mipmap.coin_btc;
                break;
            case "ETH":
                resId = R.mipmap.coin_eth;
                break;
            case "ZEC":
                resId = R.mipmap.coin_zec;
                break;
            case "SC":
                resId = R.mipmap.coin_sc;
                break;
        }
        return resId;
    }

    public Coin cloneSelf( ) {
        return new Coin(this.getAlias(), this.getKey(), this.getAmount(), this.getResId());
    }


}
