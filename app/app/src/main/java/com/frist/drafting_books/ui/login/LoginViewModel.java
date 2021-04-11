package com.frist.drafting_books.ui.login;

import android.annotation.SuppressLint;
import android.util.Log;
import android.util.Patterns;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.frist.drafting_books.DB.LeanConfig;
import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.DB.LoginCallback;
import com.frist.drafting_books.DB.SignUpCallback;
import com.frist.drafting_books.R;
import com.frist.drafting_books.ui.login_default.LoginFormState;

import cn.leancloud.AVOSCloud;
import cn.leancloud.AVUser;

public class LoginViewModel extends ViewModel {
    //想直接用AVUser传但后面无法得到password，只能自己写个类
    private MutableLiveData<LoginUser> loginResult = new MutableLiveData<>();
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private LoginModel loginModel;
    @SuppressLint("StaticFieldLeak")
    private LoginActivity activity;
    LoginViewModel() {
        this.loginModel = new LoginModel();
    }
    public void setAct(LoginActivity act){
        this.activity = act;
    }
    LoginViewModel(LoginActivity activity) {
        this.loginModel = new LoginModel();
        this.activity = activity;
    }
//    LoginViewModel(LoginActivity activity,LoginModel loginModel) {
//        this.loginModel = loginModel;
//        this.activity = activity;
//    }

    LiveData<LoginUser> getLoginResult() {
        return loginResult;
    }
    LiveData<LoginFormState> getLoginState() {
        return loginFormState;
    }

    public void login(String username, String password) {

        // can be launched in a separate asynchronous job
//        todo 这里是登录函数，登录成功和失败的情况
//        LeancloudDB dbt = LeancloudDB.getInstance();
        //为了从此act进入，再把username和password传过去。写到DB的构造函数。
        LeancloudDB dbt = new LeancloudDB(activity.getApplication());
        dbt.Login(username, password, new LoginCallback() {
            @Override
            public void Success() {
//                loginResult.setValue(AVUser.getCurrentUser());
                loginResult.setValue(new LoginUser(username,password));
            }

            @Override
            public void Fail() {
//                loginResult.setValue(new LoginResult(222));
                Toast.makeText(activity.getApplicationContext(),"User not registered, now registered",Toast.LENGTH_LONG).show();
                dbt.addUser(username, password, new SignUpCallback() {
                    @Override
                    public void Success() {
                        dbt.Login(username, password, new LoginCallback() {
                            @Override
                            public void Success() {
                                loginResult.setValue(new LoginUser(username,password));
//                        loginResult.setValue("result_u");
                            }
                            @Override
                            public void Fail() {
                                Toast.makeText(activity.getApplicationContext(),"The username has been registered.2",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                    @Override
                    public void Fail() {
                        Toast.makeText(activity.getApplicationContext(),"The username has been registered",Toast.LENGTH_LONG).show();
                    }
                });
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
//        LeancloudDB dbt = LeancloudDB.getInstance();
        LeancloudDB dbt = new LeancloudDB(activity.getApplication());
        dbt.addUser(u, p, new SignUpCallback() {
            @Override
            public void Success() {
                dbt.Login(u, p, new LoginCallback() {
                    @Override
                    public void Success() {
                        loginResult.setValue(new LoginUser(u,p));
//                        loginResult.setValue("result_u");
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