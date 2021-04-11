package com.frist.drafting_books.ui.community;

import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.frist.drafting_books.R;
import com.smailnet.emailkit.Draft;
import com.smailnet.emailkit.EmailKit;

import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.AVUser;

public class Form extends AppCompatActivity {
    private EditText time_start,time_end;
    private TextView owner_id;
    private ImageView book_img;
    private AlertDialog.Builder builder;
    String email_text = "Default Message";

    private static final String TAG = "Form";
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_form);
        //控件初始化
        time_start=findViewById(R.id.time_start);
        time_end=findViewById(R.id.time_end);
        book_img = findViewById(R.id.book_img);
        owner_id = findViewById(R.id.owner_id);
        owner_id.setText(getIntent().getStringExtra("book_owner"));
        Glide.with(this).load(getIntent().getStringExtra("bookcover"))
                .placeholder(R.drawable.nobookcover)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.nobookcover).into(book_img);

        ActionBar actionBar = getSupportActionBar();

        if(actionBar != null){
            actionBar.setHomeButtonEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setHomeAsUpIndicator(R.drawable.ic_esc);
            actionBar.setTitle("Borrowing information");
        }
        time_start.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg1();
                    return true;
                }
                return false;
            }
        });
        time_end.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    showDatePickDlg2();
                    return true;
                }
                return false;
            }
        });

    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        getMenuInflater().inflate(R.menu.form,menu);
        return super.onCreateOptionsMenu(menu);
    }
    @SuppressLint("NonConstantResourceId")
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                this.finish();
                return true;
            case R.id.action_form:
//                返回，toast发送请求，传表数据库
//                Intent intent=new Intent(Form.this,Communicate.class);
//                startActivity(intent);
                Map<String,String> email = new HashMap<>();
                Bundle data = getIntent().getExtras();
                email.put("to", (String) data.get("to"));
                Log.d(TAG, "onOptionsItemSelected: "+((TextView)findViewById(R.id.name_edit)).getText().equals(""));
                Log.d(TAG, "onOptionsItemSelected: "+((TextView)findViewById(R.id.contact_edit)).getText().equals(""));
                if((!((TextView)findViewById(R.id.name_edit)).getText().toString().equals(""))&&(!((TextView)findViewById(R.id.contact_edit)).getText().toString().equals(""))){
                    email_text = "Request user:"+((TextView)findViewById(R.id.name_edit)).getText().toString()+
                            "\nPhone:"+((TextView)findViewById(R.id.contact_edit)).getText().toString()+
                            "\nAddress:"+((TextView)findViewById(R.id.address_edit)).getText().toString()+
                            "\nLeave message:"+((TextView)findViewById(R.id.leavemessage_edit)).getText().toString();
                    email.put("text",email_text);
                    sendEmail_kit(email);
                    builder=new AlertDialog.Builder(this).setTitle("Email reminder").setMessage("The mail has been sent, pay attention to the mailbox.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //ToDo: 你想做的事情
//                        Toast.makeText(Form.this, "确定按钮", Toast.LENGTH_LONG).show();
                            Form.this.finish();
                        }
                    });
                    builder.create().show();
                }else {
                    builder=new AlertDialog.Builder(this).setTitle("Content reminder").setMessage("The name and contact information cannot be empty.").setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            //ToDo: 你想做的事情
//                        Toast.makeText(Form.this, "确定按钮", Toast.LENGTH_LONG).show();

                        }
                    });
                    builder.create().show();
                }

                return true;
        }
        return super.onOptionsItemSelected(item);
    }
    void sendEmail_kit(Map<String ,String> email){
        //初始化框架
        EmailKit.initialize(this);

//配置发件人邮件服务器参数
        EmailKit.Config config = new EmailKit.Config()
                .setMailType(EmailKit.MailType.FOXMAIL)     //选择邮箱类型，快速配置服务器参数
                .setAccount("1438714538@qq.com")             //发件人邮箱
                .setPassword("nqafxexziwhzighb");             //密码或授权码
//设置一封草稿邮件
        Draft draft = new Draft()
                .setNickname("DraftingBooks："+AVUser.getCurrentUser().getUsername())                      //发件人昵称
                .setTo(email.get("to"))                        //收件人邮箱
                .setSubject("DraftingBooks: lend request")             //邮件主题
                .setText(email.get("text"));                 //邮件正文

//使用SMTP服务发送邮件
        EmailKit.useSMTPService(config)
                .send(draft, new EmailKit.GetSendCallback() {
                    @Override
                    public void onSuccess() {
                        Log.i("Email", "发送成功！");
                    }

                    @Override
                    public void onFailure(String errMsg) {
                        Log.i("Email", "发送失败，错误：" + errMsg);
                    }
                });
    }
    protected void sendEmail(String to) {
        Log.i("Send E-mail", "");
        String[] TO = {to};
        String[] CC = {""};
        Intent emailIntent = new Intent(Intent.ACTION_SEND);

        emailIntent.setData(Uri.parse("mailto:"));
        emailIntent.setType("text/plain");

        emailIntent.putExtra(Intent.EXTRA_EMAIL, TO);
        emailIntent.putExtra(Intent.EXTRA_CC, CC);
        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "您的标题");
        emailIntent.putExtra(Intent.EXTRA_TEXT, "这里是邮件消息");

        try {
            startActivity(Intent.createChooser(emailIntent, "Send mail..."));
            finish();
            Log.i("邮件发送完成...", "");
        } catch (android.content.ActivityNotFoundException ex) {
            Toast.makeText(getApplicationContext(), "You have no E-mail Client", Toast.LENGTH_SHORT).show();
        }
    }


    protected void showDatePickDlg1() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Form.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Form.this.time_start.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    protected void showDatePickDlg2() {
        Calendar calendar = Calendar.getInstance();
        DatePickerDialog datePickerDialog = new DatePickerDialog(Form.this, new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Form.this.time_end.setText(year + "-" + monthOfYear + "-" + dayOfMonth);
            }
        }, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH));
        datePickerDialog.show();
    }
    public Bitmap getNewBitmap(Bitmap bitmap, int newWidth ,int newHeight){
        // 获得图片的宽高.
        int width = bitmap.getWidth();
        int height = bitmap.getHeight();
        // 计算缩放比例.
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        // 取得想要缩放的matrix参数.
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        // 得到新的图片.
        Bitmap newBitmap = Bitmap.createBitmap(bitmap, 0, 0, width, height, matrix, true);
        return newBitmap;
    }


}

