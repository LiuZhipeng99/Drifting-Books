package com.frist.drafting_books.DB;

import android.content.Context;
import android.util.Log;

import androidx.annotation.Nullable;

import com.frist.drafting_books.utils.GetBookFromNetCallback;
import com.frist.drafting_books.utils.VolleyHelper;

import org.jetbrains.annotations.NotNull;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.Random;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import cn.leancloud.types.AVGeoPoint;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;

//--||userID|password|nickname|imageLink|location|booksId_list||--
//--||booksID|isbn|bookJson|is_lent|commentsList||--?userId?
//funs:
//todo 先试试不用到bean那些类
public class LeancloudDB {
    private static LeancloudDB db; //单例模式，静态的自己对象，实现异步的静态的get实例方法（懒汉模式）

//    private final String tableUser = "t_users";
    private final String tableBooks = "t_books";
    private final String defaultImageLink = "http://qiniu.lifelover.top/touxiang20210301170002.png";
    private LeancloudDB(){
        LeanConfig.initAVOSCloud(true);
    }
    public LeancloudDB(Context ctx){ //专门写给login活动的
        AVOSCloud.initialize(ctx, LeanConfig.APP_ID,LeanConfig.APP_KEY,LeanConfig.API_URL);
    }
    public synchronized static LeancloudDB getInstance(){
        if(db == null){
            db = new LeancloudDB();
        }
        return db;
    }
    /**
    *@description 注册方法
     * 现在使用User对象不用考虑维护对话了即返回id，而注册需要邮箱/手机/用户名不同//目前只考虑用户名注册故无邮箱等参数
     **/
    public void addUser(String name, String password, SignUpCallback cal){ //定位服务在此处获取还是获取后传参？todo 之后考虑定位
        AVUser user = new AVUser(); //没有表名因为用的_User表
// 等同于 user.put("username", "Tom")
        user.setUsername(name);
        user.setPassword(password);
//        user.setEmail("tom@qq.example"); 手机邮箱都不能一样
        Random genEmail = new Random();
        user.setEmail(genEmail.nextInt(100000000)+"@example.draftingBooks");
//        user.setMobilePhoneNumber("+8618200008888");
// 设置其他属性的方法跟 AVObject 一样
        user.put("imageLink",defaultImageLink);//在更新用户信息里改
        ArrayList<String> bookIds = new ArrayList<>();
        user.put("booksId_list",bookIds);
        AVGeoPoint point = new AVGeoPoint(39.9, 116.4);
        user.put("location",point);
        user.signUpInBackground().subscribe(new Observer<AVUser>() { //方法也不同与object
            public void onSubscribe(@NotNull Disposable disposable) {}
            public void onNext(@NotNull AVUser user) {
                cal.Success();
                Log.d("DB","注册成功。objectId：" + user.getObjectId());
            }
            public void onError(@NotNull Throwable throwable) {
                // 注册失败（通常是因为用户名已被使用）//todo 这里需要提示用户但需要传context/Toast.makeText()或dialog
                cal.Fail();
                Log.e("User",throwable.toString());
            }
            public void onComplete() {}
        });
        //todo 需要主线程开启定位服务
//        return ids.get(0); 不需要通过在onnext赋值，最后检验一样的。除了本地保存id，user类自带会话系统，可以不用保存
    }
    public void Login(String name,String password,LoginCallback callback){
        AVUser.logIn(name, password).subscribe(new Observer<AVUser>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(AVUser user) {
                // 登录成功
                callback.Success();
                Log.d("Login:",AVUser.getCurrentUser().getObjectId());
            }
            public void onError(Throwable throwable) {
                // 登录失败（可能是密码错误）
                callback.Fail();
            }
            public void onComplete() {}
        });
    }
    /**
    *@description
     * 录入图书方法，添加到book表，ctx是用来调用网络方法的
    *@author ZhipengLiu
     **/
    public void addBook(String isbn, Context ctx){ //用了全局的userid
        AVObject book = new AVObject(tableBooks);
        //用isbn获取书籍信息，向书表加行，
        book.put("isbn",isbn);
        book.put("is_lent",false);
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            book.put("userId",AVUser.getCurrentUser().getObjectId());
            book.put("userName",AVUser.getCurrentUser().getUsername());
            book.put("email",AVUser.getCurrentUser().getEmail());
        } else {
            // 显示注册或登录页面
            Log.d("User","need sign");
            return;
        }
        VolleyHelper vh = new VolleyHelper(ctx);
        vh.jsonGETBOOK(isbn, new GetBookFromNetCallback() {
            @Override
            public void querySuccess(JSONObject bookJson) {
                try {
//                    bookJson = bookJson.getJSONObject("nameValuePairs");
                    bookJson.remove("original_texts");
                    bookJson.remove("catalog");
                    bookJson.remove("create_time");
                    Log.d("DB",bookJson.get("comments").toString());
                    JSONArray cms = bookJson.getJSONArray("comments");
//                    ArrayList<JSONObject> lists = new ArrayList<>();
//                    for(int i=0;i<cms.length();i++){
//                        lists.add(cms.getJSONObject(i));
//                    }
                    book.put("comments",bookJson.get("comments")); //白费功夫！！
//                    book.put("commentstest",bookJson.getJSONArray("comments")); 和上面get效果一样
//                    bookJson.remove("comments");
                    book.put("book_json",bookJson);
                    submitBook(currentUser,book); //如果book的提交在这个函数外那么json不能提交上去。//这里接口get已经达到顶部，不需要继续call到调用add的函数
//                    TODO 这里把submit放入查询成功，避免有空的列插入
                } catch (JSONException e) {
                    Log.e("DB",e.toString());
                }
            }
            @Override
            public void queryFail(Error e) {
            }
        });
    }
    /**
    *@description addbook的私有函数，实现回调函数调用
    *@author ZhipengLiu
    *@created at 2021/4/9
     **/
    private void submitBook(AVUser currentUser, @NotNull AVObject book) {
        book.saveInBackground().subscribe(new Observer<AVObject>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }
            @Override
            public void onNext(@NonNull AVObject avObject) { //add之类的方法是给非数组类用也没啥发生。
                currentUser.addUnique("booksId_list",avObject.getObjectId()); //这里保证了arr不会有重复的同一本书//但book无法保证，故book可能有ISBN和uerid都一样的书
                currentUser.saveInBackground(); //todo 这里其实不能保证因为书的id一定是唯一的，
            }
            @Override
            public void onError(@NonNull Throwable e) {
               Log.e("DB",e.toString());
            }
            @Override
            public void onComplete() {
            }
        });//没什么要针对提交结果处理的事，故先不加handel
        //todo 添加后更新user表的arraylist//使用文档中的更新数组
