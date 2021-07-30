package com.example.yolood;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.material.textfield.TextInputLayout;

public class UpdateActivity extends AppCompatActivity {
    Button update_btn, delete_btn;
    EditText title_input;
    TextInputLayout content_input;

    String id,title,context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update);

        //-------------------------------------------------ActionBar Color Change--------
        ActionBar actionBar;
        actionBar=getSupportActionBar();

        ColorDrawable colorDrawable
                =new ColorDrawable(Color.parseColor("#07B5B5"));
        actionBar.setBackgroundDrawable(colorDrawable);
        //---------------------------------------------------------------------------------

        update_btn=findViewById(R.id.update_btn);
        delete_btn=findViewById(R.id.delete_btn);
        title_input=findViewById(R.id.update_title_input);
        content_input=findViewById(R.id.update_content_input);

        //first we calll
        getIntentData();

        //Set actionbar title after getAndSetIntentData method
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setTitle(title);
        }

        update_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyDatabaseHelper myDB = new MyDatabaseHelper(UpdateActivity.this);
                title = title_input.getText().toString().trim();
                context = content_input.getEditText().getText().toString().trim();
                myDB.updateData(id, title, context);
            }
        });
        delete_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                confirmDialog();
            }
        });



    }
    void getIntentData(){
        if(getIntent().hasExtra("id") && getIntent().hasExtra("title") &&
        getIntent().hasExtra("context")){

            //getting data
            id=getIntent().getStringExtra("id");
            title=getIntent().getStringExtra("title");
            context=getIntent().getStringExtra("context");

            //setting data
            title_input.setText(title);
            content_input.getEditText().setText(context);
        }
        else{
            Toast.makeText(this,"no data",Toast.LENGTH_SHORT).show();
        }
    }
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete "+ title + " ?" );
        builder.setMessage("Silmek istediğinize emin misiniz? " + title + " ?" );
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB= new MyDatabaseHelper(UpdateActivity.this);
                myDB.deleteOneRow(id);
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });
        builder.create().show();
    }
}