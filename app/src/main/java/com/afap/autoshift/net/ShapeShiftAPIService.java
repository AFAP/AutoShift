package com.afap.autoshift.net;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

public interface ShapeShiftAPIService {

    /**
     * 获取货币转化市场的信息
     */

    @GET("/marketinfo/{a_b}")
    Observable<JsonObject> getMarketInfo(@Path("a_b") String a_b);


}
