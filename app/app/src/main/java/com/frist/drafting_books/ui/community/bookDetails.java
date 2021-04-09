package com.frist.drafting_books.ui.community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.frist.drafting_books.R;

import java.util.Objects;

public class bookDetails extends AppCompatActivity {
    private String bookId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Bundle bundle = new Bundle();
        bundle = getIntent().getExtras();
        //bookId = bundle.getString("bookId");
//        getSupportActionBar().hide();

        //左侧添加一个默认的返回图标
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);
        initRating();
        initDouBanButton();
        initCommentButton();

    }

    //下面可以实现返回键
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        //Toolbar的事件---返回
        if (item.getItemId() == android.R.id.home) {
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

void initDouBanButton(){
    Button button=findViewById(R.id.doubanbutton);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:在这里导入网址
            Uri uri= Uri.parse("https://www.douban.com/");
            Intent intent=new Intent(Intent.ACTION_VIEW,uri);
            startActivity(intent);


        }
    });
}
void initCommentButton(){
    Button button=findViewById(R.id.commentButton);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:在这里导入网址
            Intent intent=new Intent();
            intent.setClass(bookDetails.this, Comment.class);
            Bundle bundle=new Bundle();
            bundle.putString("id","1");
            intent.putExtras(bundle);
            startActivity(intent);

        }
    });
}


void initRating(){
//TODO:要数据库获得数据append到textview
    float rate=8.8f;
    RatingBar ratingBar=findViewById(R.id.ratingbar);
    TextView textView=findViewById(R.id.rating_text);
    ratingBar.setRating(rate/2);//注意，这里10分要初二
    textView.append(Float.toString(rate));

    }

}