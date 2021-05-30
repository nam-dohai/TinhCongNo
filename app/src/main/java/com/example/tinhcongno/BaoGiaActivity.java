package com.example.tinhcongno;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.example.tinhcongno.adapter.BaoGiaAdapter;
import com.example.tinhcongno.database.DataSource;
import com.example.tinhcongno.model.MatHang;

import java.util.ArrayList;

public class BaoGiaActivity extends AppCompatActivity {

    EditText edtSearch;
    ListView listBaoGia;
    Button btnBaoGiaSaveChange, btnAddMatHang;
    ArrayList<MatHang> arrayMatHang = new ArrayList<>();
    DataSource dataSource;
    BaoGiaAdapter adapter;

    ListenerDataChange listener = new ListenerDataChange() {
        @Override
        public void listener() {
            btnBaoGiaSaveChange.setClickable(true);
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bao_gia);

        AnhXa();
        dataSource = new DataSource(this);
        dataSource.open();

        arrayMatHang = dataSource.getAllMatHangFromBaoGia();
        adapter = new BaoGiaAdapter(this,arrayMatHang, listener, dataSource);
        listBaoGia.setAdapter(adapter);

        btnBaoGiaSaveChange.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                btnBaoGiaSaveChange.setClickable(false);

                //Lưu lại tất cả các thay đổi
                ArrayList<ListenerButtonChangeClick> arrayListener = adapter.getArrayListener();
                for (ListenerButtonChangeClick listen : arrayListener){
                    listen.listener();
                }

                adapter.deleteArrayListener(); //Xóa hết các thay đổi đã lưu
            }
        });

        edtSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                search(s.toString());
            }
        });

        btnAddMatHang.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openDialog();

            }
        });

        listBaoGia.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                OpenDialogDelete(position);
                return false;
            }
        });
    }
    void AnhXa(){
        listBaoGia = (ListView)findViewById(R.id.listBaoGia);
        btnBaoGiaSaveChange = (Button)findViewById(R.id.btnBaoGiaSaveChange);
        edtSearch = (EditText)findViewById(R.id.edtSearch);
        btnAddMatHang = (Button)findViewById(R.id.btnAddMatHang);
    }
    void search(String s){
        ArrayList<MatHang> array = new ArrayList<>();
        for (MatHang matHang : arrayMatHang){
            if (matHang.getName().toLowerCase().contains(s.toLowerCase())){
                array.add(matHang);
            }
        }
        adapter.searchList(array);
    }
    void openDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_add_mat_hang);

        EditText edtAddMatHang = (EditText)dialog.findViewById(R.id.edtAddMatHang);
        EditText edtAddMenhGia = (EditText)dialog.findViewById(R.id.edtAddMenhGia);
        Button btnAddSave = (Button)dialog.findViewById(R.id.btnAddSave);
        Button btnAddDismiss = (Button)dialog.findViewById(R.id.btnAddDismiss);

        btnAddSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v){
                MatHang matHang = new MatHang(edtAddMatHang.getText().toString(),Float.parseFloat(edtAddMenhGia.getText().toString()));
                dataSource.addMatHangToBaoGia(matHang);
                matHang = dataSource.getLastMatHangFromBaoGia();
                arrayMatHang.add(matHang);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });

        btnAddDismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
            }
        });
        dialog.show();
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
                dataSource.removeMatHangFromBaoGia(arrayMatHang.get(position));
                arrayMatHang.remove(position);
                adapter.notifyDataSetChanged();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}