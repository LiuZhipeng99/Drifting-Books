package com.frist.drafting_books.ui.login;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import android.util.Patterns;
import android.widget.Toast;

import com.frist.drafting_books.DB.LeancloudDB;
import com.frist.drafting_books.DB.LoginCallback;
import com.frist.drafting_books.DB.SignUpCallback;
import com.frist.drafting_books.data.LoginRepository;
import com.frist.drafting_books.data.Result;
import com.frist.drafting_books.data.model.LoggedInUser;
import com.frist.drafting_books.R;

import cn.leancloud.AVUser;

public class LoginViewModel extends ViewModel {

    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

    LoginViewModel(LoginRepository loginRepository) {
        this.loginRepository = loginRepository;
    }

    LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }

    LiveData<LoginResult> getLoginResult() {
        return loginResult;
    }

    public void login(String username, String password) {
        // can be launched in a separate asynchronous job
//        todo 这里是登录函数，登录成功和失败的情况
        LeancloudDB dbt = LeancloudDB.getInstance();
        dbt.Login(username, password, new LoginCallback() {
            @Override
            public void Success() {
                LoggedInUser fakeUser =
                        new LoggedInUser(
                                AVUser.getCurrentUser().getObjectId(),
                                username);
                Result.Success<LoggedInUser> result = new Result.Success<>(fakeUser);
                LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
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
                        LoggedInUser fakeUser =
                                new LoggedInUser(
                                        AVUser.getCurrentUser().getObjectId(),
                                        u);
                        Result.Success<LoggedInUser> result = new Result.Success<>(fakeUser);
                        LoggedInUser data = ((Result.Success<LoggedInUser>) result).getData();
                        loginResult.setValue(new LoginResult(new LoggedInUserView(data.getDisplayName())));
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