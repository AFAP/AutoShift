package com.afap.autoshift.model;

/**
 * A转换B的相关信息
 */

public class CoinPair {

    private Coin coin1;
    private Coin coin2;
    private Coin anchorCoin;
    private boolean validA;
    private boolean validB;
    public String platform1;
    public String platform2;


    /**
     * 以下是动态获取
     */
    private Depth depthA;
    private Depth depthB;
    private double avage_a;
    private double avage_b;
    private double cost_a;
    private double earn_a;
    private double netProfit;
    private double netProfitRate;


    public CoinPair(Coin coin1, String p1, Coin coin2, String p2,Coin anchorCoin) {
        this.coin1 = coin1;
        this.coin2 = coin2;
        this.platform1 = p1;
        this.platform2 = p2;
        this.anchorCoin = anchorCoin;
    }

    public Coin getAnchorCoin() {
        return anchorCoin;
    }

    public void setAnchorCoin(Coin anchorCoin) {
        this.anchorCoin = anchorCoin;
    }

    public void setValidA(boolean validA) {
        this.validA = validA;
    }

    public boolean isValidA() {
        return validA;
    }public boolean isValidB() {
        return validB;
    }

    public void setValidB(boolean validB) {
        this.validB = validB;
    }

    public Coin getCoin1() {
        return coin1;
    }

    public void setCoin1(Coin coin1) {
        this.coin1 = coin1;
    }

    public Coin getCoin2() {
        return coin2;
    }

    public void setCoin2(Coin coin2) {
        this.coin2 = coin2;
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
