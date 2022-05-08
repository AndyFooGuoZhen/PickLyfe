package com.example.userprofilescreen.UpdateScreen;

public class Patch {
    private String title, description, date;
    private long id;

    public Patch(String title, String description, String date, long id) {
        this.title = title;
        this.description = description;
        this.date = date;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }
}
