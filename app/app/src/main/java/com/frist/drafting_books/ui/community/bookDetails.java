
package com.frist.drafting_books.ui.community;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frist.drafting_books.DB.GetBookFromLean;
import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.R;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;


import java.util.List;
import java.util.Objects;

import cn.leancloud.AVObject;

public class bookDetails extends AppCompatActivity {
    private String bookId;
//    private BundleBoat bookBundle;//从上面传下来到
    private AVObject book;
    private JsonObject jb;//json解析出来的东西
    private Bundle bundle;
    private static final String TAG = "bookDetails";

    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

//        Log.d(TAG, "onCreate: "+bundle.getSerializable("book"));

        //解析书籍信息

        //初始化actionbar
        getSupportActionBar().setTitle("书籍详情");
        //左侧添加一个默认的返回图标
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent=getIntent();
        bundle=intent.getExtras();
        bookId=bundle.getString("bookid");
        LeancloudDB dbt = LeancloudDB.getInstance();
        //查询book
        dbt.showBookDetail(bookId, new GetBookFromLean() {
            @Override
            public void querySuccess(List<AVObject> books, List<AVObject> book_borrow) {

            }

            @Override
            public void querySuccess(List<AVObject> books) {

            }

            @Override
            public void queryOneSuccess(AVObject bookoj) {
                book=bookoj;
                Gson gs = new Gson();
                jb =gs.toJsonTree(book.get("book_json")).getAsJsonObject();
//        Log.d(TAG, "onCreate: "+jb);
                initRating();
                initDouBanButton();
                initCommentButton();
                initDetails();
                initLendButton();
            }

            @Override
            public void queryFail(Error e) {

            }
        });


        //解析AVObject



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
//TODO 加监听
void initLendButton(){
    Button button=findViewById(R.id.lend);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:在这里导入网址
            Intent intent=new Intent(bookDetails.this,Form.class);
            //传入书image，拥有者id
            intent.putExtra("to",book.get("email").toString());
            String cover_url = jb.getAsJsonObject("nameValuePairs").get("cover_url").toString();
            if(cover_url.length()>2){
                cover_url=cover_url.substring(1,cover_url.length()-1);
            }
            intent.putExtra("bookcover",cover_url);
            intent.putExtra("book_owner",book.get("userName").toString());
            startActivity(intent);
        }
    });
}
void initDetails(){
        //初始化封面。
    ImageView imageCover=findViewById(R.id.cover);

    String cover_url=jb.getAsJsonObject("nameValuePairs").get("cover_url").toString() ;
                if(cover_url.length()>2){
                    cover_url=cover_url.substring(1,cover_url.length()-1);
                }

        Glide.with(this).load(cover_url)
                .placeholder(R.drawable.nobookcover)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.nobookcover).into(imageCover);



    //初始化标题.
    TextView title=findViewById(R.id.title);
//    String t=jb.getAsJsonObject("nameValuePairs").get("title").toString();
    String tit=jb.getAsJsonObject("nameValuePairs").get("title").toString();
                if(tit.length()>2){
                    tit=tit.substring(1,tit.length()-1);
                }

        title.setText(tit);



    //概要 lable
    TextView abstr=findViewById(R.id.abstracts);
    String abs=jb.getAsJsonObject("nameValuePairs").get("abstract").toString();
    if(abs.length()>2){
        abs=abs.substring(1,abs.length());
    }
    JsonArray temp= (JsonArray) jb.getAsJsonObject("nameValuePairs").getAsJsonObject("labels").get("values");
    String lable=temp.get(1).toString();
    abstr.setText("摘要:  "+abs+"\n"+"标签:  "+lable);




    //内容简介

    TextView contentdetail=findViewById(R.id.contentdetail);
    String book_intro=jb.getAsJsonObject("nameValuePairs").get("book_intro").toString();
                if(book_intro.length()>2){
                    book_intro=book_intro.substring(1,book_intro.length()-1);

                }
    Log.d(TAG, "initDetails: "+book_intro);
        contentdetail.setText(book_intro.replace("\\n","\n"));



    //作者详情
    TextView authordetail=findViewById(R.id.authordetail);
    String author_intro=jb.getAsJsonObject("nameValuePairs").get("author_intro").toString();

                if(author_intro.length()>2){
                    author_intro=author_intro.substring(1,author_intro.length()-1);

                }
        authordetail.setText(author_intro.replace("\\n","\n"));


}

void initDouBanButton(){
    Button button=findViewById(R.id.doubanbutton);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:在这里导入网址
//            String url=jb.getAsJsonObject("nameValuePairs").get("url").toString() ;
//            Uri uri= Uri.parse(url.substring(1,url.length()-1));
            Uri uri;
            String url=jb.getAsJsonObject("nameValuePairs").get("url").toString();

                if(url.length()>2){
                    url=url.substring(1,url.length()-1);
                    uri=uri=Uri.parse(url);

                }else{
                    uri=Uri.parse("https://www.douban.com/");
                }
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
            bundle.putString("bookid",bookId);
            intent.putExtras(bundle);

            startActivity(intent);

        }
    });
}


void initRating(){
//TODO:要数据库获得数据append到textview
    //float rate=8.8f;
//    String star=jb.getAsJsonObject("nameValuePairs").getAsJsonObject("rating").getAsJsonObject("nameValuePairs").get("star_count").toString() ;
    String star=jb.getAsJsonObject("nameValuePairs").getAsJsonObject("rating").getAsJsonObject("nameValuePairs").get("star_count").toString() ;

    RatingBar ratingBar=findViewById(R.id.ratingbar);
    TextView textView=findViewById(R.id.rating_text);
    ratingBar.setRating(Float.parseFloat(star));
    textView.append(star);

    }

}