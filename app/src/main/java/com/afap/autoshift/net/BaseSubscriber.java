package com.afap.autoshift.net;


import com.afap.autoshift.utils.LogUtil;
import com.afap.utils.ToastUtil;

import java.net.SocketTimeoutException;

import rx.Subscriber;

/**
 * 基础的Subscriber，主要是统一处理onError中的异常情况
 */
public class BaseSubscriber<T> extends Subscriber<T> {
    private final static String TAG = "BaseSubscriber";

    @Override
    public void onCompleted() {
        LogUtil.i(TAG, "---->onCompleted");
    }

    @Override
    public void onError(Throwable error) {
        LogUtil.i(TAG, "---->onError");
        error.printStackTrace();
        if (error instanceof SocketTimeoutException) {
            ToastUtil.showShort("连接超时，请检查您的网络情况");
        } else {
            ToastUtil.showShort("请检查网络");

        }
    }

    @Override
    public void onNext(T t) {
        LogUtil.i(TAG, "---->onNext");
    }
}
