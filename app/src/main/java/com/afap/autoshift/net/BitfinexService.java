package com.afap.autoshift.net;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
import rx.Observable;

public interface BitfinexService {

    /**
     * 获取指定市场的买卖深度
     *
     * @param cat 数字货币类型
     */
    @GET("book/{cat}/P0")
    Observable<JsonArray> getDepth(@Path("cat") String cat);

}
