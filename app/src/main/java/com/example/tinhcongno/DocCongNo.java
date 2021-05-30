package com.example.tinhcongno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.ExpandableListView;
import android.widget.ListView;
import android.widget.Toast;

import com.example.tinhcongno.adapter.ExpandableAdapterCongTy;
import com.example.tinhcongno.database.DataSource;
import com.example.tinhcongno.model.DonHang;

import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class DocCongNo extends AppCompatActivity {

    Button btnXuatFileExcelCaThang;
    ExpandableListView listView;
    ArrayList<String> arrayCongTy;
    HashMap<String, ArrayList<String>> arrayNgayThang = new HashMap<>();
    DataSource dataSource;
    ExpandableAdapterCongTy adapter;

    FileOutputStream fos = null;
    CellType cellType = CellType.STRING;
    File fileOutput = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS),"Tổng Công nợ.xlsx");


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_doc_cong_no);

        AnhXa();
        dataSource = new DataSource(this);
        dataSource.open();

        arrayCongTy = dataSource.getAllCongTy();

        for (String s : arrayCongTy){
            arrayNgayThang.put(s,dataSource.getAllNgayThangByCongTy(s));
        }

        adapter = new ExpandableAdapterCongTy(arrayCongTy,arrayNgayThang);
        listView.setAdapter(adapter);

        listView.setOnChildClickListener(new ExpandableListView.OnChildClickListener() {
            @Override
            public boolean onChildClick(ExpandableListView parent, View v, int groupPosition, int childPosition, long id) {
                String congTy = arrayCongTy.get(groupPosition);
                String ngayThang = arrayNgayThang.get(congTy).get(childPosition);
                Intent intent = new Intent(DocCongNo.this,CongNoChiTiet.class);
                intent.putExtra("ngaythang",ngayThang);
                intent.putExtra("congty",congTy);
                startActivity(intent);
                return true;
            }
        });

        btnXuatFileExcelCaThang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuatFileExcelCaThang();
            }
        });
    }
    void AnhXa(){
        listView = (ExpandableListView)findViewById(R.id.listview);
        btnXuatFileExcelCaThang = (Button)findViewById(R.id.btnXuatFileExcelCaThang);
    }
    void XuatFileExcelCaThang(){
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = null;
        int i;
        Row row = null;
        Cell cellNgayThang = null;
        Cell cellMatHang = null;
        Cell cellSoLuong = null;
        Cell cellMenhGia = null;
        Cell cellThanhTien = null;
        Cell cellTongTien = null;
        for (String congTy : arrayCongTy){
            i=0;
            sheet = workbook.createSheet(congTy);

            row = sheet.createRow(i);
            cellNgayThang = row.createCell(0);
            cellNgayThang.setCellValue("Ngày");

            cellMatHang = row.createCell(1);
            cellMatHang.setCellValue("Mặt hàng");

            cellSoLuong = row.createCell(2);
            cellSoLuong.setCellValue("Số lượng");

            cellMenhGia = row.createCell(3);
            cellMenhGia.setCellValue("Đơn giá");

            cellThanhTien = row.createCell(4);
            cellThanhTien.setCellValue("Thành tiền");

            cellTongTien = row.createCell(5);
            cellTongTien.setCellValue("Tổng ngày");
            i++;
            for (String ngayThang: arrayNgayThang.get(congTy)){
                row = sheet.createRow(i);
                cellNgayThang = row.createCell(0);
                cellNgayThang.setCellValue(ngayThang);

                cellTongTien = row.createCell(5);

                i++;
                ArrayList<DonHang> arrayDonHang = dataSource.getArrayDonHang(ngayThang,congTy);

                cellTongTien.setCellFormula("SUM(E"+String.valueOf(i+1)+":E"+String.valueOf(i+arrayDonHang.size())+")");
                for (int j=0;j<arrayDonHang.size();j++){
                    DonHang donHang = arrayDonHang.get(j);
                    row = sheet.createRow(i);

                    cellMatHang = row.createCell(1);
                    cellMatHang.setCellValue(donHang.getMatHang().getName());

                    cellSoLuong = row.createCell(2);
                    cellSoLuong.setCellValue(donHang.getSoLuong());

                    cellMenhGia = row.createCell(3);
                    cellMenhGia.setCellValue(donHang.getMatHang().getMenhGia());

                    cellThanhTien = row.createCell(4);
                    cellThanhTien.setCellFormula("C" + String.valueOf(i+1) + "*" + "D" +String.valueOf(i+1));
                    i++;
                }
            }
        }
        try {
            fos = new FileOutputStream(fileOutput);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        try {
            workbook.write(fos);
            Toast.makeText(this,"Xuất File Thành Công",Toast.LENGTH_LONG).show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}