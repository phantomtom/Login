package com.example.test1;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    private ListView messagesListView;
    private EditText messageInput;
    private ImageButton sendButton;
    private TextView chatTitle;

    private ChatAdapter chatAdapter;
    private List<ChatMessage> messages;
    private ChatDbHelper chatDbHelper;
    private SQLiteDatabase chatDb;

    private String currentUser;
    private String targetUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        // 获取聊天对象用户名
        targetUser = getIntent().getStringExtra("target_username");

        // 获取当前用户名
        SharedPreferences sp = getSharedPreferences("LOGIN", MODE_PRIVATE);
        currentUser = sp.getString("username", "");

        initViews();
        setupDatabase();
        loadMessages();
        setupListeners();
    }

    private void initViews() {
        messagesListView = findViewById(R.id.messages_list);
        messageInput = findViewById(R.id.message_input);
        sendButton = findViewById(R.id.send_button);
        chatTitle = findViewById(R.id.chat_title);

        chatTitle.setText("与 " + targetUser + " 聊天");

        messages = new ArrayList<>();
        chatAdapter = new ChatAdapter(this, messages);
        messagesListView.setAdapter(chatAdapter);
    }

    private void setupDatabase() {
        chatDbHelper = new ChatDbHelper(this);
        chatDb = chatDbHelper.getWritableDatabase();
    }

    @SuppressLint("Range")
    private void loadMessages() {
        messages.clear();

        String query = "SELECT * FROM chat_messages WHERE " +
                "(sender = ? AND receiver = ?) OR (sender = ? AND receiver = ?) " +
                "ORDER BY timestamp ASC";

        Cursor cursor = chatDb.rawQuery(query,
                new String[]{currentUser, targetUser, targetUser, currentUser});

        while (cursor.moveToNext()) {
            ChatMessage message = new ChatMessage();
            message.setSender(cursor.getString(cursor.getColumnIndex("sender")));
            message.setReceiver(cursor.getString(cursor.getColumnIndex("receiver")));
            message.setContent(cursor.getString(cursor.getColumnIndex("content")));
            message.setTimestamp(cursor.getLong(cursor.getColumnIndex("timestamp")));
            message.setMessageType(cursor.getInt(cursor.getColumnIndex("message_type")));

            messages.add(message);
        }
        cursor.close();

        chatAdapter.notifyDataSetChanged();
        scrollToBottom();
    }

    private void setupListeners() {
        sendButton.setOnClickListener(v -> sendMessage());

        messageInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                sendButton.setEnabled(s.length() > 0);
            }

            @Override
            public void afterTextChanged(Editable s) {}
        });
    }

    private void sendMessage() {
        String content = messageInput.getText().toString().trim();
        if (content.isEmpty()) return;

        ChatMessage message = new ChatMessage(currentUser, targetUser, content);
        saveMessageToDatabase(message);

        messages.add(message);
        chatAdapter.notifyDataSetChanged();

        messageInput.setText("");
        scrollToBottom();

        // 模拟对方回复（可选，实际应用中应通过网络通信）
        simulateReply();
    }

    private void saveMessageToDatabase(ChatMessage message) {
        String sql = "INSERT INTO chat_messages (sender, receiver, content, timestamp, message_type) " +
                "VALUES (?, ?, ?, ?, ?)";
        chatDb.execSQL(sql, new Object[]{
                message.getSender(),
                message.getReceiver(),
                message.getContent(),
                message.getTimestamp(),
                message.getMessageType()
        });
    }

    private void scrollToBottom() {
        messagesListView.post(() -> {
            if (chatAdapter.getCount() > 0) {
                messagesListView.setSelection(chatAdapter.getCount() - 1);
            }
        });
    }

    private void simulateReply() {
        // 模拟自动回复（可选功能）
        new Handler().postDelayed(() -> {
            String[] replies = {
                    "你好！",
                    "很高兴和你聊天！",
                    "这个功能很有趣！",
                    "我收到了你的消息",
                    "谢谢你的消息"
            };

            String reply = replies[(int) (Math.random() * replies.length)];
            ChatMessage autoReply = new ChatMessage(targetUser, currentUser, reply);

            saveMessageToDatabase(autoReply);
            messages.add(autoReply);
            chatAdapter.notifyDataSetChanged();
            scrollToBottom();

        }, 1000 + (long) (Math.random() * 2000));
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (chatDb != null) {
            chatDb.close();
        }
        if (chatDbHelper != null) {
            chatDbHelper.close();
        }
    }
}