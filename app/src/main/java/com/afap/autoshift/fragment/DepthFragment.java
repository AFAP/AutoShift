package com.afap.autoshift.fragment;


import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.afap.autoshift.ListActivity;
import com.afap.autoshift.R;
import com.afap.autoshift.adapter.CoinPairViewAdapter;
import com.afap.autoshift.adapter.SimpleShiftViewAdapter;
import com.afap.autoshift.model.Coin;
import com.afap.autoshift.model.CoinPair;
import com.afap.autoshift.model.Depth;
import com.afap.autoshift.model.DepthOrder;
import com.afap.autoshift.model.PairInfo;
import com.afap.autoshift.model.PlatformInfo;
import com.afap.autoshift.net.BaseSubscriber;
import com.afap.autoshift.net.MarketService;
import com.afap.autoshift.net.Network;
import com.afap.autoshift.utils.LogUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class DepthFragment extends Fragment {
    public final static String KEY_PLATFORM1 = "key_platform1";
    public final static String KEY_PLATFORM2 = "key_platform2";


    private PlatformInfo mPlatform1;
    private PlatformInfo mPlatform2;

    private DecimalFormat df = new DecimalFormat("0.00000");
    private DecimalFormat df_net = new DecimalFormat("#.##%");

    private ListActivity.OnListInteractionListener mListener;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView mRecyclerView;
    private CoinPairViewAdapter mAdapter;

    private List<CoinPair> mValues;


    public static DepthFragment newInstance(PlatformInfo platform1, PlatformInfo platform2) {
        DepthFragment fragment = new DepthFragment();
        Bundle b = new Bundle();
        b.putSerializable(KEY_PLATFORM1, platform1);
        b.putSerializable(KEY_PLATFORM2, platform2);

        fragment.setArguments(b);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPlatform1 = (PlatformInfo) getArguments().getSerializable(KEY_PLATFORM1);
        mPlatform2 = (PlatformInfo) getArguments().getSerializable(KEY_PLATFORM2);


        mValues = new ArrayList<>();
        // 遍历锚定货币，只有双方平台采用同一种锚定货币时才会对比市场行情
        for (Coin anchorCoin1 : mPlatform1.getAnchorCoins()) {
            for (Coin anchorCoin2 : mPlatform2.getAnchorCoins()) {
                if (TextUtils.equals(anchorCoin1.getAlias(), anchorCoin2.getAlias())) {
                    List<Coin> coins1 = mPlatform1.getmCoinMap().get(anchorCoin1.getAlias());
                    List<Coin> coins2 = mPlatform2.getmCoinMap().get(anchorCoin2.getAlias());
                    // 遍历单个锚定货币支持的虚拟币种，同种币种互相兑换
                    for (Coin coin1 : coins1) {
                        for (Coin coin2 : coins2) {
                            if (TextUtils.equals(coin1.getAlias(), coin2.getAlias())) {
                                mValues.add(new CoinPair(coin1, mPlatform1.platformName, coin2, mPlatform2.platformName));
                                mValues.add(new CoinPair(coin2, mPlatform2.platformName, coin1, mPlatform1.platformName));
                            }
                        }
                    }

                }
            }
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle
            savedInstanceState) {
        return inflater.inflate(R.layout.fragment_list, null);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        initView();
        loadAll();
    }

    private void initView() {
        mListener = new ListActivity.OnListInteractionListener() {
            @Override
            public void onListFragmentInteraction(PairInfo pairInfo) {
                if (pairInfo.getDepthB() != null) {
                    //showDetail(pairInfo);
                }
            }
        };
        mSwipeRefreshLayout = getView().findViewById(R.id.swipeRefreshLayout);
        mSwipeRefreshLayout.setColorSchemeColors(Color.BLUE, Color.GREEN, Color.RED, Color.YELLOW);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                loadAll();
            }
        });
        mRecyclerView = getView().findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
        mAdapter = new CoinPairViewAdapter(mValues, mListener, getActivity());
        mRecyclerView.setAdapter(mAdapter);
    }


    private void loadAll() {
        for (int i = 0; i < mValues.size(); i++) {
            getDepth_A(mValues.get(i));
        }
        mSwipeRefreshLayout.setRefreshing(false);
    }


    private void getDepth_A(final CoinPair pair) {

        Observable<JsonObject> observable = null;

        switch (pair.platform1) {
            case "Bittrex":
                observable = Network.getBittrexService().getDepth(pair.getCoin1().getKey());
                break;
            case "Poloniex":
                observable = Network.getPoloniexService().getDepth(pair.getCoin1().getKey());
                break;
        }

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonArray) {
//                        LogUtil.i(pair.getYunbi_a(), jsonArray.toString());
//
//                        double total_amount = 0;
//                        double gap_amount = 0; // 深度缺口
//                        double total_pay_a = 0;
//
//                        Depth depth = Depth.parseFromJson(jsonArray);
//                        pair.setDepthA(depth);
//
//                        List<DepthOrder> asks = depth.getAsks();
//                        boolean flag = false;
//                        for (int i = 0; i < asks.size(); i++) {
//                            double price = asks.get(i).getPrice();
//                            double amount = asks.get(i).getAmount();
//
//
//                            gap_amount = amount_a - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
//                            total_amount = total_amount + amount; // 至当前深度，可买入的数量
//                            if (gap_amount <= 0) { //  至上一前深度，已经满足
//
//                            } else if (total_amount >= amount_a) { // 至当前深度，已经满足需要买入的数量
//                                total_pay_a = total_pay_a + gap_amount * price;
//                                flag = true;
//                            } else {
//                                total_pay_a = total_pay_a + amount * price;
//                            }
//                        }
//                        double avage_a = total_pay_a / amount_a;
//                        pair.setAvage_a(avage_a);
//                        pair.setValidA(flag);
//
//                        getBDepth_B(pair, amount_a, amount_b, total_pay_a);
                    }
                });
    }

}
