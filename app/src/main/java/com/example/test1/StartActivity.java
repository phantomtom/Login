package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.WindowManager;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // 设置全屏模式
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_start);
        //判断在SharedPreference中存在登录信息，则跳转到MainActivity，否则跳转到

        SharedPreferences sp = getSharedPreferences("AUTO_LOGIN",
                Context.MODE_PRIVATE);
        // SharedPreferences.Editor edit = sp.edit();
        String username = sp.getString("username", "");
        String password = sp.getString("password", "");
        //应该判断Token,目前么有网络请求的功能，暂时使用判断用户名、密码代替
        Intent intent = null;
        if (username != null && username.length() > 0 &&
                password != null && password.length() > 0
        ) {
        //MainActivity
            intent = new Intent(this, MainActivity.class);
        } else {
        //LoginActivity
            intent = new Intent(this, LoginActivity.class);
        }
        //启动Activity
        startActivity(intent);
        finish();//避免后退回来
    }
}