package com.example.userprofilescreen.PerkScreen;

public class Perk {

    private long id;
    private int rarity, thumbnail;
    private String perkName, description;

    public Perk(long id, int rarity, int thumbnail, String perkName, String description) {
        this.id = id;
        this.rarity = rarity;
        this.thumbnail = thumbnail;
        this.perkName = perkName;
        this.description = description;
    }

    public Perk(String perkName){
        this.perkName = perkName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getPerkName() {
        return perkName;
    }

    public void setPerkName(String perkName) {
        this.perkName = perkName;
    }

    public int getRarity() {
        return rarity;
    }

    public void setRarity(int rarity) {
        this.rarity = rarity;
    }

    public int getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(int thumbnail) {
        this.thumbnail = thumbnail;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return perkName + " "
                + id + " "
                + rarity + " "
                + description + " ";
    }
}
