package com.example.test1;


import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.navigation.fragment.NavHostFragment;

import com.example.test1.databinding.ActivityLoginBinding;
import com.example.test1.databinding.FragmentFirstBinding;
import com.github.ybq.android.spinkit.SpinKitView;

public class FirstFragment extends Fragment implements View.OnClickListener{

    private FragmentFirstBinding binding;
    ImageView image_choose;
    private ActivityResultLauncher<Intent> mResultLauncher;
    private Uri picUri;
    Button button_sign_up;
    MyDbHelper helper;
    private SQLiteDatabase db;
    EditText et_username, et_password, et_password_confirm;


    @Override
    public View onCreateView(
            LayoutInflater inflater, ViewGroup container,
            Bundle savedInstanceState
    ) {
        setLightStatusIcon(true);


        binding = FragmentFirstBinding.inflate(inflater, container, false);
        return binding.getRoot();

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        SpinKitView kit_first = getView().findViewById(R.id.spin_kit_first);
        kit_first.setVisibility(View.GONE);

        requestAllPower();

        helper = new MyDbHelper(getActivity(), "users", null, 2);
        // 调用 getWritableDatabase()或者 getReadableDatabase()其中一个方法将数据库建立
        db = helper.getWritableDatabase();  //得到的是SQLiteDatabase对象


        image_choose = getView().findViewById(R.id.imageView_avatar);
        image_choose.setOnClickListener(this);
        button_sign_up = getView().findViewById(R.id.button_sign_up);
        button_sign_up.setOnClickListener(this);
        et_username = getView().findViewById(R.id.textPersonNameFirst);
        et_password = getView().findViewById(R.id.textPasswordFirst);
        et_password_confirm = getView().findViewById(R.id.textPasswordConfirmFirst);

        mResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
            @Override
            public void onActivityResult(ActivityResult result) {
                if (result.getResultCode() ==getActivity().RESULT_OK) {
                    Intent intent = result.getData();
                    picUri = intent.getData();
                    if (picUri != null) {
                        image_choose.setImageURI(picUri);

                    }
                }
            }
        });

        binding.buttonSignInFirst.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 显示加载进度条
                kit_first.setVisibility(View.VISIBLE);

                // 禁用按钮防止重复点击
                binding.buttonSignInFirst.setEnabled(false);
                button_sign_up.setEnabled(false);

                // 使用Handler延迟执行跳转，让用户看到加载效果
                new android.os.Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        NavHostFragment.findNavController(FirstFragment.this)
                                .navigate(R.id.action_FirstFragment_to_SecondFragment);

                        // 跳转后恢复按钮状态（可选）
                        binding.buttonSignInFirst.setEnabled(true);
                        button_sign_up.setEnabled(true);
                    }
                }, 1000); // 延迟1秒执行跳转
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
                    View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN|View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
        }
    }





    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(getActivity(),
                Manifest.permission.WRITE_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(getActivity(),
                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            for (int i = 0; i < permissions.length; i++) {
                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getActivity().getApplicationContext(), "" + "权限已获取", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getActivity().getApplicationContext(), "" + "权限获取失败", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        int id = v.getId();

        if (id == R.id.imageView_avatar) {
            Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
            intent.setType("image/*");
            mResultLauncher.launch(intent);
        } else if (id == R.id.button_sign_up) {
            String usernameString = et_username.getText().toString();
            String passwordString = et_password.getText().toString();
            String passwordConfirmString = et_password_confirm.getText().toString();
            Cursor cursor = db.rawQuery("select * from users where username = ?", new String[]{usernameString});

            if (cursor.moveToFirst()) {
                Toast.makeText(getActivity().getApplicationContext(), "用户名已被注册", Toast.LENGTH_SHORT).show();
            } else if (picUri == null) {
                Toast.makeText(getActivity().getApplicationContext(), "未选取图片", Toast.LENGTH_SHORT).show();
            } else if (!passwordString.equals(passwordConfirmString)) {
                Toast.makeText(getActivity().getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
            } else {
                String sql_insert1 = String.format("insert into users (username, password, avatar, motto) values('%s','%s','%s','%s')",
                        usernameString,
                        passwordString,
                        picUri.toString(),
                        "这个人很懒，什么都没留下。");
                db.execSQL(sql_insert1);
                Toast.makeText(getActivity().getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
            }
        }
    }

}