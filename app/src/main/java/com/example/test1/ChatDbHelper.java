package com.example.test1;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import androidx.annotation.Nullable;

public class ChatDbHelper extends SQLiteOpenHelper {
    private static final String DATABASE_NAME = "chat.db";
    private static final int DATABASE_VERSION = 1;

    public ChatDbHelper(@Nullable Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // 创建聊天消息表
        String createTable = "CREATE TABLE chat_messages (" +
                "id INTEGER PRIMARY KEY AUTOINCREMENT, " +
                "message_id TEXT, " +
                "sender TEXT, " +
                "receiver TEXT, " +
                "content TEXT, " +
                "timestamp INTEGER, " +
                "message_type INTEGER, " +
                "is_read INTEGER DEFAULT 0)";
        db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS chat_messages");
        onCreate(db);
    }
}