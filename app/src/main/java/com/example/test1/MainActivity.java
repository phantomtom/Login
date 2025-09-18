package com.example.test1;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.fragment.NavHostFragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.databinding.ActivityLoginBinding;
import com.example.test1.databinding.ActivityMainBinding;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    // ViewBinding 对象，用于替代 findViewById
    private ActivityMainBinding binding;

    // 用于存储用户数据的列表
    private ArrayList<String> avatars = new ArrayList<>();    // 头像URI列表
    private ArrayList<String> usernames = new ArrayList<>();  // 用户名列表
    private ArrayList<String> mottos = new ArrayList<>();     // 个性签名列表

    // UI组件声明
    private ListView lv_view;                // 用户列表视图
    private MyAdapter myAdapter;             // 自定义适配器
    private Uri avatarUri;                   // 当前用户头像URI
    private ImageView imageViewAvatar, imageViewSignOut, imageViewDelete; // 头像、退出、删除按钮
    private String usernameString, mottoString; // 当前用户名和个性签名
    private TextView textViewUsername;       // 显示用户名的TextView
    EditText editTextMotto;                  // 编辑个性签名的EditText

    // 数据库相关
    private MyDbHelper helper;               // 数据库帮助类
    private SQLiteDatabase db;               // 数据库实例

    @Override
    public <T extends View> T findViewById(int id) {
        return super.findViewById(id);
    }

    // 忽略可访问性检查的注解
    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        // 使用ViewBinding设置布局
        binding = ActivityMainBinding.inflate(getLayoutInflater());

        // 从Fragment中查找视图组件
        imageViewSignOut = getSupportFragmentManager().findFragmentById(R.id.fragment_letter).getView().findViewById(R.id.imageView_sign_out);
        imageViewDelete = getSupportFragmentManager().findFragmentById(R.id.fragment_letter).getView().findViewById(R.id.imageView_delete);

        // 初始化数据库
        helper = new MyDbHelper(this, "users", null, 2);
        // 获取可写数据库实例
        db = helper.getWritableDatabase();

        setContentView(binding.getRoot());

        // 从SharedPreferences获取登录信息
        SharedPreferences sp1 = getSharedPreferences("LOGIN", MODE_PRIVATE);
        usernameString = sp1.getString("username", "0"); // 默认值为"0"
        // 从数据库查询当前用户的个性签名
        mottoString = selectByUsername(usernameString, 4, null);

        // 查询数据库中的所有用户
        Cursor cursor = db.query("users", null, null, null, null, null, null);
        if (cursor.moveToFirst()) {
            do {
                // 排除当前用户，将其他用户信息添加到列表中
                if(!usernameString.equals(cursor.getString(1))){
                    avatars.add(cursor.getString(3));  // 第3列是头像
                    usernames.add(cursor.getString(1)); // 第1列是用户名
                    mottos.add(cursor.getString(4));   // 第4列是个性签名
                }
            } while (cursor.moveToNext());
        }

        // 初始化UI组件
        textViewUsername = findViewById(R.id.tv_username);
        if (!usernameString.equals("0")) {
            textViewUsername.setText(usernameString); // 设置用户名
        } else {
            Log.d("tv_username", "null"); // 日志记录空用户名
        }

        editTextMotto = findViewById(R.id.et_motto);
        if (mottoString != null) {
            editTextMotto.setText(mottoString); // 设置个性签名
        } else {
            editTextMotto.setText("这个人很懒，什么都没留下。"); // 默认签名
            Log.d("et_motto", "null"); // 日志记录空签名
        }

        // 从数据库查询当前用户头像
        String avatarString = selectByUsername(usernameString, 3, null);
        imageViewAvatar = findViewById(R.id.imageView_avatar_letter);
        if (avatarString != null) {
            avatarUri = Uri.parse(avatarString); // 将字符串转换为URI
            Log.d("avatarString", avatarString); // 日志记录头像URI
            imageViewAvatar.setImageURI(null); // 先清空图像
            imageViewAvatar.setImageURI(avatarUri); // 设置头像
        }

        // 初始化列表视图和适配器
        lv_view = (ListView) findViewById(R.id.lv_view);
        myAdapter = new MyAdapter();
        myAdapter.setContext(this);
        myAdapter.setmData(getList()); // 设置适配器数据
        lv_view.setAdapter(myAdapter);

        // 列表项点击事件监听器
        lv_view.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> arg0, View arg1, int position, long arg3) {
                // 获取点击的用户信息
                PeopleInfo people = (PeopleInfo) myAdapter.getItem(position);
                String usernameStringTo = people.getUsername();
//                String mottoStringTo = people.getMotto();
//
//                // 保存用户信息到SharedPreferences，用于后续比较
//                SharedPreferences sp5 = getSharedPreferences("SIMILARITY", MODE_PRIVATE);
//                SharedPreferences.Editor editor5 = sp5.edit();
//                editor5.putString("username", usernameString);
//                editor5.putString("usernameTo", usernameStringTo);
//                editor5.putString("motto", mottoString);
//                editor5.putString("mottoTo", mottoStringTo);
//                editor5.apply();

                // 改为启动聊天页面
                Intent intent = new Intent(MainActivity.this, ChatActivity.class);
                intent.putExtra("target_username", usernameStringTo);
                startActivity(intent);

                // 显示用户信息提示
                Toast.makeText(MainActivity.this, "昵称：" + people.getUsername() + "\n个性签名：" + people.getMotto(), Toast.LENGTH_SHORT).show();
            }
        });

        // 列表触摸事件监听器，用于清除EditText的焦点
        lv_view.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    editTextMotto.clearFocus(); // 清除焦点
                }
                return false;
            }
        });

        // 个性签名编辑框焦点变化监听器
        editTextMotto.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (!hasFocus) {
                    // 失去焦点时更新数据库中的个性签名
                    String mottoStringNew = editTextMotto.getText().toString();
                    String sql_update1 = String.format("update users set motto = '%s' where username = '%s'", mottoStringNew, usernameString);
                    db.execSQL(sql_update1);
                }
            }
        });

        // 退出按钮点击事件
        imageViewSignOut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 清除自动登录信息
                SharedPreferences sp3 = getSharedPreferences("AUTO_LOGIN", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit3 = sp3.edit();
                edit3.clear();
                edit3.commit();

                Toast.makeText(MainActivity.this, "退出成功", Toast.LENGTH_SHORT).show();

                // 延时退出应用
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0); // 退出应用
                    }
                }, 300); // 延时300毫秒执行
            }
        });

        // 注销按钮点击事件
        imageViewDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 从数据库删除用户
                String sql_delete1 = String.format("delete from users where username = '%s'", usernameString);
                db.execSQL(sql_delete1);

                // 清除登录信息
                SharedPreferences sp4 = getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
                SharedPreferences.Editor edit4 = sp4.edit();
                edit4.clear();
                edit4.commit();

                Toast.makeText(MainActivity.this, "注销成功", Toast.LENGTH_SHORT).show();

                // 延时退出应用
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        System.exit(0); // 退出应用
                    }
                }, 300); // 延时300毫秒执行
            }
        });
    }

    // 获取用户信息列表
    private List<PeopleInfo> getList() {
        List<PeopleInfo> list = new ArrayList<PeopleInfo>();
        for (int i = 0; i < avatars.size(); i++) {
            PeopleInfo people = new PeopleInfo();
            people.setAvatar(avatars.get(i));     // 设置头像
            people.setMotto(mottos.get(i));       // 设置个性签名
            people.setUsername(usernames.get(i)); // 设置用户名
            list.add(people);                     // 添加到列表
        }
        return list;
    }

    // 根据用户名查询数据库
    public String selectByUsername(String username, int key, String defValue) {
        // 执行参数化查询，防止SQL注入
        Cursor cursor = db.rawQuery("select * from users where username = ?", new String[]{username});
        if (cursor.moveToFirst()) {
            return cursor.getString(key); // 返回指定列的值
        } else {
            return defValue; // 返回默认值
        }
    }
}