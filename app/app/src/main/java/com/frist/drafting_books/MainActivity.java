package com.frist.drafting_books;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.DB.LoginCallback;
import com.frist.drafting_books.ui.home.HomeFragment;
import com.frist.drafting_books.ui.login.LoginActivity;
import com.frist.drafting_books.ui.record.RecordFragment;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.NavDestination;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;


public class MainActivity extends AppCompatActivity {
    private MainActivity self;
    @SuppressLint("StaticFieldLeak")
    private static Context main_ctx;

    public synchronized static Context getMain_ctx() { //不能设置为静态会内存泄漏/应不应该设定为synchronized
        return main_ctx;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        main_ctx = getApplication();
//        LeanConfig.initAVOSCloud(true); //改到DB的实例方法里（懒加载）
//        先进入login
//        Intent logina = new Intent(this, LoginActivity.class);
        Bundle login = getIntent().getExtras();
        LeancloudDB dbt = LeancloudDB.getInstance();
        //折磨 todo 看起来笨笨的操作
        self = this;
        dbt.Login(login.get("username").toString(), login.get("password").toString(), new LoginCallback() {
            @Override
            public void Success() {
                //这里还需要传act但将就用静态方法也行//不行！/再写个回调方法 !回调函数只能底到高，这里不可能传上来
                setContentView(R.layout.activity_main);
                BottomNavigationView navView = findViewById(R.id.nav_view);
                // Passing each menu ID as a set of Ids because each
                // menu should be considered as top level destinations.
                AppBarConfiguration appBarConfiguration = new AppBarConfiguration.Builder(
                        R.id.navigation_home,R.id.navigation_record,R.id.navigation_community,R.id.navigation_message)
                        .build();
                NavController navController = Navigation.findNavController(self, R.id.nav_host_fragment);
                NavigationUI.setupActionBarWithNavController(self, navController, appBarConfiguration);
                NavigationUI.setupWithNavController(navView, navController);

                //隐藏actionbar
                getSupportActionBar().hide();
                navController.addOnDestinationChangedListener(new NavController.OnDestinationChangedListener() {
                    @Override
                    public void onDestinationChanged(@NonNull NavController controller, @NonNull NavDestination destination, @Nullable Bundle arguments) {
                        if(destination.getId() == R.id.navigation_message) {
                            System.out.println("is message");
                            navController.navigate(R.id.navigation_community);
                        } else {
                            System.out.println("no message");
                        }
                    }
                });
            }
            @Override
            public void Fail() {

            }
        });
//todo 这里直接进入login有个问题是返回就直接进入Main了//










//todo volley test

//        VolleyHelper vh = new VolleyHelper(this);
//        vh.stringGETTEST();
//        vh.jsonGETBOOK("9787101052039", new GetBookCallback() {
//            @Override
//            public void querySuccess(JSONObject bookJson) {
//          //ok
//            }
//            @Override
//            public void queryFail(Error e) {
//            }
//        });

//todo unit test

//        LeancloudDB dbt = LeancloudDB.getInstance();
//        dbt.Login("t","666666");

//        dbt.addUser("username1","password1");
//        dbt.addBook("19787101052039");
//        dbt.lentBook("606a82a27fa6c4403bc994a0");
        //ArrayList<Map<String,String>> res = dbt.showMyBooks();
////        dbt.addUser("username1","password1");
//        dbt.Login("username1","password1");
//        dbt.addBook("9787101052039",getApplication());
//        dbt.returnBook("606a82a27fa6c4403bc994a0");

//        ArrayList<Map<String,String>> res = dbt.showMyBooks();
        // return just 0, but database has 8 books
//        System.out.println("res大小 "+res.size()); //返回res大小为0这是因为多线程不同步，show返回得过早了//前面都是void函数故不需要与当前线程同步
//        for(int i=0;i<res.size();i++){
//            System.out.println(res.get(i));
//        }
//        System.out.println("pass"); login是新开了个线程//signup不会实例/login都能实例当前的User
    }




    /**
    *@description 扫描录入图书，从扫描界面返回的函数，获取ISBN
    *@author ZhipengLiu
    *@created at 2021/4/9
     **/
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        Log.d("onActivityResult", "扫描回调");
        //扫码结果
        IntentResult intentResult = IntentIntegrator.parseActivityResult(requestCode, resultCode, data);
        if (intentResult != null) {
            if (intentResult.getContents() == null) {
                Log.d("onActivityResult", "扫描回调没有内容");
            } else {
                String ISBN = intentResult.getContents();//返回值
                LeancloudDB dbt = LeancloudDB.getInstance();
                dbt.addBook(ISBN,getApplication());
                //不是在qt活动的释放函数切换而是在这里
//                switchContent();
//                replace_to_home(); 会出现控件布局打乱
//                this.onCreate(null);
//                放弃了，干脆写个record的返回页面///解决了如下，通过control能之间跳转到
                NavController navController = Navigation.findNavController(this, R.id.nav_host_fragment);
                navController.navigate(R.id.navigation_home);
                System.out.println("回调结束");
            }
        }
    }
    public void switchContent(Fragment from,Fragment to){
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
        if(!to.isAdded()){
            tran.hide(from).add(R.id.navigation_home,to).commit();
        }else {
            tran.hide(from).show(to).commit();
        }
    }
    public void replace_to_home(){
        //home是空的
//        Fragment current = getSupportFragmentManager().findFragmentById(R.id.navigation_record);
        FragmentTransaction tran = getSupportFragmentManager().beginTransaction();
//        assert current != null;
//        tran.replace(R.id.navigation_home,current);
        HomeFragment ret = new HomeFragment();
        tran.add(R.id.navigation_home,ret).commit();
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