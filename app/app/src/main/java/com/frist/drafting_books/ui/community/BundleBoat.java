package com.frist.drafting_books.ui.community;

import java.io.Serializable;

import cn.leancloud.AVObject;
//这个类用来作为传递适配器。把avobject放到bundle传输
public class BundleBoat extends AVObject implements Serializable {
    BundleBoat(AVObject oj){
        super(oj);

        System.out.println("gouzao");
    }


}
