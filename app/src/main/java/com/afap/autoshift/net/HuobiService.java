package com.afap.autoshift.net;

import com.google.gson.JsonObject;

import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.Query;
import rx.Observable;

public interface HuobiService {


    /**
     * 获取指定市场的买卖深度
     */
    // https://api.huobi.pro/market/depth?symbol=zecusdt&type=step0
    @Headers({
            "User-Agent: Mozilla/5.0 (Linux; Android 7.0; ZUK Z2131 Build/NRD90M; wv) AppleWebKit/537.36 (KHTML, like Gecko) Version/4.0 Chrome/55.0.2883.91 Mobile Safari/537.36",
            "Content-Type: application/json",
            "Accept-Language: zh-CN"
    })
    @GET("/market/depth?type=step0")
    Observable<JsonObject> getDepth(@Query("symbol") String symbol);

}
