package com.afap.autoshift.model;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * 市场深度
 */
public class Depth {
    private List<DepthOrder> asks; // 卖单
    private List<DepthOrder> bids; // 买单

    public List<DepthOrder> getAsks() {
        return asks;
    }

    public void setAsks(List<DepthOrder> asks) {
        this.asks = asks;
    }

    public List<DepthOrder> getBids() {
        return bids;
    }

    public void setBids(List<DepthOrder> bids) {
        this.bids = bids;
    }

    public static Depth parseFromJson(JsonArray array) {


        Depth depth = new Depth();
        List<DepthOrder> asks = new ArrayList<>();

        for (int i = 25; i < 50; i++) {
            JsonArray arr = array.get(i).getAsJsonArray();
            double price = arr.get(0).getAsDouble();
            double amount =  Math.abs(arr.get(2).getAsDouble());

            DepthOrder order = new DepthOrder(price, amount);
            asks.add(order);
        }

        List<DepthOrder> bids = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            JsonArray arr = array.get(i).getAsJsonArray();
            double price = arr.get(0).getAsDouble();
            double amount = Math.abs(arr.get(2).getAsDouble()) ;

            DepthOrder order = new DepthOrder(price, amount);
            bids.add(order);
        }

        Collections.sort(asks, new Comparator<DepthOrder>() {
            @Override
            public int compare(DepthOrder o1, DepthOrder o2) {
                return o1.getPrice() > o2.getPrice() ? 1 : -1;
            }
        });
        Collections.sort(bids, new Comparator<DepthOrder>() {
            @Override
            public int compare(DepthOrder o1, DepthOrder o2) {
                return o1.getPrice() > o2.getPrice() ? -1 : 1;
            }
        });

        depth.setAsks(asks);
        depth.setBids(bids);
        return depth;
    }

}
