package com.afap.autoshift.model;

import com.google.gson.JsonObject;

/**
 * Created by CCL on 2017/7/23.
 */

public class ShiftInfo {
    private String pair;
    private double rate;
    private double minerFee;
    private double limit;
    private double minimum;
    private double maxLimit;


    public String getPair() {
        return pair;
    }

    public void setPair(String pair) {
        this.pair = pair;
    }

    public double getRate() {
        return rate;
    }

    public void setRate(double rate) {
        this.rate = rate;
    }

    public double getMinerFee() {
        return minerFee;
    }

    public void setMinerFee(double minerFee) {
        this.minerFee = minerFee;
    }

    public double getLimit() {
        return limit;
    }

    public void setLimit(double limit) {
        this.limit = limit;
    }

    public double getMinimum() {
        return minimum;
    }

    public void setMinimum(double minimum) {
        this.minimum = minimum;
    }

    public double getMaxLimit() {
        return maxLimit;
    }

    public void setMaxLimit(double maxLimit) {
        this.maxLimit = maxLimit;
    }


    public static ShiftInfo parseFromJson(JsonObject json) {
        ShiftInfo shiftInfo = new ShiftInfo();
        shiftInfo.setPair(json.get("pair").getAsString());
        shiftInfo.setRate(json.get("rate").getAsDouble());
        shiftInfo.setMinerFee(json.get("minerFee").getAsDouble());
        shiftInfo.setLimit(json.get("limit").getAsDouble());
        shiftInfo.setMinimum(json.get("minimum").getAsDouble());
        shiftInfo.setMaxLimit(json.get("maxLimit").getAsDouble());

        return shiftInfo;
    }

}
