package com.example.tinhcongno.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class SQLiteHelper extends SQLiteOpenHelper {
    public static final String TABLE_BAO_GIA = "bao_gia";
    public static final String TABLE_CONG_NO = "cong_no";

    public static final String COLUMN_ID = "_id";
    public static final String COLUMN_MAT_HANG = "mat_hang";
    public static final String COLUMN_MENH_GIA = "menh_gia";
    public static final String COLUMN_SO_LUONG = "so_luong";
    public static final String COULUMN_CONG_TY = "cong_ty";
    public static final String COULUMN_NGAY_THANG = "ngay_thang";

    private static final String DATABASE_NAME = "CongNo.db";
    private static final int VERSION = 1;

    private static final String CREATE_TABLE_BAO_GIA = "create table "
            + TABLE_BAO_GIA
            + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MAT_HANG + " text not null, "
            + COLUMN_MENH_GIA + " double);";

    private static final String CREATE_TABLE_CONG_NO = "create table "
            + TABLE_CONG_NO
            + "( " + COLUMN_ID + " integer primary key autoincrement, "
            + COLUMN_MAT_HANG + " text not null, "
            + COLUMN_SO_LUONG + " double not null, "
            + COULUMN_CONG_TY + " text not null, "
            + COULUMN_NGAY_THANG + " text not null, "
            + COLUMN_MENH_GIA + " double not null);";

    public SQLiteHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE_BAO_GIA);
        db.execSQL(CREATE_TABLE_CONG_NO);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_CONG_NO);
        db.execSQL("DROP TABLE IF EXISTS "+ TABLE_BAO_GIA);
    }
}
