package com.example.tinhcongno;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.example.tinhcongno.database.DataSource;
import com.example.tinhcongno.database.SQLiteHelper;
import com.example.tinhcongno.model.DonHang;
import com.example.tinhcongno.model.MatHang;

import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Iterator;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class NhapCongNo extends AppCompatActivity {

    Button btnChuyen;
    Spinner spnChonCongTy;
    AutoCompleteTextView edtMatHang;
    EditText edtSoLuong;
    TextView tvDatePicker;
    TextView tvDate;
    EditText edtMenhGia;
    Button btnNhap;
    TextView tvMatHangLichSu, tvSoLuongLichSu, tvMenhGiaLichSu;

    ArrayList<MatHang> arrayMatHang;
    ArrayList<String> arrayNameMatHang = new ArrayList<>();
    ArrayList<String> arrayCongTy = new ArrayList<>();
    HashMap<String, Float> hashMap = new HashMap<>();

    DataSource dataSource;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nhap_cong_no);

        AnhXa();
        dataSource = new DataSource(this);
        dataSource.open();
        arrayMatHang = dataSource.getAllMatHangFromBaoGia();

        CreateSpinner();
        CreateAutoTextView();

        //Chọn ngày tháng
        tvDatePicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatePicker();
            }
        });

        edtMatHang.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (s.length() != 0){
                    edtMenhGia.setText(hashMap.get(s.toString())+"");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        btnNhap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Nhập hàng
                WriteData();
            }
        });

        btnChuyen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NhapCongNo.this, CongNoChiTiet.class);
                intent.putExtra("congty",spnChonCongTy.getSelectedItem().toString());
                intent.putExtra("ngaythang",tvDate.getText());
                startActivity(intent);
            }
        });
    }

    void AnhXa(){
        btnChuyen = (Button)findViewById(R.id.btnChuyen);
        spnChonCongTy = (Spinner)findViewById(R.id.spnChonCongTy);
        edtMatHang = (AutoCompleteTextView)findViewById(R.id.edtMatHang);
        edtMenhGia = (EditText)findViewById(R.id.edtMenhGia);
        edtSoLuong = (EditText)findViewById(R.id.edtSoLuong);
        btnNhap = (Button)findViewById(R.id.btnNhap);
        tvDate = (TextView)findViewById(R.id.tvDate);
        tvDatePicker = (TextView)findViewById(R.id.tvDatePicker);
        tvMatHangLichSu = (TextView)findViewById(R.id.tvMatHangLichSu);
        tvSoLuongLichSu = (TextView)findViewById(R.id.tvSoLuongLichSu);
        tvMenhGiaLichSu = (TextView)findViewById(R.id.tvMenhGiaLichSu);
    }

    private void CreateSpinner(){
        arrayCongTy.add("Hamaden");
        arrayCongTy.add("Vietinak");
        arrayCongTy.add("Fuji");
        arrayCongTy.add("Shin");
        arrayCongTy.add("Topy");
        arrayCongTy.add("Tân Hà Phát");
        arrayCongTy.add("Nhà hàng Nhật");
        arrayCongTy.add("Cafeshop");
        ArrayAdapter arrayAdapterCongTy = new ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item,arrayCongTy);
        arrayAdapterCongTy.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spnChonCongTy.setAdapter(arrayAdapterCongTy);
    }
    private void CreateAutoTextView(){
        // tạo arrayMathang và Hashtable Mat Hang
        for (MatHang item : arrayMatHang){
            arrayNameMatHang.add(item.getName());
            hashMap.put(item.getName(),item.getMenhGia());
        }
        ArrayAdapter arrayAdapterMatHang = new ArrayAdapter(this, android.R.layout.simple_list_item_1,arrayNameMatHang);
        edtMatHang.setAdapter(arrayAdapterMatHang);
    }
    private void DatePicker(){
        Calendar calendar = Calendar.getInstance();
        int nam = calendar.get(Calendar.YEAR);
        int thang = calendar.get(Calendar.MONTH);
        int ngay = calendar.get(Calendar.DATE);
        DatePickerDialog datePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            @Override

            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                calendar.set(year,month,dayOfMonth);
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("dd/MM/yy");
                tvDate.setText(simpleDateFormat.format(calendar.getTime()));
            }
        },nam,thang,ngay);
        datePickerDialog.show();
    }

    private void WriteData(){
        String congTy = spnChonCongTy.getSelectedItem().toString();
        if (congTy.equals("")){
            Toast.makeText(this,"Chưa nhập Công Ty",Toast.LENGTH_LONG).show();
            return;
        }
        String ngayThang = tvDate.getText().toString();
        if (ngayThang.equals("")){
            Toast.makeText(this,"Chưa nhập Ngày Tháng",Toast.LENGTH_LONG).show();
            return;
        }
        String matHang = edtMatHang.getText().toString();
        if (matHang.equals("")){
            Toast.makeText(this,"Chưa nhập Mặt Hàng",Toast.LENGTH_LONG).show();
            return;
        }
        String soLuong = edtSoLuong.getText().toString();
        if (soLuong.equals("")){
            Toast.makeText(this,"Chưa nhập Số lượng",Toast.LENGTH_LONG).show();
            return;
        }
        String menhGia = edtMenhGia.getText().toString();
        if (menhGia.equals("")){
            Toast.makeText(this,"Chưa nhập Mệnh giá",Toast.LENGTH_LONG).show();
            return;
        }

        if (hashMap.get(matHang) != null){
            if (hashMap.get(matHang).toString() != menhGia){
                int i = arrayNameMatHang.indexOf(matHang);
                MatHang mh = arrayMatHang.get(i);
                hashMap.replace(matHang,Float.parseFloat(menhGia));
                dataSource.updateDataToBaoGia(menhGia,mh, SQLiteHelper.COLUMN_MENH_GIA);
            }
        }
        else{
            MatHang mh = new MatHang(matHang,Float.parseFloat(menhGia));
            arrayMatHang.add(mh);
            CreateAutoTextView();
            dataSource.addMatHangToBaoGia(mh);
        }
        MatHang hang = new MatHang(matHang,Float.parseFloat(menhGia));
        DonHang donHang = new DonHang(hang,Float.parseFloat(soLuong),congTy,ngayThang);

        dataSource.addDonHangtoCongNo(donHang);

        edtMatHang.setText("");
        edtSoLuong.setText("");
        edtMenhGia.setText("");

        tvMatHangLichSu.setText(matHang);
        tvMenhGiaLichSu.setText(menhGia);
        tvSoLuongLichSu.setText(soLuong);
    }
}