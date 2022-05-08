package picklyfe.registration.Game;

import com.fasterxml.jackson.annotation.JsonIgnore;
import picklyfe.registration.Events.Event;
import picklyfe.registration.Perks.Perk;
import picklyfe.registration.Profile.UserProfile;

import javax.persistence.*;
import java.util.List;
import java.util.Random;

@Entity
public class Game {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToOne
    @JsonIgnore
    private UserProfile userProfile;

    @ManyToOne(cascade = CascadeType.ALL)
    //@JoinColumn(name = "id")
    private Event event;

    @OneToOne
    @JoinColumn(name = "Perk_Id")
    private Perk perk;

    private int seed;
    private double multiplier;
    private int difficulty;

    private int status1;    // Money
    private int status2;    // Health
    private int status3;    // Intelligence
    private int status4;    // Relationship
    private int numEventsSurvived;
    private int currScore;
    private Random rand = new Random();


    public Game() {
    }

    public Game(UserProfile userProfile, int difficulty, int seed) {
        this.userProfile = userProfile;
        this.seed = seed;
        this.rand = new Random(seed);
        this.difficulty = difficulty;

        this.status1 = 100;
        this.status2 = 100;
        this.status3 = 100;
        this.status4 = 100;

        switch (difficulty) {
            case 1:
                this.multiplier = 0.8;
                break;
            case 2:
                this.multiplier = 1.0;
                break;
            case 3:
                this.multiplier = 1.2;
                break;
            case 4:
                this.multiplier = 1.5;
                break;
            default:
                this.multiplier = 1.8;
                break;
        }
    }


    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Event getEvent() {
        return event;
    }

    public void setEvent(Event event) {
        this.event = event;
    }

    public long getSeed() {
        return seed;
    }

    public void setSeed(int seed) {
        this.seed = seed;
    }

    public int getStatus1() {
        return status1;
    }

    public void setStatus1(int status1) {
        this.status1 = status1;
    }

    public int getStatus2() {
        return status2;
    }

    public void setStatus2(int status2) {
        this.status2 = status2;
    }

    public int getStatus3() {
        return status3;
    }

    public void setStatus3(int status3) {
        this.status3 = status3;
    }

    public int getStatus4() {
        return status4;
    }

    public void setStatus4(int status4) {
        this.status4 = status4;
    }

    public int getNumEventsSurvived() {
        return numEventsSurvived;
    }

    public void setNumEventsSurvived(int numEventsSurvived) {
        this.numEventsSurvived = numEventsSurvived;
    }

    public int getCurrScore() {
        return currScore;
    }

    public void setCurrScore(int currScore) {
        this.currScore = currScore;
    }

    public int getDifficulty() {
        return difficulty;
    }

    public void setDifficulty(int difficulty) {
        this.difficulty = difficulty;
    }

    public double getMultiplier() {
        return multiplier;
    }

    public void setMultiplier(double multiplier) {
        this.multiplier = multiplier;
    }

    public Random getRand() {
        return rand;
    }

    public void setRand(Random rand) {
        this.rand = rand;
    }

    public Perk getPerk() {
        return perk;
    }

    public void setPerk(Perk perk) {
        this.perk = perk;
    }
}
