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

import java.util.Objects;

public class bookDetails extends AppCompatActivity {
    private String bookId;
    private BundleBoat bookBundle;//从上面传下来到
    private JsonObject jb;//json解析出来的东西

    private static final String TAG = "bookDetails";

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_book_details);
        Intent intent=getIntent();
        Log.d(TAG, "onCreate: "+intent.getSerializableExtra("book"));
//        bookBundle = (BundleBoat)intent.getSerializableExtra("book");
        Bundle bundle=intent.getExtras();
//        Log.d(TAG, "onCreate: "+bundle);
        bookBundle=(BundleBoat)bundle.getSerializable("book");
//        Log.d(TAG, "onCreate: "+bundle.getSerializable("book"));
      //  Log.d(TAG, "onCreate: "+bookBundle);
        //解析书籍信息
        Gson gs = new Gson();
//        jb =gs.toJsonTree(bookBundle.get("book_json")).getAsJsonObject();
        Log.d("tile", (String) bundle.get("title"));


        //初始化actionbar
        getSupportActionBar().setTitle("");
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
void initDetails(){
        //初始化封面。
    ImageView imageCover=findViewById(R.id.cover);

    String cover_url=jb.getAsJsonObject("nameValuePairs").get("cover_url").toString() ;
    cover_url=cover_url.substring(1,cover_url.length()-1);
    Glide.with(this).load(cover_url)
            .placeholder(R.drawable.nobookcover)
            .diskCacheStrategy(DiskCacheStrategy.NONE)
            .error(R.drawable.nobookcover).into(imageCover);

    //初始化标题.
    TextView title=findViewById(R.id.title);
    String t=jb.getAsJsonObject("nameValuePairs").get("title").toString();
    title.setText(t.substring(1,title.length()-1));

    //概要
    TextView abstr=findViewById(R.id.abstracts);
    String abs=jb.getAsJsonObject("nameValuePairs").get("abstract").toString();
    Class temp=jb.getAsJsonObject("nameValuePairs").getAsJsonObject("labels").get("values").getClass();
    Log.d(TAG, "initDetails: "+temp);
    abstr.setText(abs.substring(1,title.length()-1));
}
void initDouBanButton(){
    Button button=findViewById(R.id.doubanbutton);

    button.setOnClickListener(new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            //TODO:在这里导入网址
            String url=jb.getAsJsonObject("nameValuePairs").get("url").toString() ;
            Uri uri= Uri.parse(url.substring(1,url.length()-1));
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
            bundle.putSerializable("book",bookBundle);
            intent.putExtras(bundle);
            startActivity(intent);

        }
    });
}


void initRating(){
//TODO:要数据库获得数据append到textview
    //float rate=8.8f;
    String star=jb.getAsJsonObject("nameValuePairs").getAsJsonObject("rating").getAsJsonObject("nameValuePairs").get("star_count").toString() ;
    RatingBar ratingBar=findViewById(R.id.ratingbar);
    TextView textView=findViewById(R.id.rating_text);
    ratingBar.setRating(Float.parseFloat(star.substring(1,star.length()-1)));
    textView.append(star.substring(1,star.length()-1));

    }

}