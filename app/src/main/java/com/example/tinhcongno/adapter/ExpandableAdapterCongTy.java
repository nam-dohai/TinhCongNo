package com.example.tinhcongno.adapter;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.BaseExpandableListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.tinhcongno.R;

import org.apache.poi.ss.formula.functions.T;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

public class ExpandableAdapterCongTy extends BaseExpandableListAdapter {
    private ArrayList<String> arrayCongTy;
    private HashMap<String, ArrayList<String>> arrayNgayThang;

    public ExpandableAdapterCongTy(ArrayList<String> arrayCongTy, HashMap<String, ArrayList<String>> arrayNgayThang){
        this.arrayCongTy = arrayCongTy;
        this.arrayNgayThang = arrayNgayThang;
    }
    @Override
    public int getGroupCount() {
        if (arrayCongTy != null){
            return arrayCongTy.size();
        }
        return 0;
    }

    @Override
    public int getChildrenCount(int groupPosition) {
        if (arrayNgayThang != null && arrayNgayThang != null){
            return arrayNgayThang.get(arrayCongTy.get(groupPosition)).size();
        }
        return 0;
    }

    @Override
    public Object getGroup(int groupPosition) {
        return arrayCongTy.get(groupPosition);
    }

    @Override
    public Object getChild(int groupPosition, int childPosition) {
        return arrayNgayThang.get(arrayCongTy.get(groupPosition)).get(childPosition);
    }

    @Override
    public long getGroupId(int groupPosition) {
        return groupPosition;
    }

    @Override
    public long getChildId(int groupPosition, int childPosition) {
        return childPosition;
    }

    @Override
    public boolean hasStableIds() {
        return false;
    }

    @Override
    public View getGroupView(int groupPosition, boolean isExpanded, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_group_congty,parent,false);
        }
        TextView tvCongTy = (TextView)convertView.findViewById(R.id.tvCongTy);
        tvCongTy.setText(arrayCongTy.get(groupPosition));
        return convertView;
    }

    @Override
    public View getChildView(int groupPosition, int childPosition, boolean isLastChild, View convertView, ViewGroup parent) {
        if (convertView == null){
            convertView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_item_group_ngaythang,parent,false);
        }
        TextView tvNgayThang = (TextView)convertView.findViewById(R.id.tvNgayThang);
        tvNgayThang.setText(arrayNgayThang.get(arrayCongTy.get(groupPosition)).get(childPosition));
        return convertView;
    }

    @Override
    public boolean isChildSelectable(int groupPosition, int childPosition) {
        return true;
    }
}
