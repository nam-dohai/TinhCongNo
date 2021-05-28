package com.example.tinhcongno.database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.example.tinhcongno.model.DonHang;
import com.example.tinhcongno.model.MatHang;

import java.util.ArrayList;
import java.util.HashMap;

public class DataSource {
    private SQLiteDatabase database;
    private SQLiteHelper dbhelper;
    private String[] allColumnsBaoGia = {SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_MAT_HANG,SQLiteHelper.COLUMN_MENH_GIA};
    private String[] allCoulmnsCongNo = {SQLiteHelper.COLUMN_ID,SQLiteHelper.COLUMN_MAT_HANG,SQLiteHelper.COLUMN_MENH_GIA,SQLiteHelper.COLUMN_SO_LUONG,SQLiteHelper.COULUMN_CONG_TY,SQLiteHelper.COULUMN_NGAY_THANG};

    public DataSource(Context context){
        dbhelper = new SQLiteHelper(context);
    }

    public void open() throws SQLException{
        database = dbhelper.getWritableDatabase();
    }

    public void close(){
        dbhelper.close();
    }

    public ArrayList<MatHang> getAllMatHangFromBaoGia(){
        ArrayList<MatHang> arrayList = new ArrayList<>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_BAO_GIA,allColumnsBaoGia,null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            MatHang matHang = getMatHangFromBaoGia(cursor);
            arrayList.add(matHang);
            cursor.moveToNext();
        }
        return arrayList;
    }
    public void addMatHangToBaoGia( MatHang matHang){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_MAT_HANG,matHang.getName());
        values.put(SQLiteHelper.COLUMN_MENH_GIA,matHang.getMenhGia());
        database.insert(SQLiteHelper.TABLE_BAO_GIA,null,values);
    }

    public void deleteAll(){
        database.delete(SQLiteHelper.TABLE_BAO_GIA,null,null);
    }

    public MatHang getMatHangFromBaoGia(Cursor cursor){
        MatHang matHang = new MatHang();
        matHang.setId((int) cursor.getLong(0));
        matHang.setName(cursor.getString(1));
        matHang.setMenhGia(cursor.getFloat(2));
        return matHang;
    }

    public void addDonHangtoCongNo(DonHang donHang){
        ContentValues values = new ContentValues();
        values.put(SQLiteHelper.COLUMN_MAT_HANG,donHang.getMatHang().getName());
        values.put(SQLiteHelper.COLUMN_MENH_GIA,donHang.getMatHang().getMenhGia());
        values.put(SQLiteHelper.COLUMN_SO_LUONG,donHang.getSoLuong());
        values.put(SQLiteHelper.COULUMN_CONG_TY,donHang.getCongTy());
        values.put(SQLiteHelper.COULUMN_NGAY_THANG,donHang.getNgayThang());
        database.insert(SQLiteHelper.TABLE_CONG_NO,null,values);
    }
    public ArrayList<String> getAllCongTy(){
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor = database.query(SQLiteHelper.TABLE_CONG_NO,new String[]{SQLiteHelper.COULUMN_CONG_TY},null,null,null,null,null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String congTy = getCongTy(cursor);
            if (!arrayList.contains(congTy)){
                arrayList.add(congTy);
            }
            cursor.moveToNext();
        }
        return arrayList;
    }
    public String getCongTy(Cursor cursor){
        return cursor.getString(0);
    }

    public ArrayList<String> getAllNgayThangByCongTy(String congTy){
        ArrayList<String> arrayList = new ArrayList<>();
        Cursor cursor1 = database.rawQuery("SELECT " + SQLiteHelper.COULUMN_NGAY_THANG +" FROM " + SQLiteHelper.TABLE_CONG_NO + " WHERE " + SQLiteHelper.COULUMN_CONG_TY + " = " + "\"" + congTy + "\"",null);
        Cursor cursor = database.query(SQLiteHelper.TABLE_CONG_NO,new String[]{SQLiteHelper.COULUMN_NGAY_THANG},SQLiteHelper.COULUMN_CONG_TY  + " = \"" +congTy +"\"",null,null,null,SQLiteHelper.COULUMN_NGAY_THANG);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            String ngayThang = getNgayThang(cursor);
            if (!arrayList.contains(ngayThang)){
                arrayList.add(ngayThang);
            }
            cursor.moveToNext();
        }
        return arrayList;
    }
    public String getNgayThang(Cursor cursor){
        return cursor.getString(0);
    }

    public ArrayList<DonHang> getArrayDonHang(String ngayThang, String congTy){
        ArrayList<DonHang> arrayDonHang = new ArrayList<>();
        Log.e("AAA","SELECT " + SQLiteHelper.COLUMN_MAT_HANG + "," + SQLiteHelper.COLUMN_SO_LUONG + "," + SQLiteHelper.COLUMN_MENH_GIA+ "," + SQLiteHelper.COLUMN_ID + " FROM " + SQLiteHelper.TABLE_CONG_NO +" WHERE " + SQLiteHelper.COULUMN_NGAY_THANG + " = \"" + ngayThang + "\"" + " AND"  + SQLiteHelper.COULUMN_CONG_TY + " = \"" + congTy + "\"");
        Cursor cursor = database.rawQuery("SELECT " + SQLiteHelper.COLUMN_MAT_HANG + "," + SQLiteHelper.COLUMN_SO_LUONG + "," + SQLiteHelper.COLUMN_MENH_GIA + "," + SQLiteHelper.COLUMN_ID + " FROM " + SQLiteHelper.TABLE_CONG_NO +" WHERE " + SQLiteHelper.COULUMN_NGAY_THANG + " = \"" + ngayThang + "\"" + " AND "  + SQLiteHelper.COULUMN_CONG_TY + " = \"" + congTy + "\"",null);
        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            DonHang donHang = new DonHang();
            MatHang matHang = new MatHang();
            matHang.setName(cursor.getString(0));
            matHang.setMenhGia(cursor.getFloat(2));
            donHang.setMatHang(matHang);
            donHang.setSoLuong(cursor.getFloat(1));
            donHang.setId((int)cursor.getLong(3));
            arrayDonHang.add(donHang);
            cursor.moveToNext();
        }
        return arrayDonHang;
    }

    public void updateData(String s, DonHang donHang, String key){
        ContentValues values = new ContentValues();
        values.put(key,s);
        Log.e("AAA",SQLiteHelper.COLUMN_ID + " = \""+ donHang.getId()+ "\"");
        database.update(SQLiteHelper.TABLE_CONG_NO,values,SQLiteHelper.COLUMN_ID + " = \""+ donHang.getId()+ "\"" ,null);
    }
}
