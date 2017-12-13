package com.afap.autoshift.net;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface HuobiService {


    /**
     * 获取指定市场的买卖深度
     */
    // https://api.huobi.pro/market/depth?symbol=zecusdt&type=step0
    @GET("/market/depth?type=step0")
    Observable<JsonObject> getDepth(@Query("symbol") String symbol);

}
