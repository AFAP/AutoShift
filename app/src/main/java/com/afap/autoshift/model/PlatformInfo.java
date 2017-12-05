package com.afap.autoshift.model;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

public class PlatformInfo implements Serializable {

    public String platformName;
    private ArrayList<Coin> anchorCoins = new ArrayList<>();
    private Map<String, ArrayList<Coin>> mCoinMap = new HashMap<>();

    public PlatformInfo(String name) {
        this.platformName = name;
    }


    public List<Coin> getAnchorCoins() {
        return anchorCoins;
    }

    public void addAnchorCoin(Coin anchorCoin) {
        this.anchorCoins.add(anchorCoin);
    }

    public Map<String, ArrayList<Coin>> getmCoinMap() {
        return mCoinMap;
    }


    public static PlatformInfo parseInfo(JSONObject obj) {
        PlatformInfo platform = new PlatformInfo(obj.optString("name"));
        JSONArray anchorArr = obj.optJSONArray("anchorCoins");
        // 解析锚定货币
        for (int i = 0; i < anchorArr.length(); i++) {
            JSONObject coinObj = anchorArr.optJSONObject(i);
            platform.addAnchorCoin(Coin.parseCoin(coinObj));
        }

        // 解析各个锚定货币对应的其他货币
        JSONObject marketObj = obj.optJSONObject("markets");
        for (Iterator<String> it = marketObj.keys(); it.hasNext(); ) {
            String anchor = it.next();
            platform.getmCoinMap().put(anchor, new ArrayList<Coin>());
            JSONArray arr = marketObj.optJSONArray(anchor);

            for (int i = 0; i < arr.length(); i++) {
                platform.getmCoinMap().get(anchor).add(Coin.parseCoin(arr.optJSONObject(i)));
            }
        }


        return platform;
    }

}
