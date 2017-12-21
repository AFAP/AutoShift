package com.afap.autoshift.net;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;

public class Network {
    private static BittrexService mBittrexService;
    private static PoloniexService mPoloniexService;
    private static HitBtcService mHitBtcService;
    private static BitfinexService mBitfinexService;
    private static HuobiService mHuobiService;
    private static GateService mGateService;
    private static BinanceService mBinanceService;





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
    public static PoloniexService getPoloniexService() {
        if (mPoloniexService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://poloniex.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            mPoloniexService = retrofit.create(PoloniexService.class);
        }
        return mPoloniexService;
    }

    public static HitBtcService getHitBtcService() {
        if (mHitBtcService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.hitbtc.com/api/2/public/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            mHitBtcService = retrofit.create(HitBtcService.class);
        }
        return mHitBtcService;
    }

    public static BitfinexService getBitfinexService() {
        if (mBitfinexService == null) {
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
            mBitfinexService = retrofit.create(BitfinexService.class);
        }
        return mBitfinexService;
    }

    public static HuobiService getHuobiService() {
        if (mHuobiService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.huobi.pro")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            mHuobiService = retrofit.create(HuobiService.class);
        }
        return mHuobiService;
    }

    public static GateService getGateService() {
        if (mGateService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://data.gate.io/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            mGateService = retrofit.create(GateService.class);
        }
        return mGateService;
    }

    public static BinanceService getBinanceService() {
        if (mBinanceService == null) {
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

            // OkHttp3.0的使用方式
            OkHttpClient okHttpClient = new OkHttpClient.Builder()
                    .retryOnConnectionFailure(true)
                    .addInterceptor(loggingInterceptor) // TODO 最后关闭日志
                    .build();

            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.binance.com/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                    .client(okHttpClient)
                    .build();
            mBinanceService = retrofit.create(BinanceService.class);
        }
        return mBinanceService;
    }


}