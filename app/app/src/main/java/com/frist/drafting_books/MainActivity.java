package com.frist.drafting_books;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.frist.drafting_books.ui.record.QrCodeActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.google.zxing.qrcode.QRCodeWriter;

import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

import java.util.HashMap;
import java.util.Map;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    private ImageView imageView;
    private TextView textView;
    private EditText editText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.test_camera);
        imageView = this.findViewById(R.id.imageView_zxing);
        textView = this.findViewById(R.id.textview_zxing);
        editText = this.findViewById(R.id.edittext_zxing);

        findViewById(R.id.button_zxing).setOnClickListener(this);
        findViewById(R.id.button_start).setOnClickListener(this);
        findViewById(R.id.button_native).setOnClickListener(this);
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_zxing:
                String count = editText.getText().toString().trim();
                if (TextUtils.isEmpty(count)) {
                    Toast.makeText(MainActivity.this, "请输入内容", Toast.LENGTH_LONG).show();
                    return;
                }
                //生成二维码显示在imageView上
                imageView.setImageBitmap(generateBitmap(count, 600, 600));
                break;
            case R.id.button_start:
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                        //.setPrompt("请对准二维码")// 设置提示语
                        .setCameraId(0)// 选择摄像头,可使用前置或者后置
                        .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                        .initiateScan();// 初始化扫码
                break;
            case R.id.button_native:
                new IntentIntegrator(this)
                        .setDesiredBarcodeFormats(IntentIntegrator.ALL_CODE_TYPES)// 扫码的类型,可选：一维码，二维码，一/二维码
                        //.setPrompt("请对准二维码")// 设置提示语
                        .setCameraId(0)// 选择摄像头,可使用前置或者后置
                        .setBeepEnabled(true)// 是否开启声音,扫完码之后会"哔"的一声
                        .setCaptureActivity(QrCodeActivity.class)//自定义扫码界面
                        .initiateScan();// 初始化扫码
                break;
        }
    }

    /**
     * 生成固定大小的二维码(不需网络权限)
     *
     * @param content 需要生成的内容
     * @param width   二维码宽度
     * @param height  二维码高度
     * @return
     */
    private Bitmap generateBitmap(String content, int width, int height) {
        QRCodeWriter qrCodeWriter = new QRCodeWriter();
        Map<EncodeHintType, String> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        try {
            BitMatrix encode = qrCodeWriter.encode(content, BarcodeFormat.QR_CODE, width, height, hints);
            int[] pixels = new int[width * height];
            for (int i = 0; i < height; i++) {
                for (int j = 0; j < width; j++) {
                    if (encode.get(j, i)) {
                        pixels[i * width + j] = 0x00000000;
                    } else {
                        pixels[i * width + j] = 0xffffffff;
                    }
                }
            }
            return Bitmap.createBitmap(pixels, 0, width, width, height, Bitmap.Config.RGB_565);
        } catch (WriterException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫码结果
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //扫码失败
            } else {
                String result = intentResult.getContents();//返回值
                textView.setText("扫码结果：" + result);
            }
        }
    }
}


//public class MainActivity extends AppCompatActivity {
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//        BottomNavigationView navView = findViewById(R.id.nav_view);
//        // Passing each menu ID as a set of Ids because each
//        // menu should be considered as top level destinations.
//        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
//                R.id.navigation_home)
//                .build();
//        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
//        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
//        NavigationUI.setupWithNavController(navView, navController);
//
//
//        //试验leancloud
//        AVOSCloud.initialize(this, "GXBxsr19fKHJ9rSP9e3LdPAy-gzGzoHsz", "s94d7Tn2DpB7D2uuzprBqb9y", "https://imleancloud.lifelover.top");
//// 构建对象
//        AVObject todo = new AVObject("Todo");
//
//// 为属性赋值
//        todo.put("title",   "工程师周会");
//        todo.put("content", "周二两点，全体成员");
//
//// 将对象保存到云端
//        todo.saveInBackground().subscribe(new Observer<AVObject>() {
//            public void onSubscribe(@NotNull Disposable disposable) {}
//            public void onNext(@NotNull AVObject todo) {
//                // 成功保存之后，执行其他逻辑
//                System.out.println("保存成功。objectId：" + todo.getObjectId());
//
//            }
//            public void onError(@NotNull Throwable throwable) {
//                // 异常处理
//
//            }
//            public void onComplete() {}
//        });
//    }
//
//}