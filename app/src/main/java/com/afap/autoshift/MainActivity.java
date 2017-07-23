package com.afap.autoshift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afap.autoshift.model.Ask;
import com.afap.autoshift.model.Coin;
import com.afap.autoshift.model.Depth;
import com.afap.autoshift.model.DepthOrder;
import com.afap.autoshift.model.ShiftInfo;
import com.afap.autoshift.net.BaseSubscriber;
import com.afap.autoshift.net.Network;
import com.afap.autoshift.utils.LogUtil;
import com.afap.utils.ToastUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private final static int LIMIT_SIZE = 10; // 买卖各询问10个单子
    private DecimalFormat df_rate = new DecimalFormat("0.0000000000");
    private DecimalFormat df = new DecimalFormat("0.00000");

    private List<Coin> coins;

    private Spinner spinner_coin1, spinner_coin2;

    private EditText et_amount_a, et_amount_b;
    private TextView info_sc_sell, info_sc_buy_cost;
    private TextView info_eth_buy, info_eth_sell_earn;
    private TextView rate_sc_eth, sc_eth_overview;

    private double amount_a, avage_a, total_pay = 0;
    private double amount_b, avage_b, total_earn = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();

        et_amount_a = (EditText) findViewById(R.id.amount_a);
        et_amount_b = (EditText) findViewById(R.id.amount_b);

        info_sc_sell = (TextView) findViewById(R.id.info_sc_sell);
        info_sc_buy_cost = (TextView) findViewById(R.id.info_sc_buy_cost);
        info_eth_buy = (TextView) findViewById(R.id.info_eth_buy);
        info_eth_sell_earn = (TextView) findViewById(R.id.info_eth_sell_earn);
        rate_sc_eth = (TextView) findViewById(R.id.rate_sc_eth);
        sc_eth_overview = (TextView) findViewById(R.id.sc_eth_overview);


        spinner_coin1 = (Spinner) findViewById(R.id.spinner_coin1);
        spinner_coin2 = (Spinner) findViewById(R.id.spinner_coin2);

        spinner_coin1.setAdapter(new MyAdapter());
        spinner_coin2.setAdapter(new MyAdapter());


        findViewById(R.id.btn_test).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                amount_a = Double.parseDouble(et_amount_a.getText().toString());
                if (amount_a > 0) {
                    total_pay = 0;
                    total_earn = 0;
                    getMarketInfo();
                }
            }
        });

    }

    private void initData() {
        coins = new ArrayList<>();
        coins.add(new Coin("sccny", "SC", R.mipmap.coin_sc));
        coins.add(new Coin("btccny", "BTC", R.mipmap.coin_btc));
        coins.add(new Coin("ethcny", "ETH", R.mipmap.coin_eth));
        coins.add(new Coin("zeccny", "ZEC", R.mipmap.coin_zec));

    }

    private void getMarketInfo() {
        Network
                .getShapeShiftAPIService()
                .getMarketInfo("SC_ETH")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtil.i("SC_ETH", jsonObject.toString());

                        ShiftInfo shiftInfo = ShiftInfo.parseFromJson(jsonObject);
                        if (shiftInfo.getLimit() < amount_a) {
                            ToastUtil.showShort("待转换数量超出限制");
                        } else {
                            amount_b = shiftInfo.getRate() * amount_a - shiftInfo.getMinerFee();
                            et_amount_b.setText(df.format(amount_b));
                            rate_sc_eth.setText("当前比率:" + df_rate.format( shiftInfo.getRate()));
                            getADepth("");
                        }
                    }
                });
    }

    private void getADepth(String type) {
        Network
                .getYunbiAPIService()
                .getDepth("sccny", LIMIT_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtil.i("getADepth", jsonObject.toString());

                        double total_amount = 0;
                        double gap_amount = 0; // 深度缺口

                        Depth depth = Depth.parseFromJson(jsonObject);
                        List<DepthOrder> asks = depth.getAsks();

                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < asks.size(); i++) {
                            double price = asks.get(i).getPrice();
                            double amount = asks.get(i).getAmount();
                            String flag = "";

                            gap_amount = amount_a - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
                            total_amount = total_amount + amount; // 至当前深度，可买入的数量
                            if (gap_amount <= 0) { //  至上一前深度，已经满足

                            } else if (total_amount >= amount_a) { // 至当前深度，已经满足需要买入的数量
                                total_pay = total_pay + gap_amount * price;
                                flag = "**";
                            } else {
                                total_pay = total_pay + amount * price;
                            }


                            if (i > 0) {
                                sb.append("\n");
                            }
                            sb.append(df.format(price)).append("  ").append(amount).append(flag);
                        }
                        avage_a = total_pay / amount_a;

                        info_sc_sell.setText(sb.toString());
                        info_sc_buy_cost.setText(String.format(getString(R.string.cost_average), df.format(avage_a)));
                        getBDepth("");
                    }
                });
    }

    private void getBDepth(String type) {
        Network
                .getYunbiAPIService()
                .getDepth("ethcny", LIMIT_SIZE)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtil.i("getBDepth", jsonObject.toString());

                        double total_amount = 0;
                        double gap_amount = 0; // 深度缺口

                        Depth depth = Depth.parseFromJson(jsonObject);
                        List<DepthOrder> bids = depth.getBids();

                        StringBuffer sb = new StringBuffer();
                        for (int i = 0; i < bids.size(); i++) {
                            double price = bids.get(i).getPrice();
                            double amount = bids.get(i).getAmount();
                            String flag = "";

                            gap_amount = amount_b - total_amount; // 至上一前深度，要满足买入数量还需要的缺口
                            total_amount = total_amount + amount; // 至当前深度，可买入的数量
                            if (gap_amount <= 0) { //  至上一前深度，已经满足

                            } else if (total_amount >= amount_b) { // 至当前深度，已经满足需要买入的数量
                                total_earn = total_earn + gap_amount * price;
                                flag = "**";
                            } else {
                                total_earn = total_earn + amount * price;
                            }


                            if (i > 0) {
                                sb.append("\n");
                            }
                            sb.append(df.format(price)).append("  ").append(amount).append(flag);
                        }
                        avage_b = total_earn / amount_b;

                        info_eth_buy.setText(sb.toString());
                        info_eth_sell_earn.setText(String.format(getString(R.string.earn_average), df.format(avage_b)));

                        String overview = String.format("买入A花费%f,卖出B得到%f,获利：%f", total_pay, total_earn, (total_earn -
                                total_pay));
                        sc_eth_overview.setText("++" + overview);

                    }
                });
    }


    class MyAdapter extends BaseAdapter {

        @Override
        public int getCount() {
            return coins.size();
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            Coin c = coins.get(position);

            View v = View.inflate(MainActivity.this, R.layout.atom_spinner_coin, null);
            ImageView img = (ImageView) v.findViewById(R.id.img_coin);
            TextView tv = (TextView) v.findViewById(R.id.name_coin);
            img.setImageResource(c.getResId());
            tv.setText(c.getName());

            return v;
        }
    }


}
