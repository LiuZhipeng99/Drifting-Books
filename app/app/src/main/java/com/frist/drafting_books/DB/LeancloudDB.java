package com.frist.drafting_books.DB;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;
import android.widget.Toast;

import com.frist.drafting_books.utils.HttpHelper;
import com.google.gson.Gson;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.leancloud.AVObject;
import cn.leancloud.AVQuery;
import cn.leancloud.AVUser;
import cn.leancloud.json.JSON;
import cn.leancloud.json.JSONObject;
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
    public void addUser(String name,String password){ //定位服务在此处获取还是获取后传参？todo 之后考虑定位
        AVUser user = new AVUser(); //没有表名因为用的_User表
// 等同于 user.put("username", "Tom")
        user.setUsername(name);
        user.setPassword(password);
//        user.setEmail("tom@leancloud.example");
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
                System.out.println("注册成功。objectId：" + user.getObjectId());
            }
            public void onError(@NotNull Throwable throwable) {
                // 注册失败（通常是因为用户名已被使用）//todo 这里需要提示用户但需要传context/Toast.makeText()或dialog
                Log.d("User","用户注册失败（多半已使用）");
            }
            public void onComplete() {}
        });
        //todo 需要主线程开启定位服务
//        return ids.get(0); 不需要通过在onnext赋值，最后检验一样的。除了本地保存id，user类自带会话系统，可以不用保存
    }
    public void Login(String name,String password){
        AVUser.logIn(name, password).subscribe(new Observer<AVUser>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(AVUser user) {
                // 登录成功
                System.out.println("Login:"+AVUser.getCurrentUser().getObjectId());
            }
            public void onError(Throwable throwable) {
                // 登录失败（可能是密码错误）//todo 这里提示用户
            }
            public void onComplete() {}
        });
    }
    /**
    *@description
     * 录入图书方法，添加到book表
    *@author ZhipengLiu
     **/
    public void addBook(String isbn){ //用了全局的userid
        AVObject book = new AVObject(tableBooks);
        //用isbn获取书籍信息，向书表加行，
        book.put("isbn",isbn);
        book.put("is_lent",false);
        //这里获取json
//        JSONObject js = HttpHelper.asy_get(isbn);
        JSON js = new JSON();
        book.put("BookJson",js);
        ArrayList<String> comments = new ArrayList<>();
        comments.add("好耶!");
        book.put("comments",comments);
        AVUser currentUser = AVUser.getCurrentUser();
        if (currentUser != null) {
            book.put("userId",AVUser.getCurrentUser().getObjectId());
        } else {
            // 显示注册或登录页面
            Log.d("User","need sign");
            return;
        }
        book.saveInBackground().subscribe(new Observer<AVObject>() {
            @Override
            public void onSubscribe(@NonNull Disposable d) {
            }

            @Override
            public void onNext(@NonNull AVObject avObject) { //add之类的方法是给非数组类用也没啥发生。
                currentUser.addUnique("bookId_list",avObject.getObjectId()); //这里保证了arr不会有重复的同一本书//但book无法保证，故book可能有ISBN和uerid都一样的书
                currentUser.save(); //todo 这里其实不能保证因为书的id一定是唯一的，
            }

            @Override
            public void onError(@NonNull Throwable e) {
                System.out.println("book上传失败！");
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

    public void updateUser(String name,String password,String imageLink){ //todo 同时更新location但是是自动获取而不能手动修改/手机号邮箱目前不加入都是默认
        AVUser currentUser = AVUser.getCurrentUser(); //todo 可以当全局的userid变量的
        if (currentUser != null) {
            currentUser.setUsername(name);
            currentUser.setPassword(password);
            currentUser.put("imageLink",imageLink);
            currentUser.save();
        } else {
            // 显示注册或登录页面
            Log.d("User","need sign");
        }
    }
    public void lentBook(String bookId){
        AVObject toChange = AVObject.createWithoutData(tableBooks, bookId);
        toChange.put("is_lent", true);
        toChange.save();
    }
    public void returnBook(String bookId){
        AVObject toChange = AVObject.createWithoutData(tableBooks, bookId);
        toChange.put("is_lent", false);
        toChange.save();
    }
    public List<AVObject> showBooks(){//因为返回arraylist的元素map需要指定类型不如python的好使
        final List<AVObject>[] res = new List[]{new ArrayList<>()};
        AVQuery<AVObject> query = new AVQuery<>(tableBooks);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(List<AVObject> arrs) {
                // students 是包含满足条件的 Student 对象的数组
                res[0] = arrs;
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {}
        });
        return res[0]; //为什么需要改成数组没搞明白
    }
    /**
    *@description
     * 展示当前用户的书籍/查询目前只考虑单线程
    *@author ZhipengLiu
    *@created at 2021/4/5
     **/
    public ArrayList<Map<String,String>> showMyBooks(){
        //两种查询方法，一种是查询自身的arraylist，另一种是条件查询books,前者返回一列，后者返回一个包含所有满足条件的list//应该考虑返回给listview用的数组：每个book的map包含名字，
        AVUser currentUser = AVUser.getCurrentUser();
        ArrayList<String> booksId_list;
        if (currentUser == null) {
            Log.d("User","cur is null");
            //todo 登录
        }
        String curId = AVUser.getCurrentUser().getObjectId();
//        booksId_list = (ArrayList<String>) currentUser.get("booksId_list");
        //有了idlist，查询book表 /感觉上面这句有bug还是用条件查询吧
        ArrayList<Map<String,String>> res = new ArrayList<>();
        AVQuery<AVObject> query = new AVQuery<>(tableBooks);
        query.whereEqualTo("userId",curId);
        query.findInBackground().subscribe(new Observer<List<AVObject>>() {
            public void onSubscribe(Disposable disposable) {}
            public void onNext(List<AVObject> arrs) {
                // students 是包含满足条件的 Student 对象的数组
                for(int i=0;i<arrs.size();i++){
                    AVObject book = arrs.get(i);
                    Map<String,String> mp = new HashMap<>();
                    mp.put("title",book.getString("title"));
                    mp.put("cover",book.getString("cover"));
                    mp.put("id",book.getObjectId());
                    res.add(mp);
                }
                System.out.println("查询结果的数组大小："+arrs.size());
            }
            public void onError(Throwable throwable) {}
            public void onComplete() {
                System.out.println("这个complete会不会比res返回更先");
            }
        });
        return res;
    }
}
