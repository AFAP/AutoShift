package com.afap.autoshift.fragment;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
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
import android.widget.TextView;

import com.afap.autoshift.Config;
import com.afap.autoshift.ListActivity;
import com.afap.autoshift.R;
import com.afap.autoshift.adapter.CoinPairViewAdapter;
import com.afap.autoshift.model.Coin;
import com.afap.autoshift.model.CoinPair;
import com.afap.autoshift.model.Depth;
import com.afap.autoshift.model.DepthOrder;
import com.afap.autoshift.model.PairInfo;
import com.afap.autoshift.model.PlatformInfo;
import com.afap.autoshift.net.BaseSubscriber;
import com.afap.autoshift.net.Network;
import com.afap.autoshift.utils.LogUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

public class DepthFragment extends Fragment {
    public final static String KEY_PLATFORM1 = "key_platform1";
    public final static String KEY_PLATFORM2 = "key_platform2";


    private PlatformInfo mPlatform1;
    private PlatformInfo mPlatform2;

    private DecimalFormat df = new DecimalFormat("0.00000000");
    private DecimalFormat df_net = new DecimalFormat("#.##%");

    private OnListInteractionListener mListener;
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

                                mValues.add(new CoinPair(coin1.cloneSelf(), mPlatform1.platformName, coin2.cloneSelf(),
                                        mPlatform2.platformName, anchorCoin1));
                                mValues.add(new CoinPair(coin2.cloneSelf(), mPlatform2.platformName, coin1.cloneSelf(),
                                        mPlatform1.platformName, anchorCoin1));
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
        mListener = new OnListInteractionListener() {
            @Override
            public void onListFragmentInteraction(CoinPair pairInfo) {
                if (pairInfo.getDepthB() != null) {
                    showDetail(pairInfo);
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


    /**
     * 获取待卖出货币的市场深度
     */
    private void getDepth_A(final CoinPair pair) {
        final String platform = pair.platform1;
        Observable<JsonObject> observable;
        switch (platform) {
            case Config.PLATFORM_BITTREX:
                observable = Network.getBittrexService().getDepth(pair.getCoin1().getKey());
                break;
            case Config.PLATFORM_POLONIEX:
                observable = Network.getPoloniexService().getDepth(pair.getCoin1().getKey());
                break;
            case Config.PLATFORM_HITBTC:
                observable = Network.getHitBtcService().getDepth(pair.getCoin1().getKey());
                break;
            case Config.PLATFORM_BITFINEX:
                observable = Network
                        .getBitfinexService()
                        .getDepth(pair.getCoin1().getKey())
                        .flatMap(new Func1<JsonArray, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(JsonArray jsonElements) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.add("result", jsonElements);
                                return Observable.just(jsonObject);
                            }
                        });
                break;
            case Config.PLATFORM_HUOBI:
                observable = Network.getHuobiService().getDepth(pair.getCoin1().getKey());
                break;
            case Config.PLATFORM_GATE:
                observable = Network.getGateService().getDepth(pair.getCoin1().getKey());
                break;
            default:
                return;
        }

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {

                        double amount_a = pair.getCoin1().getAmount();
                        double total_amount = 0;
                        double gap_amount = 0; // 深度缺口
                        double total_pay_a = 0;

                        Depth depth = Depth.parseFromJson(jsonObject, platform);
                        pair.setDepthA(depth);

                        List<DepthOrder> asks = depth.getSells();
                        boolean flag = false;
                        for (int i = 0; i < asks.size(); i++) {
                            double price = asks.get(i).getPrice();
                            double amount = asks.get(i).getAmount();


                            gap_amount = amount_a - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
                            total_amount = total_amount + amount; // 至当前深度，可买入的数量
                            if (gap_amount <= 0) { //  至上一前深度，已经满足

                            } else if (total_amount >= amount_a) { // 至当前深度，已经满足需要买入的数量
                                total_pay_a = total_pay_a + gap_amount * price;
                                flag = true;
                            } else {
                                total_pay_a = total_pay_a + amount * price;
                            }
                        }
                        double avage_a = total_pay_a / amount_a;
                        pair.setAvage_a(avage_a);
                        pair.setValidA(flag);

                        getDepth_B(pair, total_pay_a);
                    }
                });
    }

    private void getDepth_B(final CoinPair pair, final double total_pay_a) {
        final String platform = pair.platform2;
        Observable<JsonObject> observable;
        switch (platform) {
            case Config.PLATFORM_BITTREX:
                observable = Network.getBittrexService().getDepth(pair.getCoin2().getKey());
                break;
            case Config.PLATFORM_POLONIEX:
                observable = Network.getPoloniexService().getDepth(pair.getCoin2().getKey());
                break;
            case Config.PLATFORM_HITBTC:
                observable = Network.getHitBtcService().getDepth(pair.getCoin2().getKey());
                break;
            case Config.PLATFORM_BITFINEX:
                observable = Network
                        .getBitfinexService()
                        .getDepth(pair.getCoin2().getKey())
                        .flatMap(new Func1<JsonArray, Observable<JsonObject>>() {
                            @Override
                            public Observable<JsonObject> call(JsonArray jsonElements) {
                                JsonObject jsonObject = new JsonObject();
                                jsonObject.add("result", jsonElements);
                                return Observable.just(jsonObject);
                            }
                        });
                break;
            case Config.PLATFORM_HUOBI:
                observable = Network.getHuobiService().getDepth(pair.getCoin2().getKey());
                break;
            case Config.PLATFORM_GATE:
                observable = Network.getGateService().getDepth(pair.getCoin2().getKey());
                break;
            default:
                return;
        }

        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {

                        pair.getCoin2().setAmount(pair.getCoin1().getAmount()); // TODO 这里注意下
                        double amount_b = pair.getCoin2().getAmount();
                        double total_amount = 0;
                        double gap_amount = 0; // 深度缺口
                        double total_earn_b = 0;

                        Depth depth = Depth.parseFromJson(jsonObject, platform);
                        pair.setDepthB(depth);


                        List<DepthOrder> bids = depth.getBuys();

                        boolean flag = false;
                        for (int i = 0; i < bids.size(); i++) {
                            double price = bids.get(i).getPrice();
                            double amount = bids.get(i).getAmount();


                            gap_amount = amount_b - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
                            total_amount = total_amount + amount; // 至当前深度，可买入的数量
                            if (gap_amount <= 0) { //  至上一前深度，已经满足

                            } else if (total_amount >= amount_b) { // 至当前深度，已经满足需要买入的数量
                                total_earn_b = total_earn_b + gap_amount * price;
                                flag = true;
                            } else {
                                total_earn_b = total_earn_b + amount * price;
                            }

                        }
                        double avage_b = total_earn_b / amount_b;
                        pair.setAvage_b(avage_b);
                        pair.setValidB(flag);


                        double net = total_earn_b - total_pay_a - 0.0025 * (total_earn_b + total_pay_a);
                        double netrate = net / total_pay_a;

                        pair.setNetProfit(net);
                        pair.setNetProfitRate(netrate);
                        Collections.sort(mValues, new Comparator<CoinPair>() {

                            @Override
                            public int compare(CoinPair o1, CoinPair o2) {
                                return o1.getNetProfitRate() > o2.getNetProfitRate() ? -1 : 1;
                            }
                        });

                        mAdapter.notifyDataSetChanged();
                    }
                });
    }


    protected Dialog mDialog;
    TextView info_a_sell;
    TextView info_a_buy_cost;
    TextView info_b_buy;
    TextView info_b_sell_earn;
    TextView rate_a_b;
    TextView a_b_overview;

    private void showDetail(CoinPair pairInfo) {
        if (mDialog == null) {
            View v = getLayoutInflater().inflate(R.layout.dialog_detail, null);
            info_a_sell = v.findViewById(R.id.info_a_sell);
            info_a_buy_cost = v.findViewById(R.id.info_a_buy_cost);
            info_b_buy = v.findViewById(R.id.info_b_buy);
            info_b_sell_earn = v.findViewById(R.id.info_b_sell_earn);
            rate_a_b = v.findViewById(R.id.rate_a_b);
            a_b_overview = v.findViewById(R.id.a_b_overview);
            mDialog = new AlertDialog
                    .Builder(getActivity()).setView(v)
                    .setNegativeButton(R.string.cancel, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            mDialog.dismiss();
                        }
                    })
                    .create();
        }
        mDialog.show();


        double total_amount_a = 0;
        double gap_amount_a = 0; // 深度缺口
        double total_pay_a = 0;
        Depth depthA = pairInfo.getDepthA();
        List<DepthOrder> asks = depthA.getSells();

        StringBuffer sb_A = new StringBuffer();
        int lb = Math.min(asks.size(), Config.SIZE_DEPTH);

        for (int i = 0; i < lb; i++) {
            double price = asks.get(i).getPrice();
            double amount = asks.get(i).getAmount();
            boolean flag = false;

            gap_amount_a = pairInfo.getCoin1().getAmount() - total_amount_a; // 至上一前深度，要满足买入数量还需要的缺口
            total_amount_a = total_amount_a + amount; // 至当前深度，可买入的数量
            if (gap_amount_a <= 0) { //  至上一前深度，已经满足

            } else if (total_amount_a >= pairInfo.getCoin1().getAmount()) { // 至当前深度，已经满足需要买入的数量
                total_pay_a = total_pay_a + gap_amount_a * price;
                flag = true;
            } else {
                total_pay_a = total_pay_a + amount * price;
            }


            if (i > 0) {
                sb_A.append("\n");
            }
            sb_A.append(df.format(price)).append("  ").append(amount).append(flag ? "***" : "");
        }
        double avage_a = total_pay_a / pairInfo.getCoin1().getAmount();


        info_a_sell.setText(sb_A.toString());
        info_a_buy_cost.setText(String.format(getString(R.string.cost_average), df.format(avage_a)));


        double total_amount = 0;
        double gap_amount = 0; // 深度缺口
        double total_earn_b = 0;

        Depth depth = pairInfo.getDepthB();
        List<DepthOrder> bids = depth.getBuys();

        StringBuffer sb = new StringBuffer();
        int ls = Math.min(bids.size(), Config.SIZE_DEPTH);
        for (int i = 0; i < ls; i++) {
            double price = bids.get(i).getPrice();
            double amount = bids.get(i).getAmount();
            boolean flag = false;

            gap_amount = pairInfo.getCoin2().getAmount() - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
            total_amount = total_amount + amount; // 至当前深度，可买入的数量
            if (gap_amount <= 0) { //  至上一前深度，已经满足

            } else if (total_amount >= pairInfo.getCoin2().getAmount()) { // 至当前深度，已经满足需要买入的数量
                total_earn_b = total_earn_b + gap_amount * price;
                flag = true;
            } else {
                total_earn_b = total_earn_b + amount * price;
            }


            if (i > 0) {
                sb.append("\n");
            }
            sb.append(df.format(price)).append("  ").append(amount).append(flag ? "**" : "");
        }
        double avage_b = total_earn_b / pairInfo.getCoin2().getAmount();


        info_b_buy.setText(sb.toString());
        info_b_sell_earn.setText(String.format(getString(R.string.earn_average), df.format(avage_b)));


        double net = total_earn_b - total_pay_a - 0.0025 * (total_earn_b + total_pay_a);
        String netrate = df_net.format(net / total_pay_a);

        String overview = String.format("买入A花费:%f,卖出B得到%f,扣除佣金,获利：%S,比率：%S", total_pay_a,
                total_earn_b, df.format(net) + pairInfo.getAnchorCoin().getAlias(), netrate);
        a_b_overview.setText(overview);


    }

    public interface OnListInteractionListener {
        void onListFragmentInteraction(CoinPair item);
    }
}
