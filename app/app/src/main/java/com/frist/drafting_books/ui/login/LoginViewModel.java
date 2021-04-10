package com.frist.drafting_books.ui.login;

import android.util.Patterns;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.DB.LoginCallback;
import com.frist.drafting_books.DB.SignUpCallback;
import com.frist.drafting_books.R;
import com.frist.drafting_books.data_login.LoginRepository;
import com.frist.drafting_books.data_login.Result;
import com.frist.drafting_books.data_login.model.LoggedInUser;
import com.frist.drafting_books.ui.login_default.LoginFormState;

import cn.leancloud.AVUser;

public class LoginViewModel extends ViewModel {
    private MutableLiveData<AVUser> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LoginModel loginModel;
    private LoginActivity activity;
    LoginViewModel(LoginActivity activity) {
        this.loginModel = new LoginModel();
        this.activity = activity;
    }
    LoginViewModel(LoginActivity activity,LoginModel loginModel) {
        this.loginModel = loginModel;
        this.activity = activity;
    }

    LiveData<AVUser> getLoginResult() {
        return loginResult;
    }
    LiveData<LoginFormState> getLoginState() {
        return loginFormState;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
//        todo 这里是登录函数，登录成功和失败的情况
        LeancloudDB dbt = LeancloudDB.getInstance();
        dbt.Login(username, password, new LoginCallback() {
            @Override
            public void Success() {
                loginResult.setValue(AVUser.getCurrentUser());
            }

            @Override
            public void Fail() {
//                loginResult.setValue(new LoginResult(222));
//                Toast.makeText()
            }
        });


//        Result<LoggedInUser> result = loginRepository.login(username, password);
//
//        if (result instanceof Result.Success) {
//            LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
//            loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
//        } else {
//            loginResult.setValue(new LoginResult(R.string.login_failed));
//        }
    }
    public void Signup(String u,String p){
        LeancloudDB dbt = LeancloudDB.getInstance();
        dbt.addUser(u, p, new SignUpCallback() {
            @Override
            public void Success() {
                dbt.Login(u, p, new LoginCallback() {
                    @Override
                    public void Success() {
                        loginResult.setValue(AVUser.getCurrentUser());
                    }
                    @Override
                    public void Fail() {

                    }
                });
            }
            @Override
            public void Fail() {

            }
        });
    }
    public void loginDataChanged(String username, String password) {
        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_username, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {
        if (username == null) {
            return false;
        }
        if (username.contains("@")) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        } else {
            return !username.trim().isEmpty();
        }
    }

    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }
}