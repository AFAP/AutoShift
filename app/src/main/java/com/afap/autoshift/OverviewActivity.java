package com.afap.autoshift;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afap.autoshift.model.Coin;
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
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class OverviewActivity extends AppCompatActivity {


    private final static int LIMIT_SIZE = 10; // 买卖各询问10个单子
    private DecimalFormat df_rate = new DecimalFormat("0.00000000");
    private DecimalFormat df = new DecimalFormat("0.00000");
    private DecimalFormat df_net = new DecimalFormat("#.##%");


    private LinearLayout ll_all;


    private List<PairInfo> pairs;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_overview);

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
        pairs = new ArrayList<>();
        pairs.add(new PairInfo("SC", R.mipmap.coin_sc, "sccny", 120000, "ETH", R.mipmap.coin_eth, "ethcny", 0, "SC_ETH"));
        pairs.add(new PairInfo("ETH", R.mipmap.coin_eth, "ethcny", 3.0, "SC", R.mipmap.coin_sc, "sccny", 0, "ETH_SC"));
        pairs.add(new PairInfo("SC", R.mipmap.coin_sc, "sccny", 120000, "ZEC", R.mipmap.coin_zec, "zeccny", 0, "SC_ZEC"));
        pairs.add(new PairInfo("ZEC", R.mipmap.coin_zec, "zeccny", 3.0, "SC", R.mipmap.coin_sc, "sccny", 0, "ZEC_SC"));
        pairs.add(new PairInfo("ZEC", R.mipmap.coin_zec, "zeccny", 3, "ETH", R.mipmap.coin_eth, "ethcny", 0, "ZEC_ETH"));
        pairs.add(new PairInfo("ETH", R.mipmap.coin_eth, "ethcny", 3.0, "ZEC", R.mipmap.coin_zec, "zeccny", 0, "ETH_ZEC"));


    }

    private void initView() {
        ll_all = (LinearLayout) findViewById(R.id.ll_all);

        for (int i = 0; i < pairs.size(); i++) {
            PairInfo pair = pairs.get(i);

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


            et_amount_a.setText(String.valueOf(pair.getAmount_a()));
            et_amount_b.setText(String.valueOf(pair.getAmount_b()));

            text_a.setText(pair.getName_a());
            Drawable drawable_a = getResources().getDrawable(pair.getIcon_a());
            drawable_a.setBounds(0, 0, 96, 96);
            text_a.setCompoundDrawables(drawable_a, null, null, null);

            text_b.setText(pair.getName_b());
            Drawable drawable_b = getResources().getDrawable(pair.getIcon_b());
            drawable_b.setBounds(0, 0, 96, 96);
            text_b.setCompoundDrawables(drawable_b, null, null, null);

            ll_all.addView(v);
        }
    }


    private void loadAll() {
        for (int i = 0; i < pairs.size(); i++) {
            getMarketInfo_A_B(i);
        }

    }

    private void getMarketInfo_A_B(final int position) {
        final PairInfo pair = pairs.get(position);
        final View v = ll_all.getChildAt(position);

        final double amount_a = Double.parseDouble(((EditText) v.findViewById(R.id.amount_a)).getText().toString());

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
                        if (shiftInfo.getLimit() < amount_a) {
                            ToastUtil.showShort("待转换数量超出限制");
                        }

                        double amount_b = shiftInfo.getRate() * amount_a - shiftInfo.getMinerFee();
                        ((EditText) v.findViewById(R.id.amount_b)).setText(df.format(amount_b));
                        ((TextView) v.findViewById(R.id.rate_a_b)).setText("当前比率:" + df_rate.format(shiftInfo.getRate()) + ",最大接收数量:" + shiftInfo.getMaxLimit());
                        getADepth_A(pair, v, amount_a, amount_b);
                    }
                });
    }

    private void getADepth_A(final PairInfo pair, final View v, final double amount_a, final double amount_b) {
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
                        List<DepthOrder> asks = depth.getAsks();

                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < asks.size(); i++) {
                            double price = asks.get(i).getPrice();
                            double amount = asks.get(i).getAmount();
                            boolean flag = false;

                            gap_amount = amount_a - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
                            total_amount = total_amount + amount; // 至当前深度，可买入的数量
                            if (gap_amount <= 0) { //  至上一前深度，已经满足

                            } else if (total_amount >= amount_a) { // 至当前深度，已经满足需要买入的数量
                                total_pay_a = total_pay_a + gap_amount * price;
                                flag = true;
                            } else {
                                total_pay_a = total_pay_a + amount * price;
                            }


                            if (i > 0) {
                                sb.append("\n");
                            }
                            sb.append(df.format(price)).append("  ").append(amount).append(flag ? "***" : "");
                        }
                        double avage_a = total_pay_a / amount_a;


                        TextView info_a_sell = (TextView) v.findViewById(R.id.info_a_sell);
                        TextView info_a_buy_cost = (TextView) v.findViewById(R.id.info_a_buy_cost);

                        info_a_sell.setText(sb.toString());
                        info_a_buy_cost.setText(String.format(getString(R.string.cost_average), df.format(avage_a)));
                        getBDepth_B(pair, v, amount_a, amount_b, total_pay_a);
                    }
                });
    }

    private void getBDepth_B(final PairInfo pair, final View v, final double amount_a, final double amount_b, final double total_pay_a) {
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
                        List<DepthOrder> bids = depth.getBids();

                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < bids.size(); i++) {
                            double price = bids.get(i).getPrice();
                            double amount = bids.get(i).getAmount();
                            boolean flag = false;

                            gap_amount = amount_b - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
                            total_amount = total_amount + amount; // 至当前深度，可买入的数量
                            if (gap_amount <= 0) { //  至上一前深度，已经满足

                            } else if (total_amount >= amount_b) { // 至当前深度，已经满足需要买入的数量
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
                        double avage_b = total_earn_b / amount_b;


                        TextView info_b_buy = (TextView) v.findViewById(R.id.info_b_buy);
                        TextView info_b_sell_earn = (TextView) v.findViewById(R.id.info_b_sell_earn);
                        TextView a_b_overview = (TextView) v.findViewById(R.id.a_b_overview);

                        info_b_buy.setText(sb.toString());
                        info_b_sell_earn.setText(String.format(getString(R.string.earn_average), df.format(avage_b)));


                        double net = total_earn_b - total_pay_a - 0.001 * (total_earn_b + total_pay_a);
                        String netrate = df_net.format(net / total_pay_a);
                        if (net > 0) {
                            a_b_overview.setTextColor(ContextCompat.getColor(OverviewActivity.this, R.color.colorPrimaryDark));
                        } else {
                            a_b_overview.setTextColor(ContextCompat.getColor(OverviewActivity.this, R.color.colorAccent));
                        }

                        String overview = String.format("买入A花费%f,卖出B得到%f,扣除佣金,获利：%f,比率：%S", total_pay_a, total_earn_b, net, netrate);
                        a_b_overview.setText(overview);

                    }
                });
    }


}
