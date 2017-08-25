package com.afap.autoshift;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.afap.autoshift.adapter.SimpleShiftViewAdapter;
import com.afap.autoshift.model.Depth;
import com.afap.autoshift.model.DepthOrder;
import com.afap.autoshift.model.PairInfo;
import com.afap.autoshift.model.ShiftInfo;
import com.afap.autoshift.net.BaseSubscriber;
import com.afap.autoshift.net.Network;
import com.afap.autoshift.utils.LogUtil;
import com.afap.utils.ToastUtil;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class ListActivity extends AppCompatActivity {


    private final static int LIMIT_SIZE = 10; // 买卖各询问10个单子
    private DecimalFormat df_rate = new DecimalFormat("0.00000000");
    private DecimalFormat df = new DecimalFormat("0.00000");
    private DecimalFormat df_net = new DecimalFormat("#.##%");

    private OnListInteractionListener mListener;
    private RecyclerView mRecyclerView;
    private SimpleShiftViewAdapter mAdapter;

    private List<PairInfo> mValues;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list);

        initData();
        initView();


        loadAll();


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_overview, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        } else if (item.getItemId() == R.id.action_refresh) {
            loadAll();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void initData() {

        mValues = new ArrayList<>();
        mValues.add(new PairInfo("SC", R.mipmap.coin_sc, "sccny", 120000, "ETH", R.mipmap.coin_eth, "ethcny", 0, "SC_ETH"));
        mValues.add(new PairInfo("ETH", R.mipmap.coin_eth, "ethcny", 2.5, "SC", R.mipmap.coin_sc, "sccny", 0, "ETH_SC"));
        mValues.add(new PairInfo("SC", R.mipmap.coin_sc, "sccny", 120000, "ZEC", R.mipmap.coin_zec, "zeccny", 0, "SC_ZEC"));
        mValues.add(new PairInfo("ZEC", R.mipmap.coin_zec, "zeccny", 3.0, "SC", R.mipmap.coin_sc, "sccny", 0, "ZEC_SC"));
        mValues.add(new PairInfo("ZEC", R.mipmap.coin_zec, "zeccny", 3.0, "ETH", R.mipmap.coin_eth, "ethcny", 0, "ZEC_ETH"));
        mValues.add(new PairInfo("ETH", R.mipmap.coin_eth, "ethcny", 2.5, "ZEC", R.mipmap.coin_zec, "zeccny", 0, "ETH_ZEC"));

        mValues.add(new PairInfo("ETH", R.mipmap.coin_eth, "ethcny", 2.5, "DGD", R.mipmap.coin_dgd, "dgdcny", 0, "ETH_DGD"));
        mValues.add(new PairInfo("DGD", R.mipmap.coin_dgd, "dgdcny", 6.0, "ZEC", R.mipmap.coin_eth, "ethcny", 0, "DGD_ETH"));
        mValues.add(new PairInfo("SC", R.mipmap.coin_sc, "sccny", 100000, "DGD", R.mipmap.coin_dgd, "dgdcny", 0, "SC_DGD"));
        mValues.add(new PairInfo("DGD", R.mipmap.coin_dgd, "dgdcny", 6.0, "SC", R.mipmap.coin_sc, "sccny", 0, "DGD_SC"));
        mValues.add(new PairInfo("DGD", R.mipmap.coin_dgd, "dgdcny", 6.0, "ZEC", R.mipmap.coin_zec, "zeccny", 0, "DGD_ZEC"));
        mValues.add(new PairInfo("ZEC", R.mipmap.coin_zec, "zeccny", 2.5, "DGD", R.mipmap.coin_dgd, "dgdcny", 0, "ZEC_DGD"));


    }

    private void initView() {
        mListener = new OnListInteractionListener() {
            @Override
            public void onListFragmentInteraction(PairInfo pairInfo) {
                if (pairInfo.getDepthB() != null) {
                    showDetail(pairInfo);
                }
            }
        };

        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mAdapter = new SimpleShiftViewAdapter(mValues, mListener, this);
        mRecyclerView.setAdapter(mAdapter);

        for (int i = 0; i < mValues.size(); i++) {
            PairInfo pair = mValues.get(i);

            View v = View.inflate(this, R.layout.atom_shift, null);

            EditText et_amount_a = (EditText) v.findViewById(R.id.amount_a);
            EditText et_amount_b = (EditText) v.findViewById(R.id.amount_b);

            TextView text_a = (TextView) v.findViewById(R.id.text_a);
            TextView text_b = (TextView) v.findViewById(R.id.text_b);

//            TextView info_a_sell = (TextView) v.findViewById(R.id.info_a_sell);
//            TextView info_a_buy_cost = (TextView) v.findViewById(R.id.info_a_buy_cost);
//            TextView info_b_buy = (TextView) v.findViewById(R.id.info_b_buy);
//            TextView info_b_sell_earn = (TextView) v.findViewById(R.id.info_b_sell_earn);
//            TextView rate_a_b = (TextView) v.findViewById(R.id.rate_a_b);
//            TextView a_b_overview = (TextView) v.findViewById(R.id.a_b_overview);


        }
    }


    // 文件传输相关Dialog
    protected Dialog mDialog;
    TextView info_a_sell;
    TextView info_a_buy_cost;
    TextView info_b_buy;
    TextView info_b_sell_earn;
    TextView rate_a_b;
    TextView a_b_overview;

    private void showDetail(PairInfo pairInfo) {
        if (mDialog == null) {
            View v = getLayoutInflater().inflate(R.layout.dialog_detail, null);
            info_a_sell = (TextView) v.findViewById(R.id.info_a_sell);
            info_a_buy_cost = (TextView) v.findViewById(R.id.info_a_buy_cost);
            info_b_buy = (TextView) v.findViewById(R.id.info_b_buy);
            info_b_sell_earn = (TextView) v.findViewById(R.id.info_b_sell_earn);
            rate_a_b = (TextView) v.findViewById(R.id.rate_a_b);
            a_b_overview = (TextView) v.findViewById(R.id.a_b_overview);
            mDialog = new AlertDialog
                    .Builder(this).setView(v)
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
        List<DepthOrder> asks = depthA.getAsks();

        StringBuffer sb_A = new StringBuffer();
        for (int i = 0; i < asks.size(); i++) {
            double price = asks.get(i).getPrice();
            double amount = asks.get(i).getAmount();
            boolean flag = false;

            gap_amount_a = pairInfo.getAmount_a() - total_amount_a; // 至上一前深度，要满足买入数量还需要的缺口
            total_amount_a = total_amount_a + amount; // 至当前深度，可买入的数量
            if (gap_amount_a <= 0) { //  至上一前深度，已经满足

            } else if (total_amount_a >= pairInfo.getAmount_a()) { // 至当前深度，已经满足需要买入的数量
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
        double avage_a = total_pay_a / pairInfo.getAmount_a();



        info_a_sell.setText(sb_A.toString());
        info_a_buy_cost.setText(String.format(getString(R.string.cost_average), df.format(avage_a)));


        double total_amount = 0;
        double gap_amount = 0; // 深度缺口
        double total_earn_b = 0;

        Depth depth = pairInfo.getDepthB();
        List<DepthOrder> bids = depth.getBids();

        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < bids.size(); i++) {
            double price = bids.get(i).getPrice();
            double amount = bids.get(i).getAmount();
            boolean flag = false;

            gap_amount = pairInfo.getAmount_b() - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
            total_amount = total_amount + amount; // 至当前深度，可买入的数量
            if (gap_amount <= 0) { //  至上一前深度，已经满足

            } else if (total_amount >= pairInfo.getAmount_b()) { // 至当前深度，已经满足需要买入的数量
                total_earn_b = total_earn_b + gap_amount * price;
                flag = true;
            } else {
                total_earn_b = total_earn_b + amount * price;
            }


            if (i > 0) {
                sb.append("\n");
            }
            sb.append(df.format(price)).append("  ").append(amount).append(flag ? "***" : "");
        }
        double avage_b = total_earn_b / pairInfo.getAmount_b();



        info_b_buy.setText(sb.toString());
        info_b_sell_earn.setText(String.format(getString(R.string.earn_average), df.format(avage_b)));


        double net = total_earn_b - total_pay_a - 0.001 * (total_earn_b + total_pay_a);
        String netrate = df_net.format(net / total_pay_a);

        String overview = String.format("买入A花费%f,卖出B得到%f,扣除佣金,获利：%f,比率：%S", total_pay_a, total_earn_b, net, netrate);
        a_b_overview.setText(overview);


    }


    private void loadAll() {
        for (int i = 0; i < mValues.size(); i++) {
            getMarketInfo_A_B(i);
        }

    }

    private void getMarketInfo_A_B(final int position) {
        final PairInfo pair = mValues.get(position);

        // TODO xxxx
        final double amount_a = pair.getAmount_a();

        Network
                .getShapeShiftAPIService()
                .getMarketInfo(pair.getShift_cat())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtil.i(pair.getShift_cat(), jsonObject.toString());

                        ShiftInfo shiftInfo = ShiftInfo.parseFromJson(jsonObject);
                        pair.setShiftInfo(shiftInfo);


                        if (shiftInfo.getLimit() < amount_a) {
                            ToastUtil.showShort("待转换数量超出限制");
                        }

                        double amount_b = shiftInfo.getRate() * amount_a - shiftInfo.getMinerFee();
                        pair.setAmount_b(amount_b);

                        getADepth_A(pair, amount_a, amount_b);
                    }
                });
    }

    private void getADepth_A(final PairInfo pair, final double amount_a, final double amount_b) {
        Network
                .getYunbiAPIService()
                .getDepth(pair.getYunbi_a(), LIMIT_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtil.i(pair.getYunbi_a(), jsonObject.toString());

                        double total_amount = 0;
                        double gap_amount = 0; // 深度缺口
                        double total_pay_a = 0;

                        Depth depth = Depth.parseFromJson(jsonObject);
                        pair.setDepthA(depth);

                        List<DepthOrder> asks = depth.getAsks();
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

                        getBDepth_B(pair, amount_a, amount_b, total_pay_a);
                    }
                });
    }

    private void getBDepth_B(final PairInfo pair, final double amount_a, final double amount_b, final double total_pay_a) {
        Network
                .getYunbiAPIService()
                .getDepth(pair.getYunbi_b(), LIMIT_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtil.i(pair.getYunbi_b(), jsonObject.toString());

                        double total_amount = 0;
                        double gap_amount = 0; // 深度缺口
                        double total_earn_b = 0;

                        Depth depth = Depth.parseFromJson(jsonObject);
                        pair.setDepthB(depth);


                        List<DepthOrder> bids = depth.getBids();

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


                        double net = total_earn_b - total_pay_a - 0.001 * (total_earn_b + total_pay_a);
                        double netrate = net / total_pay_a;

                        pair.setNetProfit(net);
                        pair.setNetProfitRate(netrate);
                        Collections.sort(mValues, new Comparator<PairInfo>() {

                            @Override
                            public int compare(PairInfo o1, PairInfo o2) {
                                return o1.getNetProfitRate() > o2.getNetProfitRate() ? -1 : 1;
                            }
                        });

                        mAdapter.notifyDataSetChanged();
                    }
                });
    }

    public interface OnListInteractionListener {
        void onListFragmentInteraction(PairInfo item);
    }
}
