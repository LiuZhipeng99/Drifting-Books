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

import com.amap.api.location.AMapLocationClient;
import com.amap.api.location.AMapLocationListener;
import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.utils.HttpHelper;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVUser;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        BottomNavigationView navView = findViewById(R.id.nav_view);
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                R.id.navigation_home,R.id.navigation_record)
                .build();
        NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
        NavigationUI.setupActionBarWithNavController(this, navController, appBarConfiguration);
        NavigationUI.setupWithNavController(navView, navController);


        //试验leancloud//init应该在进入的第一个函数函数db初始化时候？？？
        AVOSCloud.initialize(this, "GXBxsr19fKHJ9rSP9e3LdPAy-gzGzoHsz", "s94d7Tn2DpB7D2uuzprBqb9y", "https://imleancloud.lifelover.top");
// 构建对象
        AVObject todo = new AVObject("Todo");

// 为属性赋值
        todo.put("title",   "工程师周会");
        todo.put("content", "周二两点，全体成员");

// 将对象保存到云端
        todo.saveInBackground().subscribe();


//todo unit test
        LeancloudDB dbt = LeancloudDB.getInstance();
//        dbt.addUser("username1","password1");
        dbt.Login("username1","password1");
//        dbt.addBook("19787101052039");
//        dbt.lentBook("606a82a27fa6c4403bc994a0");
        ArrayList<Map<String,String>> res = dbt.showMyBooks();
        System.out.println("res大小 "+res.size()); //返回res大小为0这是因为多线程不同步，show返回得过早了//前面都是void函数故不需要与当前线程同步
        for(int i=0;i<res.size();i++){
            System.out.println(res.get(i));
        }
//        System.out.println("pass"); login是新开了个线程//signup不会实例/login都能实例当前的User
    }





    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        System.out.println("扫码回调" );
        //扫码结果
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                //扫码失败
                System.out.println("扫码失败" );
            } else {
                String ISBN = intentResult.getContents();//返回值
//                String out = HttpHelper.sendGet("http://www.baidu.com",new HashMap<String, Object>(),"utf8");
//                String out = HttpHelper.asy_get(ISBN);
//                System.out.println("test out");
//                System.out.println(out);
            }
        }
    }

//    //声明AMapLocationClient类对象
//    public AMapLocationClient mLocationClient = null;
//    //声明定位回调监听器
//    public AMapLocationListener mLocationListener = new AMapLocationListener();
////初始化定位
//    mLocationClient = new AMapLocationClient(getApplicationContext());
////设置定位回调监听
//    mLocationClient.setLocationListener(mLocationListener);

}