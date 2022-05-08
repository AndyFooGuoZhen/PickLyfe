package com.example.userprofilescreen.Chat;

public class Chat {
    public String userName;
    public String message;

    public Chat(String userName, String msg) {
        this.userName = userName;
        this.message = msg;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String msg) {
        this.message = msg;
    }
}
