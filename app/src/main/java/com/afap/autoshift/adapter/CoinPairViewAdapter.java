package com.afap.autoshift.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;

import com.afap.autoshift.ListActivity.OnListInteractionListener;
import com.afap.autoshift.R;
import com.afap.autoshift.model.CoinPair;
import com.afap.autoshift.model.PairInfo;

import java.text.DecimalFormat;
import java.util.List;

public class CoinPairViewAdapter extends RecyclerView.Adapter<CoinPairViewAdapter.ViewHolder> {

    private Context mContext;
    private final List<CoinPair> mValues;
    private final OnListInteractionListener mListener;

    private DecimalFormat df_rate = new DecimalFormat("0.00000000");
    private DecimalFormat df = new DecimalFormat("0.00000");
    private DecimalFormat df_net = new DecimalFormat("0");
    private DecimalFormat df_netrate = new DecimalFormat("#.##%");

    public CoinPairViewAdapter(List<CoinPair> items, OnListInteractionListener listener, Context context) {
        mValues = items;
        mListener = listener;
        mContext = context;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_shift_simple, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        final CoinPair pairInfo = mValues.get(position);
        holder.pairInfo = pairInfo;

        Drawable drawable_a = mContext.getResources().getDrawable(pairInfo.getCoin1().getResId());
        drawable_a.setBounds(0, 0, 96, 96);
        holder.et_amount_a.setCompoundDrawables(drawable_a, null, null, null);
        holder.et_amount_a.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                double a = Double.parseDouble(s.toString());
                holder.pairInfo.getCoin1().setAmount(a);
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        Drawable drawable_b = mContext.getResources().getDrawable(pairInfo.getCoin2().getResId());
        drawable_b.setBounds(0, 0, 96, 96);
        holder.et_amount_b.setCompoundDrawables(drawable_b, null, null, null);


        if (pairInfo.getDepthB() != null) {
            holder.et_amount_a.setText(df.format(pairInfo.getCoin1().getAmount()));
            holder.et_amount_b.setText(df.format(pairInfo.getCoin2().getAmount()));
            holder.rate_a_b.setText("rate:" + df_rate.format(pairInfo.getShiftInfo().getRate()) + ",max:" + pairInfo.getShiftInfo().getMaxLimit());
            holder.info_a_buy_cost.setText("AVG:" + df.format(pairInfo.getAvage_a())
                    + "\nCOST:" + df.format(pairInfo.getAvage_a() * pairInfo.getCoin1().getAmount()));
            holder.info_b_sell_earn.setText("AVG:" + df.format(pairInfo.getAvage_b())
                    + "\nEARN:" + df.format(pairInfo.getAvage_b() * pairInfo.getCoin2().getAmount()));


            if (pairInfo.getCoin1().getAmount() > pairInfo.getShiftInfo().getMaxLimit()  ) {
                holder.tv_net.setBackgroundResource(R.drawable.bg_label_red);
                holder.tv_net.setText("----");
            } else if (pairInfo.getNetProfit() > 0) {
                holder.tv_net.setBackgroundResource(R.drawable.bg_label_green);
                holder.tv_net.setText("+" + df_netrate.format(pairInfo.getNetProfitRate()) + "\n" + df_net.format(pairInfo.getNetProfit()));

            } else {
                holder.tv_net.setBackgroundResource(R.drawable.bg_label_red);
                holder.tv_net.setText(df_netrate.format(pairInfo.getNetProfitRate()) + "\n" + df_net.format(pairInfo.getNetProfit()));
            }
        }

        holder.mView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (null != mListener) {
                   // mListener.onListFragmentInteraction(pairInfo);
                }
            }
        });
    }

    @Override
    public int getItemCount() {
        return mValues.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        private CoinPair pairInfo;
        private final View mView;
        private final EditText et_amount_a;
        private final EditText et_amount_b;
        private final TextView info_a_buy_cost;
        private final TextView info_b_sell_earn;
        private final TextView rate_a_b;
        private final TextView tv_net;


        private ViewHolder(View view) {
            super(view);
            mView = view;
            et_amount_a = (EditText) view.findViewById(R.id.amount_a);
            et_amount_b = (EditText) view.findViewById(R.id.amount_b);
            info_a_buy_cost = (TextView) view.findViewById(R.id.info_a_buy_cost);
            info_b_sell_earn = (TextView) view.findViewById(R.id.info_b_sell_earn);
            rate_a_b = (TextView) view.findViewById(R.id.rate_a_b);
            tv_net = (TextView) view.findViewById(R.id.tv_net);

        }
    }
}