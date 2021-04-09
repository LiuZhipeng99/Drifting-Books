package com.frist.drafting_books.DB;

import org.json.JSONObject;

import java.util.List;

import cn.leancloud.AVObject;

public interface GetBookFromLean {
    void querySuccess(List<AVObject> books);
    void queryFail(Error e);
}
