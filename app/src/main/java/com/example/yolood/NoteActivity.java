package com.example.yolood;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

public class NoteActivity extends AppCompatActivity {
    RecyclerView recyclerView;
    FloatingActionButton add_btn;
    CustomAdapter customAdapter;
    MyDatabaseHelper myDB;
    ArrayList<String> note_id,note_title,note_content;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_note);

//renk değiştirme kodları------------
        ActionBar actionBar;
        actionBar=getSupportActionBar();

        ColorDrawable colorDrawable
                =new ColorDrawable(Color.parseColor("#07B5B5"));
        actionBar.setBackgroundDrawable(colorDrawable);
//---------------------------------

        recyclerView=findViewById(R.id.recyclerView);
        add_btn=findViewById(R.id.add_btn);
        add_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(NoteActivity.this,AddActivity.class);
                startActivity(intent);
            }
        });

        myDB=new MyDatabaseHelper(NoteActivity.this);
        note_id=new ArrayList<>();
        note_title=new ArrayList<>();
        note_content=new ArrayList<>();

        getAllData();
        customAdapter=new CustomAdapter(NoteActivity.this,this,
                note_id,note_title,note_content);
        recyclerView.setAdapter(customAdapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(NoteActivity.this));
    }
    @Override
    protected void  onActivityResult(int requestCode, int resultCode , @Nullable Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if(requestCode==1){
            recreate();
        }
    }

    void getAllData(){
        Cursor cursor = myDB.readAllData();
        if(cursor.getCount()==0){
            Toast.makeText(this,"No Data",Toast.LENGTH_SHORT).show();
        }else{
            while(cursor.moveToNext()){
                note_id.add(cursor.getString(0));
                note_title.add(cursor.getString(1));
                note_content.add(cursor.getString(2));
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu){
        MenuInflater inflater=getMenuInflater();
        inflater.inflate(R.menu.my_menu,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (item.getItemId()==R.id.delete_all){
            confirmDialog();
        }

        return super.onOptionsItemSelected(item);
    }
    void confirmDialog(){
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Delete All" );
        builder.setMessage("Silmek istediğinize emin misiniz" );
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                MyDatabaseHelper myDB=new MyDatabaseHelper(NoteActivity.this);
                myDB.deleteAllData();
                //Refresh Activity
                Intent intent = new Intent(NoteActivity.this,NoteActivity.class);
                startActivity(intent);
                finish();
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
