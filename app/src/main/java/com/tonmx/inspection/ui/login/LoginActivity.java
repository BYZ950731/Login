package com.tonmx.inspection.ui.login;

import android.app.Activity;

import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.annotation.StringRes;
import androidx.appcompat.app.AppCompatActivity;

import android.os.UserHandle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.text.method.HideReturnsTransformationMethod;
import android.text.method.PasswordTransformationMethod;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.tonmx.inspection.DialogUtil;
import com.tonmx.inspection.FormInformationActivity;
import com.tonmx.inspection.InspectionService;
import com.tonmx.inspection.PrefData;
import com.tonmx.inspection.R;
import com.tonmx.inspection.ToastUtil;
import com.tonmx.inspection.UserInformation;
import com.tonmx.inspection.ui.login.LoginViewModel;
import com.tonmx.inspection.ui.login.LoginViewModelFactory;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class LoginActivity extends Activity {
    private EditText usernameEditText,passwordEditText,ipEditText;
    private ImageView username,userpassword,userip,loginButton,passwordEye;
    private int eye_click=0,loginStatus;
    private String strAccount,strPassword,strip;

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Intent intent = new Intent();
        intent.setClass(getApplicationContext(), InspectionService.class);
        startService(intent);
        loginStatus = PrefData.getLoginStatus();
        strAccount = PrefData.getHisName();
        strPassword = PrefData.getHisPwd();
        strip = PrefData.getHisIP();
        Log.i("BYZ","11234");
//        if(loginStatus == 1 ){//未注销，自动登录
//            Intent intent = new Intent(LoginActivity.this,
//                    UserInformation.class);
//            startActivity(intent);
//            finish();
//
//        } else {//已注销，重新登录
            init();
    //    }



    }

    @Override
    protected void onResume() {
        super.onResume();

    }


    private void showLoginFailed(@StringRes Integer errorString) {
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }


    public void login() {                        //登录按钮监听事件
            String userName = usernameEditText.getText().toString().trim();//获取当前输入的用户名和密码信息
            String userPwd = passwordEditText.getText().toString().trim();
            String userip = ipEditText.getText().toString().trim();
            sendLoginBroadcast(userName,userPwd,userip);

    }

    private void sendLoginBroadcast(String name,String password,String ip) {
        Intent intent = new Intent("com.tonmx.inspection.login");
        intent.putExtra("username", name);
        intent.putExtra("password", password);
        intent.putExtra("ip", ip);
        sendBroadcast(intent);
    }

    private void init() {
        usernameEditText = (EditText) findViewById(R.id.login_user_input);
        if(strAccount != null && strAccount.length()>=0){
            usernameEditText.setText(strAccount);
        }
        passwordEditText = (EditText) findViewById(R.id.login_password_input);
        if(strPassword != null && strPassword.length()>=0){
            passwordEditText.setText(strPassword);
        }
        passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
        ipEditText = (EditText) findViewById(R.id.login_ip_input);
        if(strip != null && strip.length()>=0){
            ipEditText.setText(strip);
        }
        username = (ImageView) findViewById(R.id.login_user);
        userpassword = (ImageView) findViewById(R.id.login_password);
        userip = (ImageView) findViewById(R.id.login_ip);


        passwordEye = (ImageView) findViewById(R.id.password_eye);
        passwordEye.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(eye_click==0) {
                    passwordEditText.setTransformationMethod(PasswordTransformationMethod.getInstance());
                    passwordEye.setImageResource(R.mipmap.close_eye);

                    eye_click =1;
                } else {
                    passwordEditText.setTransformationMethod(HideReturnsTransformationMethod.getInstance());
                    passwordEye.setImageResource(R.mipmap.open_eye);
                    eye_click = 0;
                }
            }
        });
        loginButton = (ImageView) findViewById(R.id.login);
        if(!TextUtils.isEmpty(strip) &&
                !TextUtils.isEmpty(strAccount) &&
                !TextUtils.isEmpty(strPassword)){
            loginButton.setEnabled(true);
            loginButton.setImageResource(R.mipmap.login_light);
        } else {
            loginButton.setEnabled(false);
            loginButton.setImageResource(R.mipmap.login);
        }


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });
        final ProgressBar loadingProgressBar = (ProgressBar) findViewById(R.id.loading);

        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                if(TextUtils.isEmpty(usernameEditText.getText())||
                        TextUtils.isEmpty(passwordEditText.getText())||
                        TextUtils.isEmpty(ipEditText.getText())){
                    loginButton.setImageResource(R.mipmap.login);
                    loginButton.setEnabled(false);
                } else {
                    loginButton.setImageResource(R.mipmap.login_light);
                    loginButton.setEnabled(true);
                }

//                if(!TextUtils.isEmpty(usernameEditText.getText())){
//                    username.setColorFilter(R.color.white);
//                }else {
//                    username.setImageResource(R.mipmap.login_user);
//                }


            }
        };

        usernameEditText.addTextChangedListener(afterTextChangedListener);
        passwordEditText.addTextChangedListener(afterTextChangedListener);
        ipEditText.addTextChangedListener(afterTextChangedListener);
    }
}