package com.example.test1;

import java.util.Date;

public class ChatMessage {
    private String messageId;
    private String sender;
    private String receiver;
    private String content;
    private long timestamp;
    private int messageType; // 0-文本, 1-图片, 2-文件

    public ChatMessage() {}

    public ChatMessage(String sender, String receiver, String content) {
        this.sender = sender;
        this.receiver = receiver;
        this.content = content;
        this.timestamp = new Date().getTime();
        this.messageType = 0;
    }

    // Getter和Setter方法
    public String getMessageId() { return messageId; }
    public void setMessageId(String messageId) { this.messageId = messageId; }

    public String getSender() { return sender; }
    public void setSender(String sender) { this.sender = sender; }

    public String getReceiver() { return receiver; }
    public void setReceiver(String receiver) { this.receiver = receiver; }

    public String getContent() { return content; }
    public void setContent(String content) { this.content = content; }

    public long getTimestamp() { return timestamp; }
    public void setTimestamp(long timestamp) { this.timestamp = timestamp; }

    public int getMessageType() { return messageType; }
    public void setMessageType(int messageType) { this.messageType = messageType; }
}