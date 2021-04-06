package com.frist.drafting_books.DB;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.Map;

public class Book {
    private String id;
    private String title;
    private String abstracts; //因为api有字段[美] 黄仁宇 / 中华书局 / 2006-8 / 36.00元。且不需要条件查询故当作一个字符串。
    private String publisher;
    private boolean is_lent;
    private String intro;
    private String coverUrl;
    private String url; //豆瓣的url

    public Book(String id, String title, String abstracts, String publisher, boolean is_lent, String intro, String coverUrl, String url, ArrayList<Map<String, String>> comments) {
        this.id = id;
        this.title = title;
        this.abstracts = abstracts;
        this.publisher = publisher;
        this.is_lent = is_lent;
        this.intro = intro;
        this.coverUrl = coverUrl;
        this.url = url;
        this.comments = comments;
    }

    public String getId() {
        return id;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCoverUrl() {
        return coverUrl;
    }

    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }

    public String getIntro() {
        return intro;
    }

    public void setIntro(String intro) {
        this.intro = intro;
    }

    public void setId(String id) {
        this.id = id;
    }

    public ArrayList<Map<String, String>> getComments() {
        return comments;
    }

    public void setComments(ArrayList<Map<String, String>> comments) {
        this.comments = comments;
    }

    private ArrayList<Map<String,String>> comments;

    public boolean isIs_lent() {
        return is_lent;
    }

    public void setIs_lent(boolean is_lent) {
        this.is_lent = is_lent;
    }

    public String getPublisher() {
        return publisher;
    }

    public void setPublisher(String publisher) {
        this.publisher = publisher;
    }

    public String getAbstracts() {
        return abstracts;
    }

    public void setAbstracts(String abstracts) {
        this.abstracts = abstracts;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
    @Override
    public boolean equals(@Nullable Object obj) {
        Book u = (Book) obj;
        assert u != null;
        return this.id.equals(u.getId());
    }

}
