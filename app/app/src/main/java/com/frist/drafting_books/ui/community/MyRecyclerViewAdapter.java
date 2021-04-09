package com.frist.drafting_books.ui.community;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frist.drafting_books.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import java.io.Serializable;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.AVObject;

public class MyRecyclerViewAdapter extends RecyclerView.Adapter<MyRecyclerViewAdapter.MyViewHolder> {
    private static final String TAG = "MyRecyclerViewAdapter";
    private Context context;
    private List<AVObject> list;

    public MyRecyclerViewAdapter(Context context, List<AVObject> list){
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
    static class BookBundle extends AVObject implements Serializable {

        BookBundle(AVObject book){
            super(book);

        }
    }
    //用来绑定数据到view上。
    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
//        if(list==null){
//            Log.d(TAG, "onBindViewHolder: ");
//        }else {
//            Gson gs = new Gson();
//            JsonObject jb =gs.toJsonTree(list.get(0).get("book_json")).getAsJsonObject();
//            String cover_url=jb.getAsJsonObject("nameValuePairs").get("cover_url").toString();
//            Log.d(TAG, "onBindViewHolder: "+cover_url);
//
//        }
        Log.d(TAG, "onBindViewHolder: "+position);
        Gson gs = new Gson();
        JsonObject jb =gs.toJsonTree(list.get(position).get("book_json")).getAsJsonObject();
        String cover_url=jb.getAsJsonObject("nameValuePairs").get("cover_url").toString() ;
        cover_url=cover_url.substring(1,cover_url.length()-1);
        String title=jb.getAsJsonObject("nameValuePairs").get("title").toString();
        title=title.substring(1,title.length()-1);
        //Log.d(TAG, "onBindViewHolder: "+cover_url);
        String covertest = "https://img1.doubanio.com/view/subject/m/public/s32344288.jpg";
        //System.out.println(cover_url);
        Glide.with(context).load(cover_url)
                .placeholder(R.drawable.nobookcover)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.nobookcover).into(holder.imageView);

        holder.textView.setText(title);
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent();
                intent.setClass(context,bookDetails.class);
                Bundle bundle=new Bundle();
                BookBundle bb=new BookBundle(list.get(position));
                bundle.putSerializable("book",bb);
                intent.putExtras(bundle);
                context.startActivity(intent);//内部的类也要继承


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
