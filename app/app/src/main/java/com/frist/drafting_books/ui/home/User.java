package com.frist.drafting_books.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.R;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;

import java.io.FileNotFoundException;
import java.security.PrivateKey;

import cn.leancloud.AVFile;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;


public class User extends AppCompatActivity {
    private PictureBean pictureBean;
    private ImageView head;
    private Button about,bug;
    private EditText editname,editpassword,editbookroom,editaddress;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        head=findViewById(R.id.head);
        editaddress = findViewById(R.id.editaddress);
        editbookroom = findViewById(R.id.editbookroom);
        editname = findViewById(R.id.editname);
        editpassword = findViewById(R.id.editpassword);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_esc);
            actionBar.setTitle("编辑个人信息");
        }
        about=findViewById(R.id.about);
        bug= findViewById(R.id.bug);
//一样也要初始化user的UI
        editname.setText(AVUser.getCurrentUser().getUsername());
        editpassword.setText(AVUser.getCurrentUser().getPassword());
        editbookroom.setText(AVUser.getCurrentUser().getUsername()+"'bookroom");
        editaddress.setText("Shapingba, Chongqing");
        Glide.with(this)
                .load(AVUser.getCurrentUser().get("imageLink"))
                .apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head);


        about.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                        Intent intent = new Intent(User.this,Imformation.class);
                        startActivity(intent);
            }
        });

        bug.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(User.this,Bug.class);
                startActivity(intent);
            }
        });
    }


    //选择头像的button的事件
    public void paizhao(View v){
        PictureSelector
                .create(User.this, PictureSelector.SELECT_REQUEST_CODE)
                .selectPicture(true);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        /*结果回调*/
        if (requestCode == PictureSelector.SELECT_REQUEST_CODE) {
            if (data != null) {
                pictureBean = data.getParcelableExtra(PictureSelector.PICTURE_RESULT);
//                if (pictureBean.isCut()) {
//                    head.setImageBitmap(BitmapFactory.decodeFile(pictureBean.getPath()));
////                    head.setImageURI(pictureBean.getUri());
//                } else {
//                    head.setImageURI(pictureBean.getUri());
//                }
                Glide.with(this)
                        .load(pictureBean.isCut() ? pictureBean.getPath() : pictureBean.getUri())
                        .apply(RequestOptions.bitmapTransform(new CircleCrop())).into(head);
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint({"ShowToast", "NonConstantResourceId"})
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_save:
//                鹏包加函数
                Log.d("test","进入save");
                if(!editpassword.getText().toString().equals("") && !editname.getText().toString().equals("") &&pictureBean!=null){
                    LeancloudDB db = LeancloudDB.getInstance();
                    Log.d("testpic", pictureBean.getPath().toString());//形如content://media/external/images/media/2075055
                    try {
                        AVFile file = AVFile.withAbsoluteLocalPath(AVUser.getCurrentUser().getUsername() +"'avatar.jpg", pictureBean.getPath());
                        file.saveInBackground().subscribe(new Observer<AVFile>() {
                            public void onSubscribe(Disposable disposable) {}
                            public void onNext(AVFile file) {
                                System.out.println("文件保存完成。objectId：" + file.getObjectId()+" URL:"+file.getUrl());
                                db.updateUser(editname.getText().toString(),editpassword.getText().toString(),file.getUrl());;
                            }
                            public void onError(Throwable throwable) {
                                // 保存失败，可能是文件无法被读取，或者上传过程中出现问题
                            }
                            public void onComplete() {}
                        });
                    } catch (FileNotFoundException e) {
                        Toast.makeText(getApplicationContext(),"文件上传失败(Please choose a picture)",Toast.LENGTH_LONG);
                    }
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}