package com.frist.drafting_books.utils;

import org.json.JSONObject;

public interface GetBookFromNetCallback {
    void querySuccess(JSONObject bookJson);
    void queryFail(Error e);
}
