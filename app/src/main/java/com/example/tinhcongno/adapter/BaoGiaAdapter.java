package com.example.tinhcongno.adapter;

import android.app.Activity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;

import com.example.tinhcongno.ListenerButtonChangeClick;
import com.example.tinhcongno.ListenerDataChange;
import com.example.tinhcongno.R;
import com.example.tinhcongno.database.DataSource;
import com.example.tinhcongno.database.SQLiteHelper;
import com.example.tinhcongno.model.MatHang;

import java.util.ArrayList;

public class BaoGiaAdapter extends BaseAdapter {
    private ArrayList<MatHang> arrayMatHang;
    private Activity activity;
    private ListenerDataChange listener;
    private DataSource dataSource;
    private ArrayList<ListenerButtonChangeClick> arrayListener = new ArrayList<>();
    private ListenerButtonChangeClick listenerDataChange;

    public BaoGiaAdapter(Activity activity, ArrayList<MatHang> arrayMatHang, ListenerDataChange listener, DataSource dataSource) {
        this.arrayMatHang = arrayMatHang;
        this.activity = activity;
        this.listener = listener;
        this.dataSource = dataSource;
    }

    @Override
    public int getCount() {
        if (arrayMatHang != null)
            return arrayMatHang.size();
        return 0;
    }

    @Override
    public Object getItem(int position) {
        return arrayMatHang.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater layoutInflater = activity.getLayoutInflater();
        View view = layoutInflater.inflate(R.layout.items_bao_gia,parent,false);

        EditText edtBaoGiaMatHang = (EditText)view.findViewById(R.id.edtBaoGiaMatHang);
        EditText edtBaoGiaMenhGia = (EditText)view.findViewById(R.id.edtBaoGiaMenhGia);

        MatHang matHang = arrayMatHang.get(position);
        edtBaoGiaMatHang.setText(matHang.getName());
        edtBaoGiaMenhGia.setText(matHang.getMenhGia()+"");

        edtBaoGiaMatHang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.listener();
                listenerDataChange = new ListenerButtonChangeClick() {
                    @Override
                    public void listener() {
                        dataSource.updateDataToBaoGia(s.toString(),matHang, SQLiteHelper.COLUMN_MAT_HANG);
                        matHang.setName(s.toString());
                        notifyDataSetChanged();
                    }
                };
                arrayListener.add(listenerDataChange); //Lưu các thay đổi vào vào array chờ xử lí
            }
        });

        edtBaoGiaMenhGia.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                listener.listener();
                listenerDataChange = new ListenerButtonChangeClick() {
                    @Override
                    public void listener() {
                        dataSource.updateDataToBaoGia(s.toString(),matHang, SQLiteHelper.COLUMN_MENH_GIA);
                        matHang.setMenhGia(Float.parseFloat(s.toString()));
                        notifyDataSetChanged();
                    }
                };
                arrayListener.add(listenerDataChange); //Lưu các thay đổi vào vào array chờ xử lí
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
    public void searchList(ArrayList<MatHang> array){
        arrayMatHang = array;
        notifyDataSetChanged();
    }
}
