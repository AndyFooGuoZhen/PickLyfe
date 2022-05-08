package com.example.userprofilescreen.UpdateScreen;

public class Announcement {
    private String title, date, description;
    private long id;

    public Announcement(String title, String date, String description, long id) {
        this.title = title;
        this.date = date;
        this.description = description;
        this.id = id;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
