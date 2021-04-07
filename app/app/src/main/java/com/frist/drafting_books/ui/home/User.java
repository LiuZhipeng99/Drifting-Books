package com.frist.drafting_books.ui.home;

import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.frist.drafting_books.R;
import com.wildma.pictureselector.PictureBean;
import com.wildma.pictureselector.PictureSelector;


public class User extends AppCompatActivity {
    private PictureBean pictureBean;
    private ImageView head;
    private Button about,bug;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.user);
        head=findViewById(R.id.head);
        ActionBar actionBar = getSupportActionBar();
        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_esc);
            actionBar.setTitle("编辑个人信息");
        }
        about=findViewById(R.id.about);
        bug= findViewById(R.id.bug);

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
                if (pictureBean.isCut()) {
                    head.setImageBitmap(BitmapFactory.decodeFile(pictureBean.getPath()));
                } else {
                    head.setImageURI(pictureBean.getUri());
                }
            }
        }
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.user,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_save:
//                鹏包加函数
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}