//        ArrayList<String> books =  currentUser.get("booksId_list");
//        currentUser.add("booksId_list",book.getObjectId());
        //这里的id不能获取应该需要在处理函数里获取，System.out.println("bookid "+book.getObjectId());
//        currentUser.save();
    }

    public void updateUser(String name,String email,String imageLink){ //todo 同时更新location但是是自动获取而不能手动修改/手机号邮箱目前不加入都是默认
        AVUser currentUser = AVUser.getCurrentUser(); //todo 可以当全局的userid变量的
        if (currentUser != null) {
            currentUser.setUsername(name);
            currentUser.put("imageLink",imageLink);
            Log.d("DB",imageLink);
            currentUser.setEmail(email);
            currentUser.saveInBackground();
        } else {
            // 显示注册或登录页面
            Log.d("DB","need sign");
        }
    }
    public void updatePassword(String password) {
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            currentUser.setPassword(password);
            currentUser.saveInBackground();
        } else {
            // 显示注册或登录页面
            Log.d("DB", "need sign");
        }
    }
    public void updateUser(String name,String email){ //todo 同时更新location但是是自动获取而不能手动修改/手机号邮箱目前不加入都是默认
        AVUser currentUser = AVUser.getCurrentUser(); //todo 可以当全局的userid变量的
        if (currentUser != null) {
            currentUser.setUsername(name);
            currentUser.setEmail(email);
            currentUser.saveInBackground();
        } else {
            // 显示注册或登录页面
            Log.d("DB","need sign");
        }
    }
    public void lentBook(String bookId){
        AVObject toChange = AVObject.createWithoutData(tableBooks, bookId);
        toChange.put("is_lent", true);
//        toChange.save();
        toChange.saveInBackground();
    }
    public void returnBook(String bookId){
        AVObject toChange = AVObject.createWithoutData(tableBooks, bookId);
        toChange.put("is_lent", false);
//        toChange.save();
        toChange.saveInBackground();
    }
    public void showBookDetail(String bookId,GetBookFromLean callback){
        AVQuery<AVObject> query = new AVQuery<>(tableBooks);
        query.getInBackground(bookId).subscribe(new Observer<AVObject>() {
            public void onSubscribe(Disposable disposable) {}
            @Override
            public void onNext(@NonNull AVObject avObject) {
                callback.queryOneSuccess(avObject);
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {
                System.out.println("这个complete不会比res返回更先");
            }
        });
    }
    public void showBooks(GetBookFromLean callback){//因为返回arraylist的元素map需要指定类型不如python的好使
        AVQuery<AVObject> query = new AVQuery<>(tableBooks);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(List<AVObject> arrs) {
                // students 是包含满足条件的 Student 对象的数组
                List<AVObject> borr = new ArrayList<>();
                callback.querySuccess(arrs,borr);
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {}
        });
//        return res[0]; //为什么需要改成数组没搞明白
    }
    /**
    *@description
     * 展示当前用户的书籍/查询目前只考虑单线程
    *@author ZhipengLiu
    *@created at 2021/4/5
     **/
    public void showMyBooks(GetBookFromLean callback){
        //两种查询方法，一种是查询自身的arraylist，另一种是条件查询books,前者返回一列，后者返回一个包含所有满足条件的list//应该考虑返回给listview用的数组：每个book的map包含名字，
        AVUser currentUser = AVUser.getCurrentUser();
        ArrayList<String> booksId_list;
        if (currentUser == null) {
            Log.d("DB","curUser is null");
            //todo 登录
//            Intent logina = new Intent(MainActivity.getMain_ctx(), LoginActivity.class);
//            startActivity(logina);
//            setContentView(R.layout.activity_main);
        }
        String curId = AVUser.getCurrentUser().getObjectId();
//        booksId_list = (ArrayList<String>) currentUser.get("booksId_list");
        //有了idlist，查询book表 /感觉上面这句有bug还是用条件查询吧
//        ArrayList<Map<String,String>> res = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>(tableBooks);
        query.whereEqualTo("userId",curId);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(List<AVObject> arrs) {
                callback.querySuccess(arrs);
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {
                System.out.println("这个complete不会比res返回更先");
            }
        });
    }
    /**
    *@description 加评论暂时不管
    *@author ZhipengLiu
    *@created at 2021/4/10
     **/
    public void addComment(String bookId){

    }
}
