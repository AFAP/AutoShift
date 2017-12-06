package com.afap.autoshift.net;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface HitBtcService {


    /**
     * 获取指定市场的买卖深度
     *
     * @param key 数字货币类型 SCBTC
     */
    // https://api.hitbtc.com/api/2/public/orderbook/SCBTC
    @GET("orderbook/{key}")
    Observable<JsonObject> getDepth(@Path("key") String key);

}
