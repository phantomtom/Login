package com.example.test1;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.Date;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;

public class ChatAdapter extends BaseAdapter {
    private List<ChatMessage> messages;
    private Context context;
    private String currentUser;

    public ChatAdapter(Context context, List<ChatMessage> messages) {
        this.context = context;
        this.messages = messages;

        // 获取当前用户名
        SharedPreferences sp = context.getSharedPreferences("LOGIN", Context.MODE_PRIVATE);
        this.currentUser = sp.getString("username", "");
    }

    public void setMessages(List<ChatMessage> messages) {
        this.messages = messages;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return messages.size();
    }

    @Override
    public Object getItem(int position) {
        return messages.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder holder;
        ChatMessage message = messages.get(position);

        if (convertView == null) {
            LayoutInflater inflater = LayoutInflater.from(context);

            // 根据消息发送者选择不同的布局
            if (message.getSender().equals(currentUser)) {
                convertView = inflater.inflate(R.layout.item_message_sent, parent, false);
            } else {
                convertView = inflater.inflate(R.layout.item_message_received, parent, false);
            }

            holder = new ViewHolder();
            holder.messageText = convertView.findViewById(R.id.message_text);
            holder.timeText = convertView.findViewById(R.id.time_text);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }

        holder.messageText.setText(message.getContent());

        // 格式化时间
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm", Locale.getDefault());
        String time = sdf.format(new Date(message.getTimestamp()));
        holder.timeText.setText(time);

        return convertView;
    }

    private static class ViewHolder {
        TextView messageText;
        TextView timeText;
    }
}