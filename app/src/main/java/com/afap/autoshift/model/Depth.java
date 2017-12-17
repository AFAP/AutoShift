package com.afap.autoshift.model;

import com.afap.autoshift.Config;
import com.afap.autoshift.net.Network;
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
    private List<DepthOrder> sells; // 卖单
    private List<DepthOrder> buys; // 买单

    public List<DepthOrder> getSells() {
        return sells;
    }

    public void setSells(List<DepthOrder> sells) {
        this.sells = sells;
    }

    public List<DepthOrder> getBuys() {
        return buys;
    }

    public void setBuys(List<DepthOrder> buys) {
        this.buys = buys;
    }

    public static Depth parseFromJson(JsonArray array) {
        Depth depth = new Depth();
        List<DepthOrder> sells = new ArrayList<>();

        for (int i = 25; i < 50; i++) {
            JsonArray arr = array.get(i).getAsJsonArray();
            double price = arr.get(0).getAsDouble();
            double amount = Math.abs(arr.get(2).getAsDouble());

            DepthOrder order = new DepthOrder(price, amount);
            sells.add(order);
        }

        List<DepthOrder> buys = new ArrayList<>();

        for (int i = 0; i < 25; i++) {
            JsonArray arr = array.get(i).getAsJsonArray();
            double price = arr.get(0).getAsDouble();
            double amount = Math.abs(arr.get(2).getAsDouble());

            DepthOrder order = new DepthOrder(price, amount);
            buys.add(order);
        }

        Collections.sort(sells, new Comparator<DepthOrder>() {
            @Override
            public int compare(DepthOrder o1, DepthOrder o2) {
                return o1.getPrice() > o2.getPrice() ? 1 : -1;
            }
        });
        Collections.sort(buys, new Comparator<DepthOrder>() {
            @Override
            public int compare(DepthOrder o1, DepthOrder o2) {
                return o1.getPrice() > o2.getPrice() ? -1 : 1;
            }
        });

        depth.setSells(sells);
        depth.setBuys(buys);
        return depth;
    }

    public static Depth parseFromJson(JsonObject jsonObject, String platform) {
        Depth depth = new Depth();
        List<DepthOrder> buys = new ArrayList<>();
        List<DepthOrder> sells = new ArrayList<>();

        switch (platform) {
            case Config.PLATFORM_BITTREX:
                parseBittres(jsonObject, buys, sells);
                break;
            case Config.PLATFORM_POLONIEX:
                parsePoloniex(jsonObject, buys, sells);
                break;
            case Config.PLATFORM_HITBTC:
                parseHitbtc(jsonObject, buys, sells);
                break;
            case Config.PLATFORM_BITFINEX:
                parseBitfinex(jsonObject, buys, sells);
                break;
            case Config.PLATFORM_HUOBI:
                parseHuobi(jsonObject, buys, sells);
                break;
            case Config.PLATFORM_GATE:
                parseGate(jsonObject, buys, sells);
                break;
            default:
                return null;
        }


        Collections.sort(sells, new Comparator<DepthOrder>() {
            @Override
            public int compare(DepthOrder o1, DepthOrder o2) {
                return o1.getPrice() > o2.getPrice() ? 1 : -1;
            }
        });
        Collections.sort(buys, new Comparator<DepthOrder>() {
            @Override
            public int compare(DepthOrder o1, DepthOrder o2) {
                return o1.getPrice() > o2.getPrice() ? -1 : 1;
            }
        });
        depth.setSells(sells);
        depth.setBuys(buys);
        return depth;
    }


    private static void parseBittres(JsonObject jsonObject, List<DepthOrder> buys, List<DepthOrder> sells) {
        JsonArray buyArr = jsonObject.getAsJsonObject("result").getAsJsonArray("buy");
        int length = Math.min(buyArr.size(), Config.SIZE_DEPTH);

        for (int i = 0; i < length; i++) {
            JsonObject obj = buyArr.get(i).getAsJsonObject();
            double price = obj.get("Rate").getAsDouble();
            double amount = obj.get("Quantity").getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            buys.add(order);
        }

        JsonArray sellArr = jsonObject.getAsJsonObject("result").getAsJsonArray("sell");
        length = Math.min(sellArr.size(), Config.SIZE_DEPTH);
        for (int i = 0; i < length; i++) {
            JsonObject obj = sellArr.get(i).getAsJsonObject();
            double price = obj.get("Rate").getAsDouble();
            double amount = obj.get("Quantity").getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            sells.add(order);
        }
    }

    private static void parsePoloniex(JsonObject jsonObject, List<DepthOrder> buys, List<DepthOrder> sells) {
        JsonArray buyArr = jsonObject.getAsJsonArray("bids");
        for (int i = 0; i < buyArr.size(); i++) {
            JsonArray obj = buyArr.get(i).getAsJsonArray();
            double price = obj.get(0).getAsDouble();
            double amount = obj.get(1).getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            buys.add(order);
        }


        JsonArray sellArr = jsonObject.getAsJsonArray("asks");
        for (int i = 0; i < sellArr.size(); i++) {
            JsonArray obj = sellArr.get(i).getAsJsonArray();
            double price = obj.get(0).getAsDouble();
            double amount = obj.get(1).getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            sells.add(order);
        }
    }

    private static void parseHitbtc(JsonObject jsonObject, List<DepthOrder> buys, List<DepthOrder> sells) {
        JsonArray buyArr = jsonObject.getAsJsonArray("bid");
        int length = Math.min(buyArr.size(), Config.SIZE_DEPTH);

        for (int i = 0; i < length; i++) {
            JsonObject obj = buyArr.get(i).getAsJsonObject();
            double price = obj.get("price").getAsDouble();
            double amount = obj.get("size").getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            buys.add(order);
        }

        JsonArray sellArr = jsonObject.getAsJsonArray("ask");
        length = Math.min(sellArr.size(), Config.SIZE_DEPTH);
        for (int i = 0; i < length; i++) {
            JsonObject obj = sellArr.get(i).getAsJsonObject();
            double price = obj.get("price").getAsDouble();
            double amount = obj.get("size").getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            sells.add(order);
        }
    }

    private static void parseBitfinex(JsonObject jsonObject, List<DepthOrder> buys, List<DepthOrder> sells) {
        JsonArray array = jsonObject.getAsJsonArray("result");
        for (int i = 25; i < 50; i++) {
            JsonArray arr = array.get(i).getAsJsonArray();
            double price = arr.get(0).getAsDouble();
            double amount = Math.abs(arr.get(2).getAsDouble());

            DepthOrder order = new DepthOrder(price, amount);
            sells.add(order);
        }

        for (int i = 0; i < 25; i++) {
            JsonArray arr = array.get(i).getAsJsonArray();
            double price = arr.get(0).getAsDouble();
            double amount = Math.abs(arr.get(2).getAsDouble());

            DepthOrder order = new DepthOrder(price, amount);
            buys.add(order);
        }
    }

    private static void parseHuobi(JsonObject jsonObject, List<DepthOrder> buys, List<DepthOrder> sells) {
        JsonObject tickObj = jsonObject.getAsJsonObject("tick");

        JsonArray buyArr = tickObj.getAsJsonArray("bids");
        for (int i = 0; i < buyArr.size(); i++) {
            JsonArray obj = buyArr.get(i).getAsJsonArray();
            double price = obj.get(0).getAsDouble();
            double amount = obj.get(1).getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            buys.add(order);
        }


        JsonArray sellArr = tickObj.getAsJsonArray("asks");
        for (int i = 0; i < sellArr.size(); i++) {
            JsonArray obj = sellArr.get(i).getAsJsonArray();
            double price = obj.get(0).getAsDouble();
            double amount = obj.get(1).getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            sells.add(order);
        }
    }

    private static void parseGate(JsonObject jsonObject, List<DepthOrder> buys, List<DepthOrder> sells) {

        JsonArray buyArr = jsonObject.getAsJsonArray("bids");
        for (int i = 0; i < buyArr.size(); i++) {
            JsonArray obj = buyArr.get(i).getAsJsonArray();
            double price = obj.get(0).getAsDouble();
            double amount = obj.get(1).getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            buys.add(order);
        }


        JsonArray sellArr = jsonObject.getAsJsonArray("asks");
        for (int i = 0; i < sellArr.size(); i++) {
            JsonArray obj = sellArr.get(i).getAsJsonArray();
            double price = obj.get(0).getAsDouble();
            double amount = obj.get(1).getAsDouble();

            DepthOrder order = new DepthOrder(price, amount);
            sells.add(order);
        }
    }


}
