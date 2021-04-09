package com.frist.drafting_books.ui.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.frist.drafting_books.R;

import java.util.List;
import java.util.Map;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "MyRecyclerViewAdapter";
    private Context context;
    private List<Map<String,String>> list;

    public MyRecyclerViewAdapter(Context context, List<Map<String,String>> list){
        this.context = context;
        this.list = list;
    }
    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.fragment_blank_recyclerview_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;

    }

    //用来绑定数据到view上。
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        Glide.with(context).load(list.get(position).get("cover"))
                .placeholder(R.mipmap.ic_launcher)
                .error(R.mipmap.ic_launcher).into(holder.imageView);
        holder.textView.setText(list.get(position).get("title"));
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(context,bookDetails.class);
                Bundle bundle=new Bundle();
                bundle.putString("bookId",list.get(position).get("id"));
                intent.putExtras(bundle);
                context.startActivity(intent);


            }
        });
    }



    @Override
    public int getItemCount() {
        return list.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        ImageView imageView;
        TextView textView;
        LinearLayout linearLayout;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView=itemView.findViewById(R.id.image_view);
            textView=itemView.findViewById(R.id.text_view);
            linearLayout=itemView.findViewById(R.id.item_layout);

        }
    }
}
