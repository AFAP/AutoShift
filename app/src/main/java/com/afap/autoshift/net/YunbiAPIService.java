package com.afap.autoshift.net;

import com.google.gson.JsonObject;

import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import rx.Observable;

public interface YunbiAPIService {

//https://yunbi.com/api/v2/order_book.json?market=sccny


    /**
     * 获取指定市场的订单
     */
    @GET("api/v2/order_book.json")
    Observable<JsonObject> getOrderBook(@Query("market") String market);


    /**
     * 注册百度推送
     *
     * @param user_id     用户id
     * @param channel_id  渠道id
     * @param bd_user_id  百度用户id
     * @param device_type 设备类型 3 android
     */
    @POST("/api.php?act=tuisong_user_bind")
    @FormUrlEncoded
    Observable<JsonObject> registerUser2Push(
            @Field("user_id") String user_id,
            @Field("channel_id") String channel_id,
            @Field("bd_user_id") String bd_user_id,
            @Field("device_type") String device_type);
}
