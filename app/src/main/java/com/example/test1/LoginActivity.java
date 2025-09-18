package com.example.test1;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.drawable.AnimationDrawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;

import com.example.test1.databinding.FragmentSecondBinding;
import com.google.android.material.snackbar.Snackbar;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.fragment.NavHostFragment;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;

import com.example.test1.databinding.ActivityLoginBinding;

import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    //    private AppBarConfiguration appBarConfiguration;
    private ActivityLoginBinding binding;
    ImageView image_choose;
    private ActivityResultLauncher<Intent> mResultLauncher;
    private Uri picUri;
    Button button_sign_up;
    MyDbHelper helper;
    private SQLiteDatabase db;
    EditText et_username, et_password, et_password_confirm;


    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);

        binding = ActivityLoginBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
//        requestAllPower();

//        helper = new MyDbHelper(LoginActivity.this, "users", null, 2);
//        // 调用 getWritableDatabase()或者 getReadableDatabase()其中一个方法将数据库建立
//        db = helper.getWritableDatabase();  //得到的是SQLiteDatabase对象


//        image_choose = findViewById(R.id.imageView_avatar);
//        image_choose.setOnClickListener(this);
//        button_sign_up = findViewById(R.id.button_sign_up);
//        button_sign_up.setOnClickListener(this);
//        et_username = findViewById(R.id.textPersonNameFirst);
//        et_password = findViewById(R.id.textPasswordFirst);
//        et_password_confirm = findViewById(R.id.textPasswordConfirmFirst);
//
//        mResultLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), new ActivityResultCallback<ActivityResult>() {
//            @Override
//            public void onActivityResult(ActivityResult result) {
//                if (result.getResultCode() == RESULT_OK) {
//                    Intent intent = result.getData();
//                    picUri = intent.getData();
//                    if (picUri != null) {
//                        image_choose.setImageURI(picUri);
//                        SharedPreferences sp = getSharedPreferences("AVATAR", MODE_PRIVATE);
//                        SharedPreferences.Editor editor = sp.edit();
//                        editor.putString("avatar", picUri.toString());
//                        editor.apply();
//                    }
//                }
//            }
//        });
    }


    @Override
    public void onClick(View v) {
//        switch (v.getId()) {
//            case R.id.imageView_avatar:
//                Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
//                intent.setType("image/*");
//                mResultLauncher.launch(intent);
//                break;
//            case R.id.button_sign_up:
//                String usernameString = et_username.getText().toString();
//                String passwordString = et_password.getText().toString();
//                String passwordConfirmString = et_password_confirm.getText().toString();
//                Cursor cursor = db.rawQuery("select * from users where username = ?", new String[]{usernameString});
//                if (cursor.moveToFirst()) {
//                    Toast.makeText(getApplicationContext(), "用户名已被注册", Toast.LENGTH_SHORT).show();
//                } else if (picUri == null) {
//                    Toast.makeText(getApplicationContext(), "未选取图片", Toast.LENGTH_SHORT).show();
//                } else if (!passwordString.equals(passwordConfirmString)) {
//                    Toast.makeText(getApplicationContext(), "两次密码不一致", Toast.LENGTH_SHORT).show();
//                } else {
//                    String sql_insert1 = String.format("insert into users (username, password, avatar) values('%s','%s','%s')", usernameString, passwordString, picUri.toString());
//                    db.execSQL(sql_insert1);
//                    Toast.makeText(getApplicationContext(), "注册成功", Toast.LENGTH_SHORT).show();
//                }
//                break;
//
//        }
    }
//
//    public void requestAllPower() {
//        if (ContextCompat.checkSelfPermission(this,
//                Manifest.permission.WRITE_EXTERNAL_STORAGE)
//                != PackageManager.PERMISSION_GRANTED) {
//            if (ActivityCompat.shouldShowRequestPermissionRationale(this,
//                    Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
//            } else {
//                ActivityCompat.requestPermissions(this,
//                        new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
//                                Manifest.permission.READ_EXTERNAL_STORAGE}, 1);
//            }
//        }
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//
//        if (requestCode == 1) {
//            for (int i = 0; i < permissions.length; i++) {
//                if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
//                    Toast.makeText(this, "" + "权限已获取", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "" + "权限获取失败", Toast.LENGTH_SHORT).show();
//                }
//            }
//        }
//    }
}




