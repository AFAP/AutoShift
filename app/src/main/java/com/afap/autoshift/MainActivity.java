package com.afap.autoshift;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.afap.autoshift.model.Ask;
import com.afap.autoshift.model.Coin;
import com.afap.autoshift.net.BaseSubscriber;
import com.afap.autoshift.net.Network;
import com.afap.autoshift.utils.LogUtil;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import java.util.ArrayList;
import java.util.List;

import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class MainActivity extends AppCompatActivity {


    private List<Coin> coins;

    private Spinner spinner_coin1, spinner_coin2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initData();


        spinner_coin1 = (Spinner) findViewById(R.id.spinner_coin1);
        spinner_coin2 = (Spinner) findViewById(R.id.spinner_coin2);

        spinner_coin1.setAdapter(new MyAdapter());
        spinner_coin2.setAdapter(new MyAdapter());
        getMarketBook();
    }

    private void initData() {
        coins = new ArrayList<>();
        coins.add(new Coin("sccny", "SC", R.mipmap.coin_sc));
        coins.add(new Coin("btccny", "BTC", R.mipmap.coin_btc));
        coins.add(new Coin("ethcny", "ETH", R.mipmap.coin_eth));
        coins.add(new Coin("zeccny", "ZEC", R.mipmap.coin_zec));

    }

    void getMarketBook() {
        Network
                .getAPIService()
                .getOrderBook("sccny")
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new BaseSubscriber<JsonObject>() {
                    @Override
                    public void onNext(JsonObject jsonObject) {
                        LogUtil.i("SSS", jsonObject.toString());
                        JsonArray asks = jsonObject.getAsJsonArray("asks");
                        for (int i = 0; i < asks.size(); i++) {
                            Ask.parse
                        }



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
