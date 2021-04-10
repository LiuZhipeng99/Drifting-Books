
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

import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frist.drafting_books.R;
import com.google.gson.Gson;
import com.google.gson.JsonObject;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.Objects;

import cn.leancloud.AVObject;

public class bookDetails extends AppCompatActivity {
    private String bookId;
//    private BundleBoat bookBundle;//从上面传下来到
    private AVObject book;
    private JsonObject jb;//json解析出来的东西
    private Bundle bundle;
    private static final String TAG = "bookDetails";
    @Subscribe
    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);

//        Log.d(TAG, "onCreate: "+bundle.getSerializable("book"));

        //解析书籍信息

        //初始化actionbar
        getSupportActionBar().setTitle(bundle.getString("title")+"详情");
        //左侧添加一个默认的返回图标
        Objects.requireNonNull(getSupportActionBar()).setDisplayHomeAsUpEnabled(true);
//设置返回键可用
        getSupportActionBar().setHomeButtonEnabled(true);

        Intent intent=getIntent();
        bundle=intent.getExtras();
        bookId=bundle.getString("bookid");

        initRating();
        initDouBanButton();
        initCommentButton();
        initDetails();

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


void initDetails(){
        //初始化封面。
    ImageView imageCover=findViewById(R.id.cover);

        String cover_url=bundle.getString("cover_url");

        Glide.with(this).load(cover_url)
                .placeholder(R.drawable.nobookcover)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.nobookcover).into(imageCover);



    //初始化标题.
    TextView title=findViewById(R.id.title);
//    String t=jb.getAsJsonObject("nameValuePairs").get("title").toString();
    if(bundle.getString("title").length()>=2){
        String titl=bundle.getString("title");
        title.setText(titl);
    }


    //概要 lable
    TextView abstr=findViewById(R.id.abstracts);
    if(bundle.getString("abstract").length()>=2){
        String abs=bundle.getString("abstract");
        String lable=bundle.getString("lable");
        abstr.setText("摘要:  "+abs+"\n"+"标签:  "+lable);
    }



    //内容简介

    TextView contentdetail=findViewById(R.id.contentdetail);
    if(bundle.getString("book_intro").length()>=2 ){
        String book_intro=bundle.getString("book_intro");
        contentdetail.setText(book_intro);
    }

    //作者详情
    TextView authordetail=findViewById(R.id.authordetail);
    if(bundle.getString("author_intro").length()>=2){
        String author_intro=bundle.getString("author_intro");
        authordetail.setText(author_intro);
    }

}

void initDouBanButton(){
    Button button=findViewById(R.id.doubanbutton);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:在这里导入网址
//            String url=jb.getAsJsonObject("nameValuePairs").get("url").toString() ;
//            Uri uri= Uri.parse(url.substring(1,url.length()-1));
            Uri uri=uri=Uri.parse("https://www.douban.com/");
            if(bundle.getString("url").length()>2){
                uri=Uri.parse(bundle.getString("url"));
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
//            Bundle bundle=new Bundle();
//            bundle.putSerializable("book",bookBundle);
//            intent.putExtras(bundle);
            startActivity(intent);

        }
    });
}


void initRating(){
//TODO:要数据库获得数据append到textview
    //float rate=8.8f;
//    String star=jb.getAsJsonObject("nameValuePairs").getAsJsonObject("rating").getAsJsonObject("nameValuePairs").get("star_count").toString() ;
    String star=bundle.getString("stars");
    RatingBar ratingBar=findViewById(R.id.ratingbar);
    TextView textView=findViewById(R.id.rating_text);
    ratingBar.setRating(Float.parseFloat(star));
    textView.append(star);

    }


}