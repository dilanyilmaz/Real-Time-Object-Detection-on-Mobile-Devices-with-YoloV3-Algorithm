package com.example.yolood;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class MainSelect extends AppCompatActivity {
    Button btn1, btn2, btn3;
    Context context = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select);
        btn1 = findViewById(R.id.btn1);//Nesne İle Button İlişkilendirme
        btn3 = findViewById(R.id.btn3);//Nesne İle Button İlişkilendirme

        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent sınıfından nesnemizi oluşturuyoruz.
                //context ya da MainActivity.this diyerek bu activity de çalışacağını belirtiyoruz
                //Intent ıntent = new Intent(MainActivity.this, SecondActivity.class);
                Intent ıntent = new Intent(context, MainActivity.class);
                //Activity başlatıyoruz bizden intent türünde nesne istiyor kendi oluşturduğumuz nesneyi kullanıyoruz.
                startActivity(ıntent);
            }
        });
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Intent sınıfından nesnemizi oluşturuyoruz.
                //context ya da MainActivity.this diyerek bu activity de çalışacağını belirtiyoruz
                //Intent ıntent = new Intent(MainActivity.this, SecondActivity.class);
                Intent ıntent = new Intent(context, NoteActivity.class);
                //Activity başlatıyoruz bizden intent türünde nesne istiyor kendi oluşturduğumuz nesneyi kullanıyoruz.
                startActivity(ıntent);
            }
        });

    }
}
