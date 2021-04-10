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
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;

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
    private List<BundleBoat> list;

    public MyRecyclerViewAdapter(Context context, List<BundleBoat> list){
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
//        if(list==null){
//            Log.d(TAG, "onBindViewHolder: ");
//        }else {
//            Gson gs = new Gson();
//            JsonObject jb =gs.toJsonTree(list.get(0).get("book_json")).getAsJsonObject();
//            String cover_url=jb.getAsJsonObject("nameValuePairs").get("cover_url").toString();
//            Log.d(TAG, "onBindViewHolder: "+cover_url);
//
//        }
        //Log.d(TAG, "onBindViewHolder: "+position);
        Gson gs = new Gson();
        JsonObject jb =gs.toJsonTree(list.get(position).get("book_json")).getAsJsonObject();
        String cover_url=jb.getAsJsonObject("nameValuePairs").get("cover_url").toString() ;
        cover_url=cover_url.substring(1,cover_url.length()-1);
        String title=jb.getAsJsonObject("nameValuePairs").get("title").toString();
        title=title.substring(1,title.length()-1);
        //Log.d(TAG, "onBindViewHolder: "+cover_url);
        //String covertest = "https://img1.doubanio.com/view/subject/m/public/s32344288.jpg";
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

                //传入图片
//                JsonObject jb =gs.toJsonTree(list.get(position).get("book_json")).getAsJsonObject();
//                String cover_url=jb.getAsJsonObject("nameValuePairs").get("cover_url").toString() ;
//                if(cover_url.length()>2){
//                    cover_url=cover_url.substring(1,cover_url.length()-1);
//                }
//
//                bundle.putString("cover_url",cover_url);
//                //传入标题
//                String title=jb.getAsJsonObject("nameValuePairs").get("title").toString();
//                if(title.length()>2){
//                    title=title.substring(1,title.length()-1);
//                }
//
//                bundle.putString("title",title);
//                //传入摘要
//                String abs=jb.getAsJsonObject("nameValuePairs").get("abstract").toString();
//                bundle.putString("abstract",abs);
//                //传入标签
//                JsonArray temp= (JsonArray) jb.getAsJsonObject("nameValuePairs").getAsJsonObject("labels").get("values");
//                String lable=temp.get(1).toString();
//                bundle.putString("lable",lable);
////                Log.d(TAG, "onClick: "+ti);
//                //传入豆瓣评分
//                String star=jb.getAsJsonObject("nameValuePairs").getAsJsonObject("rating").getAsJsonObject("nameValuePairs").get("star_count").toString() ;
//                Log.d(TAG, "onClick: "+star);
//                bundle.putString("stars",star);
//                //内容简介
//                String book_intro=jb.getAsJsonObject("nameValuePairs").get("book_intro").toString();
//                if(book_intro.length()>2){
//                    book_intro=book_intro.substring(1,title.length()-1);
//
//                }
//                bundle.putString("book_intro",book_intro);
//                //作者详情
//                String author_intro=jb.getAsJsonObject("nameValuePairs").get("author_intro").toString();
//                Log.d(TAG, "onClick: "+author_intro);
//                if(author_intro.length()>2){
//                    author_intro=author_intro.substring(1,title.length()-1);
//
//                }
//                bundle.putString("author_intro",author_intro);
//                //传入url
//                String url=jb.getAsJsonObject("nameValuePairs").get("url").toString();
//                if(url.length()>2){
//                    url=url.substring(1,url.length()-1);
//
//                }
//                bundle.putString("url",url);
                Bundle bundle=new Bundle();
                String bookid=list.get(position).getObjectId();
                bundle.putString("bookid",bookid);
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
