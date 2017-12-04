package com.afap.autoshift.model;

import java.io.Serializable;
import java.util.List;

public class PlatformInfo implements Serializable{

    public String platformName;
    private List<Coin> anchorCoins;


    public PlatformInfo(String name) {
        this.platformName = name;
    }


    public List<Coin> getAnchorCoins() {
        return anchorCoins;
    }

    public void setAnchorCoins(List<Coin> anchorCoins) {
        this.anchorCoins = anchorCoins;
    }


}
