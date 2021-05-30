package com.example.tinhcongno;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.baoyz.swipemenulistview.SwipeMenuListView;
import com.example.tinhcongno.adapter.DonHangAdapter;
import com.example.tinhcongno.database.DataSource;
import com.example.tinhcongno.model.DonHang;
import com.example.tinhcongno.model.MatHang;

import org.apache.poi.ss.formula.functions.T;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellType;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.Sheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.w3c.dom.Text;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.ArrayList;

import static android.os.Environment.DIRECTORY_DOCUMENTS;

public class CongNoChiTiet extends AppCompatActivity {

    Button btnXuatFileExcel, btnSaveChange;
    TextView txtNgayThang, txtCongTy;
    ListView listDonHang;
    ArrayList<DonHang> arrayDonHang;
    DataSource dataSource;
    DonHangAdapter adapterDonHang;

    FileOutputStream fos = null;
    CellType cellType = CellType.STRING;
    File fileOutput = new File(Environment.getExternalStoragePublicDirectory(DIRECTORY_DOCUMENTS),"Công nợ.xlsx");

    ListenerDataChange listener = new ListenerDataChange() {
        @Override
        public void listener() {
            btnSaveChange.setClickable(true);
            btnXuatFileExcel.setClickable(false);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cong_no_chi_tiet);

        AnhXa();
        dataSource = new DataSource(this);
        dataSource.open();

        Intent intent = getIntent();
        String ngayThang = intent.getStringExtra("ngaythang");
        String congTy = intent.getStringExtra("congty");

        if (ngayThang.equals("") || congTy.equals("")){
            Toast.makeText(this,"Chưa nhập dữ liệu",Toast.LENGTH_LONG).show();
        }

        txtCongTy.setText(congTy);
        txtNgayThang.setText(ngayThang);

        arrayDonHang = dataSource.getArrayDonHang(ngayThang,congTy);

        adapterDonHang = new DonHangAdapter(this,arrayDonHang,listener,dataSource);
        listDonHang.setAdapter(adapterDonHang);

        listDonHang.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                OpenDialogDelete(position);
                return false;
            }
        });

        btnXuatFileExcel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                XuatFileExcel();
            }
        });

        btnSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnXuatFileExcel.setClickable(true);
                btnSaveChange.setClickable(false);

                //Lưu lại tất cả các thay đổi
                ArrayList<ListenerButtonChangeClick> arrayListener = adapterDonHang.getArrayListener();
                for (ListenerButtonChangeClick listen : arrayListener){
                    listen.listener();
                }
                adapterDonHang.deleteArrayListener(); //Xóa hết các thay đổi đã lưu
            }
        });


    }
    void AnhXa(){
        listDonHang = (ListView) findViewById(R.id.listDonHang);
        btnXuatFileExcel = (Button)findViewById(R.id.btnXuatFileExcel);
        txtCongTy = (TextView)findViewById(R.id.txtCongTy);
        txtNgayThang = (TextView)findViewById(R.id.txtNgayThang);
        btnSaveChange = (Button)findViewById(R.id.btnSaveChange);
    }
    private void XuatFileExcel(){
        XSSFWorkbook workbook = new XSSFWorkbook();
        Sheet sheet = workbook.createSheet();
        for (int i=0;i<arrayDonHang.size();i++){
            DonHang donHang = arrayDonHang.get(i);
            Row row = sheet.createRow(i);

            Cell cellMatHang = row.createCell(0);
            cellMatHang.setCellValue(donHang.getMatHang().getName());

            Cell cellSoLuong = row.createCell(1);
            cellSoLuong.setCellValue(donHang.getSoLuong());

            Cell cellMenhGia = row.createCell(2);
            cellMenhGia.setCellValue(donHang.getMatHang().getMenhGia());

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
    void OpenDialogDelete(int position){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_delete_items);
        Button btnDeleteItem = (Button)dialog.findViewById(R.id.btnDeleteItem);
        Button btnDeleteDismiss = (Button)dialog.findViewById(R.id.btnDeleteDismiss);

        btnDeleteDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });

        btnDeleteItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dataSource.removeDonHangFromCongNo(arrayDonHang.get(position));
                arrayDonHang.remove(position);
                adapterDonHang.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}