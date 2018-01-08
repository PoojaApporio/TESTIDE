package com.ide.customer;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import butterknife.Bind;
import butterknife.ButterKnife;

/**
 * Created by Lenovo on 10/12/2017.
 */

public class PhotoUploadAdapter extends BaseAdapter {
    private Context context;
    private ArrayList<String> arrayList;
    private LayoutInflater inflater;

    public PhotoUploadAdapter(Context context, ArrayList<String> arrayList){
        this.context = context;
        this.arrayList = arrayList;
    }

    @Override
    public int getCount() {
        return arrayList.size();
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    static class MyHolder {
        @Bind(R.id.tv_car2)
        TextView tv_car2;

        public MyHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }

    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        MyHolder myHolder;
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(R.layout.itemlayoutforcarmodels, parent, false);
            myHolder = new MyHolder(convertView);
            convertView.setTag(myHolder);
        } else {
            myHolder = (MyHolder) convertView.getTag();
        }

        myHolder.tv_car2.setText(arrayList.get(i));
        return convertView;
    }
}
