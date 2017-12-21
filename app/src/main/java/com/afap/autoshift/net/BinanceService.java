package com.afap.autoshift.net;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface BinanceService {

    /**
     * 获取指定市场的买卖深度
     *
     */
    // https://api.binance.com/
    @GET("api/v1/depth?limit=20")
    Observable<JsonObject> getDepth(@Query("symbol") String symbol);

}
