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

public interface MarketService {

    Observable<JsonObject> getDepth( String cat);



}
