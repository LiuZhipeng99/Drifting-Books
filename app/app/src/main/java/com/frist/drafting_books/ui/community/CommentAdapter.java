package com.frist.drafting_books.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.frist.drafting_books.R;

import java.util.List;
import java.util.Map;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private List<Map<String,String>> commentList;
    private Context context;
    public CommentAdapter(Context context, List<Map<String,String>> list) {
        this.context=context;
        commentList=list;
    }

    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View inflater = LayoutInflater.from(context).inflate(R.layout.comment_item,parent,false);
        MyViewHolder myViewHolder = new MyViewHolder(inflater);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder holder, int position) {
        //这里可能键值对不上.
            holder.commentview.setTitle(commentList.get(position).get("reader"));
            holder.commentview.setContent(commentList.get(position).get("comment"));
            Glide.with(context).load(commentList.get(position).get("headpic"))
                .placeholder(R.drawable.nobookcover)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .error(R.drawable.nobookcover).into(holder.imageView);
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class MyViewHolder extends RecyclerView.ViewHolder{
        com.hymane.expandtextview.ExpandTextView commentview;
        ImageView imageView;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            commentview=itemView.findViewById(R.id.commentitem);
            imageView=itemView.findViewById(R.id.headpic);
        }
    }
    public void addTheReplyData(Map<String,String> map){
        commentList.add(0,map);
        notifyDataSetChanged();//这个东西难道能提醒数据变动吗？
    }
}
