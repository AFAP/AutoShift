package com.afap.autoshift.net;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface GateService {


    /**
     * 获取指定市场的买卖深度
     */
    // http://data.gate.io/api2/1/orderBook/zec_usdt
    @GET("api2/1/orderBook/{symbol}")
    Observable<JsonObject> getDepth(@Path("symbol") String symbol);

}
