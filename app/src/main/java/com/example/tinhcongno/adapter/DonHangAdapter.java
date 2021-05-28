package com.example.tinhcongno.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.TextView;

import com.example.tinhcongno.ListenerButtonChangeClick;
import com.example.tinhcongno.ListenerDataChange;
import com.example.tinhcongno.R;
import com.example.tinhcongno.database.DataSource;
import com.example.tinhcongno.database.SQLiteHelper;
import com.example.tinhcongno.model.DonHang;

import java.util.ArrayList;

public class DonHangAdapter extends BaseAdapter {

    private Activity activity;
    private ArrayList<DonHang> arrayDonHang;
    private ListenerDataChange listener;
    private DataSource dataSource;

    ArrayList<ListenerButtonChangeClick> arrayListener = new ArrayList<>();
    private ListenerButtonChangeClick listenerButtonChangeClick;

    public DonHangAdapter(Activity activity, ArrayList<DonHang> arrayDonHang, ListenerDataChange listener,DataSource dataSource){
        this.activity = activity;
        this.arrayDonHang = arrayDonHang;
        this.listener = listener;
        this.dataSource = dataSource;
        dataSource.open();

    }
    @Override
    public int getCount() {
        if (arrayDonHang != null)
            return arrayDonHang.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return arrayDonHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.items_don_hang,parent,false);

        EditText edittextMatHang = (EditText)view.findViewById(R.id.edittextMatHang);
        EditText edittextSoLuong = (EditText)view.findViewById(R.id.edittextSoLuong);
        EditText edittextMenhGia = (EditText)view.findViewById(R.id.edittextMenhGia);

        DonHang donHang = arrayDonHang.get(position);
        edittextMatHang.setText(donHang.getMatHang().getName());
        edittextSoLuong.setText(donHang.getSoLuong() + "");
        edittextMenhGia.setText(donHang.getMatHang().getMenhGia()+"");

        edittextMatHang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.listener();
                listenerButtonChangeClick = new ListenerButtonChangeClick() {
                    @Override
                    public void listener() {
                        dataSource.updateData(s.toString(),donHang, SQLiteHelper.COLUMN_MAT_HANG);
                        donHang.getMatHang().setName(s.toString());
                        notifyDataSetChanged();
                    }
                };
                arrayListener.add(listenerButtonChangeClick);
            }
        });

        edittextSoLuong.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.listener();
                listenerButtonChangeClick = new ListenerButtonChangeClick() {
                    @Override
                    public void listener() {
                        dataSource.updateData(s.toString(),donHang, SQLiteHelper.COLUMN_SO_LUONG);
                        donHang.setSoLuong(Float.parseFloat(s.toString()));
                        notifyDataSetChanged();
                    }
                };
                arrayListener.add(listenerButtonChangeClick);
            }
        });

        edittextMenhGia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.listener();
                listenerButtonChangeClick = new ListenerButtonChangeClick() {
                    @Override
                    public void listener() {
                        dataSource.updateData(s.toString(),donHang, SQLiteHelper.COLUMN_MENH_GIA);
                        donHang.getMatHang().setMenhGia(Float.parseFloat(s.toString()));
                        notifyDataSetChanged();
                    }
                };
                arrayListener.add(listenerButtonChangeClick);
            }
        });
        return view;
    }
    public ArrayList<ListenerButtonChangeClick> getArrayListener(){
        return arrayListener;
    }
    public void deleteArrayListener(){
        this.arrayListener = null;
        this.arrayListener = new ArrayList<>();
    }
}
