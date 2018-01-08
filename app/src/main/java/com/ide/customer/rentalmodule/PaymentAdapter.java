package com.ide.customer.rentalmodule;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.ide.customer.R;
import com.ide.customer.models.ViewPaymentOption;

public class PaymentAdapter extends BaseAdapter {

    Context context;
    ViewPaymentOption viewPaymentOption;


    public PaymentAdapter(Context context, ViewPaymentOption viewPaymentOption) {
        this.context = context;
        this.viewPaymentOption = viewPaymentOption;
    }

    @Override
    public int getCount() {
        return viewPaymentOption.getMsg().size();
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

        convertView = LayoutInflater.from(context).inflate(R.layout.itemlayoutforpaymentoption, parent, false);
        TextView tv_payment = (TextView) convertView.findViewById(R.id.tv_payment);
        tv_payment.setText(viewPaymentOption.getMsg().get(position).getPayment_option_name());
        return convertView;
    }

}
