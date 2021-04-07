package com.frist.drafting_books.ui.community;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.frist.drafting_books.R;

import java.util.List;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.MyViewHolder> {
    private List<String> commentList;
    private Context context;
    public CommentAdapter(Context context, List<String> list) {
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
            holder.commentview.setContent(commentList.get(position));
    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }
    class MyViewHolder extends RecyclerView.ViewHolder{
        com.hymane.expandtextview.ExpandTextView commentview;
        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            commentview=itemView.findViewById(R.id.commentitem);

        }
    }
}
