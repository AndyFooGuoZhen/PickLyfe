package com.example.userprofilescreen.Friends;

public class Friend {
    public String userName;
    public String gamesPlayed;

    public Friend(String userName, String gamesPlayed) {
        this.userName = userName;
        this.gamesPlayed = gamesPlayed;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getgamesPlayed() {
        return gamesPlayed;
    }

    public void setgamesPlayed(String gamesPlayed) {
        this.gamesPlayed = gamesPlayed;
    }
}




