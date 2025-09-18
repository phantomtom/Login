package com.example.test1;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.test1.databinding.FragmentModifyBinding;
import com.github.ybq.android.spinkit.SpinKitView;


public class ModifyFragment extends Fragment {

    private FragmentModifyBinding binding;

    private MyDbHelper helper;
    private SQLiteDatabase db;
    private EditText et_username, et_password_old, et_password_new;
    private String usernameString, passwordOldString, passwordNewString;
    private ImageView iv_avatar;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setLightStatusIcon(true);


        binding = FragmentModifyBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpinKitView kit_third = (SpinKitView) getView().findViewById(R.id.spin_kit_third);
        kit_third.setVisibility(View.GONE);

        et_username = getView().findViewById(R.id.textPersonNameModify);
        et_password_old = getView().findViewById(R.id.textPasswordOld);
        et_password_new = getView().findViewById(R.id.textPasswordNew);
        iv_avatar = getView().findViewById(R.id.imageView_modify);

        helper = new MyDbHelper(getActivity(), "users", null, 2);
        // 调用 getWritableDatabase()或者 getReadableDatabase()其中一个方法将数据库建立
        db = helper.getWritableDatabase();  //得到的是SQLiteDatabase对象


        binding.buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                NavHostFragment.findNavController(ModifyFragment.this)
                        .navigate(R.id.action_ModifyFragment_to_SecondFragment);
            }
        });

        binding.buttonModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usernameString = et_username.getText().toString();
                passwordOldString = et_password_old.getText().toString();
                passwordNewString = et_password_new.getText().toString();
                String password_match = selectByUsername(usernameString, 2, null);

                if (selectByUsername(usernameString, 1, null) == null) {
                    Toast.makeText(getActivity().getApplicationContext(), "用户名不存在", Toast.LENGTH_SHORT).show();
                } else if (!password_match.equals(passwordOldString)) {
                    Toast.makeText(getActivity().getApplicationContext(), "旧密码错误", Toast.LENGTH_SHORT).show();
                } else {
                    String sql_update1 = String.format("update users set password = '%s' where username = '%s'", passwordNewString, usernameString);
                    db.execSQL(sql_update1);
                    Toast.makeText(getActivity().getApplicationContext(), "密码修改成功：" + passwordNewString, Toast.LENGTH_SHORT).show();
                }

            }
        });

        et_username.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    usernameString = et_username.getText().toString();
                    Log.d("usernameOnFocusChange", usernameString);
                    if (selectByUsername(usernameString, 1, null) != null) {
                        String picUriString = selectByUsername(usernameString, 3, null);
                        Log.d("picUriString", picUriString);
                        Uri picUri = Uri.parse(picUriString);
                        iv_avatar.setImageURI(picUri);

                    }
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