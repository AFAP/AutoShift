package com.afap.autoshift.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface PoloniexService {


    /**
     * 获取指定市场的买卖深度
     *
     * @param market 数字货币类型 BTC-SC
     */
    @GET("public?command=returnOrderBook&depth=10")
    Observable<JsonObject> getDepth(@Query("currencyPair") String market );

}
