package com.frist.drafting_books.ui.community;

import java.io.Serializable;

import cn.leancloud.AVObject;

public class BundleBoat extends AVObject implements Serializable {
    BundleBoat(AVObject oj){
        super(oj);

        System.out.println("gouzao");
    }


}
