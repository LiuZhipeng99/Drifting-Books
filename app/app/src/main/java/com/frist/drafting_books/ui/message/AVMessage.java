package com.frist.drafting_books.ui.message;

import java.util.HashMap;
import java.util.Map;

import cn.leancloud.AVStatus;

public class AVMessage extends AVStatus {
    public AVMessage(String name,String phone,String address,String choose,String start,String end,String msg) {
        Map<String,Object> detail = new HashMap<>();
//        detail.put("name",name)
//        super(detail);
    }
    //    // 默认的支持图文混合的 Status
//    AVStatus(String imageUrl, String message)
//    // 附带其他自定义属性的 Status
//    AVStatus(Map<String, Object> customData)
//
//    // 设置与获取自定义属性
//    void put(String key, Object value)
//    Object get(String key)
//    // 删除某个自定义属性
//    void remove(String key)
}
