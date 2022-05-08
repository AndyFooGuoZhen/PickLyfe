package picklyfe.registration.User;

import com.fasterxml.jackson.annotation.JsonIgnore;
import picklyfe.registration.Friends.Friend;
import picklyfe.registration.Profile.UserProfile;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private String userName;
    private String password;
    private String email;
    private LocalDateTime firstJoined;
    private LocalDateTime lastLogin;
    private String role;
    private int passwordStatus;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "userProfile_ID")
    private UserProfile userProfile;

    @OneToOne
    @JsonIgnore
    private Friend friends;

    public User() {
    }

    public User(String email, String userName, String password) {
        this.userName = userName;
        this.password = password;
        this.email = email;
        this.firstJoined = LocalDateTime.now();
        this.userProfile = new UserProfile(userName, firstJoined);
        this.role = "user";
        this.passwordStatus = 0;
    }

    public long getId() {
        return id;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public LocalDateTime getLastLogin() {
        return lastLogin;
    }

    public void setLastLogin(LocalDateTime lastLogin) {
        this.lastLogin = lastLogin;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public LocalDateTime getFirstJoined() {
        return firstJoined;
    }

    public void setFirstJoined(LocalDateTime firstJoined) {
        this.firstJoined = firstJoined;
    }

    public String getRole(){
        return role;
    }

    public void setRole(String role){
        this.role = role;
    }

    public int getPasswordStatus(){
        return passwordStatus;
    }

    public void setPasswordStatus(int passwordStatus){
        this.passwordStatus = passwordStatus;
    }

    public UserProfile getUserProfile() {
        return userProfile;
    }

    public void setUserProfile(UserProfile userProfile) {
        this.userProfile = userProfile;
    }
}
