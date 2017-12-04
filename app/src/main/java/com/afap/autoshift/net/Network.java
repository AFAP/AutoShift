package com.afap.autoshift.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static BittrexService mBittrexService;


    private static MAPIService yunbiApis;
    private static ShapeShiftAPIService shapeShiftApis;

    public static MAPIService getYunbiAPIService() {
        if (yunbiApis == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.bitfinex.com/v2/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            yunbiApis = retrofit.create(MAPIService.class);
        }
        return yunbiApis;
    }

    public static ShapeShiftAPIService getShapeShiftAPIService() {
        if (shapeShiftApis == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://shapeshift.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            shapeShiftApis = retrofit.create(ShapeShiftAPIService.class);
        }
        return shapeShiftApis;
    }

    public static BittrexService getBittrexService() {
        if (mBittrexService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://bittrex.com/api/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            mBittrexService = retrofit.create(BittrexService.class);
        }
        return mBittrexService;
    }


}