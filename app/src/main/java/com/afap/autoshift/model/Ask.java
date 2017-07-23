package com.afap.autoshift.model;

import com.google.gson.JsonObject;

/**
 * Created by atom on 2017/7/5.
 * 询价订单
 */
public class Ask {

    /**
     * id : 448529979
     * side : sell
     * ord_type : limit
     * price : 0.086
     * avg_price : 0.086
     * state : wait
     * market : sccny
     * created_at : 2017-07-05T08:38:47Z
     * volume : 3398411.0
     * remaining_volume : 2950593.0
     * executed_volume : 447818.0
     * trades_count : 12
     */

    private int id;
    private String side;
    private String ord_type;
    private String price;
    private String avg_price;
    private String state;
    private String market;
    private String created_at;
    private String volume;
    private String remaining_volume;
    private String executed_volume;
    private int trades_count;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSide() {
        return side;
    }

    public void setSide(String side) {
        this.side = side;
    }

    public String getOrd_type() {
        return ord_type;
    }

    public void setOrd_type(String ord_type) {
        this.ord_type = ord_type;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getAvg_price() {
        return avg_price;
    }

    public void setAvg_price(String avg_price) {
        this.avg_price = avg_price;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getMarket() {
        return market;
    }

    public void setMarket(String market) {
        this.market = market;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public String getVolume() {
        return volume;
    }

    public void setVolume(String volume) {
        this.volume = volume;
    }

    public String getRemaining_volume() {
        return remaining_volume;
    }

    public void setRemaining_volume(String remaining_volume) {
        this.remaining_volume = remaining_volume;
    }

    public String getExecuted_volume() {
        return executed_volume;
    }

    public void setExecuted_volume(String executed_volume) {
        this.executed_volume = executed_volume;
    }

    public int getTrades_count() {
        return trades_count;
    }

    public void setTrades_count(int trades_count) {
        this.trades_count = trades_count;
    }


    public static Ask parseFromJson(JsonObject json) {
        Ask ask = new Ask();


        return ask;
    }

}
