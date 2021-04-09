package com.frist.drafting_books.DB;

import com.frist.drafting_books.MainActivity;

import cn.leancloud.AVOSCloud;

public class LeanConfig {
    public static String API_URL = "https://imleancloud.lifelover.top";
    public static String APP_ID = "GXBxsr19fKHJ9rSP9e3LdPAy-gzGzoHsz";
    public static String APP_KEY = "s94d7Tn2DpB7D2uuzprBqb9y";
    public static void initAVOSCloud(boolean status){ //因为init必须有ctx否则占主线程，如果get方法不好用就把此方法放到main文件中。
        if(status){
            AVOSCloud.initialize(MainActivity.getMain_ctx(),LeanConfig.APP_ID,LeanConfig.APP_KEY,LeanConfig.API_URL);
        }
    }
}
