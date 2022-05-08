package picklyfe.registration.Profile;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.apache.tomcat.jni.Local;
import picklyfe.registration.Friends.Friend;
import picklyfe.registration.Game.Game;
import picklyfe.registration.Perks.Perk;
import picklyfe.registration.Perks.PerkRepository;
import picklyfe.registration.Settings.Setting;
import picklyfe.registration.User.User;
import picklyfe.registration.User.UserRepository;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
public class UserProfile {
    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private User user;

    @OneToOne
    @JoinColumn(name = "game")
    private Game game;

    @OneToMany
    @JoinColumn(name = "perkID")
    private List<Perk> perk;



    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "settingID")
    private Setting setting;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @Column(length = 45, nullable = false, unique = true)
    private String userName;
    private LocalDateTime firstJoined;
    private LocalDateTime lastLogin;

    private String profilePicture;
    private String profilePictureType;
    @Column(length = 99999999)
    private String profilePictureBytes = "";

    @Lob
    private byte[] fileData;
    private LocalDateTime firstLogin;
    private long highScore;
    private long numGamesPlayed;
    private long longestEventSurvived;
    private double hoursPlayed;
    private long equippedPerk;
    @Column(length = 200, nullable = true)
    private String aboutMe;

    public UserProfile() {
    }

    public UserProfile(String userName, LocalDateTime firstJoined){
        this.userName = userName;
        this.firstLogin = LocalDateTime.now();
        this.firstJoined = firstJoined;
        this.setting = new Setting();
        this.profilePictureBytes =  "data:image/jpeg;base64,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";
    }

    public User getUser(){
        return user;
    }

    public void setUser(User user){
        this.user = user;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProfilePicture() {
        return profilePicture;
    }

    public void setProfilePicture(String profilePicture) {
        this.profilePicture = profilePicture;
    }

    public String getProfilePictureType() {
        return profilePictureType;
    }

    public void setProfilePictureType(String profilePictureType) {
        this.profilePictureType = profilePictureType;
    }

    public byte[] getFileData() {
        return fileData;
    }

    public void setFileData(byte[] fileData) {
        this.fileData = fileData;
    }

    public LocalDateTime getFirstJoined() {
        return firstJoined;
    }

    public void setFirstJoined(LocalDateTime firstJoined) {
        this.firstJoined = firstJoined;
    }

    public LocalDateTime getFirstLogin() {
        return firstLogin;
    }

    public void setFirstLogin(LocalDateTime firstLogin) {
        this.firstLogin = firstLogin;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public long getHighScore() {
        return highScore;
    }

    public void setHighScore(long highScore) {
        this.highScore = highScore;
    }

    public long getLongestEventSurvived() {
        return longestEventSurvived;
    }

    public void setLongestEventSurvived(long longestEventSurvived) {
        this.longestEventSurvived = longestEventSurvived;
    }

    public double getHoursPlayed() {
        return Math.round(hoursPlayed);
    }

    public void setHoursPlayed(double hoursPlayed) {
        this.hoursPlayed = hoursPlayed;
    }

    public String getAboutMe() {
        return aboutMe;
    }

    public void setAboutMe(String aboutMe) {
        this.aboutMe = aboutMe;
    }

    public long getNumGamesPlayed() {
        return numGamesPlayed;
    }

    public void setNumGamesPlayed(long numGamesPlayed) {
        this.numGamesPlayed = numGamesPlayed;
    }

    public List<Perk> getPerk(){
        return perk;
    }

    public void addPerk(Perk newPerk){
        this.perk.add(newPerk);
    }

    public void deletePerk(Perk deletePerk) {
        this.perk.remove(deletePerk);
    }

    public long getEquippedPerk(){
        return equippedPerk;
    }

    public void setEquippedPerk(long newPerk){
        this.equippedPerk = newPerk;
    }

    public Setting getSetting() {
        return setting;
    }

    public void setSetting(Setting setting) {
        this.setting = setting;
    }

    public String getProfilePictureBytes() {
        return profilePictureBytes;
    }

    public void setProfilePictureBytes(String profilePictureBytes) {
        this.profilePictureBytes = profilePictureBytes;
    }

    @Override
    public String toString() {
        return userName + " "
                + id + " "
                + profilePicture + " "
                + profilePictureType + " "
                + lastLogin + " "
                + highScore + " "
                + longestEventSurvived + " "
                + hoursPlayed + " "
                + aboutMe + " ";
    }
}
