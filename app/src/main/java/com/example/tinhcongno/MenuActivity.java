package com.example.tinhcongno;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class MenuActivity extends AppCompatActivity {
    Button btnNhapMoi, btnXemCongNo, btnXemBaoGia;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AnhXa();

        btnNhapMoi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenWriteActivity();
            }
        });

        btnXemCongNo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenReadActivity();
            }
        });

        btnXemBaoGia.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenBaoGiaActivity();
            }
        });
    }
    void AnhXa(){
        btnNhapMoi = (Button)findViewById(R.id.btnNhapMoi);
        btnXemCongNo = (Button)findViewById(R.id.btnXemCongNo);
        btnXemBaoGia = (Button)findViewById(R.id.btnXemBaoGia);
    }

    void OpenWriteActivity(){
        Intent intent = new Intent(MenuActivity.this,NhapCongNo.class);
        startActivity(intent);
    }

    void OpenReadActivity(){
        Intent intent = new Intent(MenuActivity.this,DocCongNo.class);
        startActivity(intent);
    }
    void OpenBaoGiaActivity(){
        Intent intent = new Intent(MenuActivity.this,BaoGiaActivity.class);
        startActivity(intent);
    }
}