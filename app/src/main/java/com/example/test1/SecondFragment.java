package com.example.test1;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;

import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.test1.databinding.FragmentSecondBinding;
import com.github.ybq.android.spinkit.SpinKitView;

public class SecondFragment extends Fragment {

    private FragmentSecondBinding binding;
    boolean isRememberChecked = false;
    boolean isAutoLoginChecked = false;
    private MyDbHelper helper;
    private SQLiteDatabase db;
    private String usernameString, passwordString, isRemember;
    private EditText username, password;
    private RadioButton remember, auto_login;
    private ImageView iv_avatar;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {

        setLightStatusIcon(false);
        binding = FragmentSecondBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        username = getView().findViewById(R.id.textPersonNameSecond);
        password = getView().findViewById(R.id.textPasswordSecond);

        remember = getView().findViewById(R.id.radioButtonRemember);
        auto_login = getView().findViewById(R.id.radioButtonAutoLogin);

        iv_avatar = getView().findViewById(R.id.imageView_sign_in);

        SpinKitView kit_second = getView().findViewById(R.id.spin_kit_second);
        kit_second.setVisibility(View.GONE);





        helper = new MyDbHelper(getActivity(), "users", null, 2);
        // 调用 getWritableDatabase()或者 getReadableDatabase()其中一个方法将数据库建立
        db = helper.getWritableDatabase();  //得到的是SQLiteDatabase对象

        binding.textViewModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_ModifyFragment);
            }
        });

        binding.radioButtonRemember.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(!isAutoLoginChecked){
                    isRememberChecked = !isRememberChecked;
                    remember.setChecked(isRememberChecked);
                }
            }
        });

        binding.radioButtonAutoLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isAutoLoginChecked = !isAutoLoginChecked;
                auto_login.setChecked(isAutoLoginChecked);
                if (isAutoLoginChecked) {
                    isRememberChecked = true;
                    remember.setChecked(true);
                }
            }
        });


        binding.buttonSignUpSecond.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(SecondFragment.this)
                        .navigate(R.id.action_SecondFragment_to_FirstFragment);
            }
        });

        username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    usernameString = username.getText().toString();
                    if (selectByUsername(usernameString, 1, null) != null) {
                        String picUriString = selectByUsername(usernameString, 3, null);
                        String passwordStringRemembered = selectByUsername(usernameString, 2, null);
                        Uri picUri = Uri.parse(picUriString);
                        iv_avatar.setImageURI(picUri);
                        SharedPreferences sp3 = getActivity().getSharedPreferences("LOGIN", getActivity().MODE_PRIVATE);
                        isRemember = sp3.getString(usernameString, "false");
                        if(isRemember.equals("true") && !isRememberChecked){
                            isRememberChecked = true;
                            remember.setChecked(true);
                            password.setText(passwordStringRemembered);
                        }
                    }
                }
            }
        });

        binding.buttonSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                usernameString = username.getText().toString();
                passwordString = password.getText().toString();
                Log.i("avatar", "登录");
                String password_match = selectByUsername(usernameString, 2, null);

                if (passwordString.equals(password_match)) {
                    kit_second.setVisibility(View.VISIBLE);

                    if (isAutoLoginChecked) {
                        SharedPreferences sp = getActivity().getSharedPreferences("AUTO_LOGIN",
                                Context.MODE_PRIVATE);
                        SharedPreferences.Editor edit = sp.edit();
                        edit.putString("username", usernameString);
                        edit.putString("password", passwordString);
                        edit.apply();//将数据写入到文件中
                    }


                    SharedPreferences sp = getActivity().getSharedPreferences("LOGIN",
                            Context.MODE_PRIVATE);
                    SharedPreferences.Editor edit = sp.edit();
                    edit.putString("username", usernameString);
                    edit.putString("password", passwordString);
                    edit.putString(usernameString, String.valueOf(isRememberChecked));
                    edit.apply();
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            kit_second.setVisibility(View.GONE);
                            Toast.makeText(getActivity().getApplicationContext(), "登录成功", Toast.LENGTH_SHORT).show();
                            Intent intent = new Intent();
                            intent.setClass(getActivity().getApplicationContext(), MainActivity.class);
                            startActivity(intent);
                            getActivity().finish();
                        }
                    }, 500);    //延时执行
//

                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "用户名或密码错误", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    protected void setLightStatusIcon(boolean light) {
        if (light) {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        } else {
            getActivity().getWindow().getDecorView().setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN | View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }

    public String selectByUsername(String username, int key, String defValue) {
        Cursor cursor = db.rawQuery("select * from users where username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            return cursor.getString(key);
        } else {
            return defValue;
        }
    }


}