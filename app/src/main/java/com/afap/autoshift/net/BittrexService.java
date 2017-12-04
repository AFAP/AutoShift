package com.afap.autoshift.net;

import com.google.gson.JsonArray;

import retrofit2.http.GET;
import retrofit2.http.Query;
import rx.Observable;

public interface BittrexService {


    /**
     * 获取指定市场的买卖深度
     *
     * @param market 数字货币类型 BTC-SC
     */
    //book/{cat}/P0
    @GET("v1.1/public/getorderbook")
    Observable<JsonArray> getDepth(@Query("market") String market, @Query("type") String type);

}
