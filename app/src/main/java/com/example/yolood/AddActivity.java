package com.example.yolood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class AddActivity extends AppCompatActivity {
    Button add_btn;
    Context context;
    EditText title_input;
    TextInputLayout content_input;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        //-------------------------------------------------ActionBar Color Change--------
        ActionBar actionBar;
        actionBar=getSupportActionBar();

        ColorDrawable colorDrawable
                =new ColorDrawable(Color.parseColor("#07B5B5"));
        actionBar.setBackgroundDrawable(colorDrawable);
        //---------------------------------------------------------------------------------


        content_input=findViewById(R.id.content_input);
        title_input=findViewById(R.id.title_input);
        add_btn=findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(AddActivity.this);
                myDB.addbook(title_input.getText().toString().trim(),
                        content_input.getEditText().getText().toString().trim());

            }
        });

    }
}