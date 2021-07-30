package com.example.yolood;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.widget.Toast;

import androidx.annotation.Nullable;

public class MyDatabaseHelper extends SQLiteOpenHelper {

    private Context context;

    private static final String DATABASE_NAME="NoteListy.db";
    private static final int DATABASE_VERSION = 1;

    private static final String TABLE_NAME="my_notelist";
    private static final String COLUMN_ID = "_id";
    private static final String COLUMN_TITLE="note_title";
    private static final String COLUMN_CONTENT="note_content";


    //Constructor (Yapıcı Metot) : Constructor bir sınıftan nesne oluşturulduğunda ilk çalışan metotlardır.
     MyDatabaseHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        this.context=context;
    }
    //tablomuzu yaratıyoruz
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME +
                " (" + COLUMN_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_TITLE + " TEXT, " +
                COLUMN_CONTENT + " TEXT);";
        db.execSQL(query);
    }
    //tablo daha önce yaratılmış mı , yaratılmışsa yeni versiyon mu kontrol ediyoruz
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    void addbook(String note_title , String note_content){
        //yazma işlemi için database i açıyoruz
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues bdb=new ContentValues();

        bdb.put(COLUMN_TITLE,note_title);
        bdb.put(COLUMN_CONTENT,note_content);
        long result =  db.insert(TABLE_NAME,null,bdb);
        if(result == -1){
            Toast.makeText(context,"Failed",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context, "insert başarılı", Toast.LENGTH_SHORT).show();
        }
    }

    //Cursor clasını import edip metodu yazıyoruz
    Cursor readAllData(){
        String query = "SELECT * FROM " + TABLE_NAME;
        SQLiteDatabase db = this.getReadableDatabase();

        Cursor cursor = null;   
        if(db != null){
            cursor = db.rawQuery(query, null);
        }
        return cursor;
    }

    void updateData(String row_id,String title,String content){
         SQLiteDatabase db=this.getWritableDatabase();
         ContentValues cv = new ContentValues();
         cv.put(COLUMN_TITLE,title);
         cv.put(COLUMN_CONTENT,content);

         long result=db.update(TABLE_NAME,cv,"_id=?",new String[]{row_id});
         if(result==-1){
             Toast.makeText(context,"Düzenlemede hata oluştu!",Toast.LENGTH_SHORT).show();
         }else {
             Toast.makeText(context,"Başarıyla güncellendi",Toast.LENGTH_SHORT).show();
         }
    }
    void deleteOneRow(String row_id){
         SQLiteDatabase db = this.getWritableDatabase();
        long result = db.delete(TABLE_NAME, "_id=?",new String[]{row_id});
        if(result==-1){
            Toast.makeText(context,"Failed to Delete",Toast.LENGTH_SHORT).show();
        }else{
            Toast.makeText(context,"Successfuly deleted",Toast.LENGTH_SHORT).show();
        }
    }
    void deleteAllData(){
         SQLiteDatabase db=this.getWritableDatabase();
         db.execSQL("DELETE FROM "+TABLE_NAME);
    }
}
