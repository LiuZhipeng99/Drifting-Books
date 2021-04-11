package com.frist.drafting_books.DB.not_used;

import androidx.annotation.Nullable;

import java.util.ArrayList;

public class User {
    private String id;
    private String nickname;
    private String password;
    private String imageLink;
    private ArrayList<String> bookID;

    private String dingwei;
    public User(String id, String nickname,String password) {
        this.id = id;
        this.nickname = nickname;
        this.bookID = new ArrayList<>();
        this.imageLink = "https://profile.csdnimg.cn/5/8/B/0_qq_43520571";
        this.password = password;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getImageLink() {
        return imageLink;
    }

    public void setImageLink(String imageLink) {
        this.imageLink = imageLink;
    }

    public ArrayList<String> getBookID() {
        return bookID;
    }

    public void setBookID(ArrayList<String> bookID) {
        this.bookID = bookID;
    }

    @Override
    public boolean equals(@Nullable Object obj) {
        User u = (User) obj;
        assert u != null;
        return this.id.equals(u.getId());
    }
}
