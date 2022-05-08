package picklyfe.registration.Perks;

import com.fasterxml.jackson.annotation.JsonIgnore;
import picklyfe.registration.Profile.UserProfile;

import javax.persistence.*;

@Entity
public class Perk {

    @ManyToOne
    @JsonIgnore
    private UserProfile userProfile;

    @OneToOne
    @JsonIgnore
    private UserProfile singleUserProfile;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long perk_id;
    private String perkName;
    private int rarity;
    private String description;
    private double scoreMultiplier;
    private double statusMultiplier;
    private int ignoreDeath;
    private int Revive;

    public Perk(){

    }

    public Perk(String perkName){
        this.perkName = perkName;
    }

    public long getId() {
        return perk_id;
    }

    public void setId(long perk_id) {
        this.perk_id = perk_id;
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

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public double getScoreMultiplier() {
        return scoreMultiplier;
    }

    public void setScoreMultiplier(double scoreMultiplier) {
        this.scoreMultiplier = scoreMultiplier;
    }

    public double getStatusMultiplier() {
        return statusMultiplier;
    }

    public void setStatusMultiplier(double statusMultiplier) {
        this.statusMultiplier = statusMultiplier;
    }

    public int getRevive() {
        return Revive;
    }

    public void setRevive(int revive) {
        Revive = revive;
    }

    public int getIgnoreDeath() {
        return ignoreDeath;
    }

    public void setIgnoreDeath(int ignoreDeath) {
        this.ignoreDeath = ignoreDeath;
    }

    @Override
    public String toString() {
        return perkName + " "
                + perk_id + " "
                + rarity + " "
                + description + " ";
    }
}
