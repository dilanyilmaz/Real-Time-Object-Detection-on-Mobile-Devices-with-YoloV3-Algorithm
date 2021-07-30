package com.example.yolood;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CustomAdapter extends RecyclerView.Adapter<CustomAdapter.MyViewHolder>{

    //Veriyi saklama işlemini gerçekleştiriyoruz
   private Context context;
    Activity activity;
   private ArrayList note_id,note_title,note_context;

    CustomAdapter(Activity activity,
                  Context context,
                  ArrayList note_id,
                  ArrayList note_title,
                  ArrayList note_context){
        this.activity=activity;
        this.context=context;
        this.note_id=note_id;
        this.note_title=note_title;
        this.note_context=note_context;

    }



    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.my_row,parent,false);
        return new MyViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        holder.note_id_txt.setText(String.valueOf(note_id.get(position)));
        holder.note_title_txt.setText(String.valueOf(note_title.get(position)));
        holder.note_txt.setText(String.valueOf(note_context.get(position)));

        //-----------------güncelleme--------------------------------------

        holder.mainLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent =new Intent(context,UpdateActivity.class);
                intent.putExtra("id",String.valueOf(note_id.get(position)));
                intent.putExtra("title",String.valueOf(note_title.get(position)));
                intent.putExtra("context",String.valueOf(note_context.get(position)));
                activity.startActivityForResult(intent,1);
            }
        });
    }

    @Override
    public int getItemCount() {
        //herhangi bir dizinin size ı olur onemli olan size bilgisi vermek
        return note_id.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        TextView note_id_txt,note_txt,note_title_txt;
        LinearLayout mainLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            note_id_txt=itemView.findViewById(R.id.note_id_txt);
            note_title_txt=itemView.findViewById(R.id.note_title_txt);
            note_txt=itemView.findViewById(R.id.note_txt);
            mainLayout=itemView.findViewById(R.id.mainLayout);
        }
    }
}
