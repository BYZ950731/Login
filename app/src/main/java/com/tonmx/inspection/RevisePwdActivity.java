package com.tonmx.inspection;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class RevisePwdActivity extends Activity {

    private EditText oldPwd,newPwd,newTwPwd;
    private TextView pwdConf;
    private ImageView back;
    private String strPwd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_revise_pwd);
        strPwd = PrefData.getHisPwd();
        oldPwd = (EditText) findViewById(R.id.old_pwd);
        newPwd = (EditText) findViewById(R.id.new_pwd);
        newTwPwd =(EditText)findViewById(R.id.new_twice_pwd);
        pwdConf =(TextView) findViewById(R.id.pwd_conf);
        back = (ImageView) findViewById(R.id.pwd_back);
        back.setEnabled(true);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent("com.tonmx.inspection.refreshUserInformationOnly");
                sendBroadcast(intent);
                finish();
                //startActivity(new Intent(RevisePwdActivity.this,UserInformation.class));
            }
        });
        pwdConf.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(!strPwd.equalsIgnoreCase(oldPwd.getText().toString())){

                    //Toast
                    ToastUtil.showShortToast(RevisePwdActivity.this,"旧密码错误！");
                } else if(!newPwd.getText().toString().equalsIgnoreCase(newTwPwd.getText().toString())){
                    ToastUtil.showShortToast(RevisePwdActivity.this,"新密码不一致！");
                  //  Toast
                } else if(newPwd.getText().toString().equalsIgnoreCase(oldPwd.getText().toString())){
                    ToastUtil.showShortToast(RevisePwdActivity.this,"新旧密码不能相同！");
                } else {

                    //修改密码，存库，且上报平台
                    //PrefData.setHisPwd(newPwd.getText().toString());
                    sendRevisePwdBroadcast(newPwd.getText().toString());
                }


            }
        });
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
                if(TextUtils.isEmpty(oldPwd.getText())||
                        TextUtils.isEmpty(newPwd.getText())||
                        TextUtils.isEmpty(newTwPwd.getText())){
                    pwdConf.setBackgroundColor(Color.parseColor("#B2B2B2"));
                    pwdConf.setEnabled(false);
                } else {
                    pwdConf.setBackgroundColor(Color.parseColor("#3385FF"));
                    pwdConf.setEnabled(true);
                }


            }
        };

        oldPwd.addTextChangedListener(afterTextChangedListener);
        newPwd.addTextChangedListener(afterTextChangedListener);
        newTwPwd.addTextChangedListener(afterTextChangedListener);


    }

    private void sendRevisePwdBroadcast(String password) {
        Intent intent = new Intent("com.tonmx.inspection.revisePwd");
        intent.putExtra("password", password);
        sendBroadcast(intent);
    }
}