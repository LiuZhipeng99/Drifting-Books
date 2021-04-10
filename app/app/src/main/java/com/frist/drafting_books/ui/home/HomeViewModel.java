package com.frist.drafting_books.ui.home;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class HomeViewModel extends ViewModel {

    private MutableLiveData<String> userName;

    public HomeViewModel() {
        userName = new MutableLiveData<>();
        userName.setValue("This is home fragment!!!!!");
    }

    public LiveData<String> getText() {
        return userName;
    }
}