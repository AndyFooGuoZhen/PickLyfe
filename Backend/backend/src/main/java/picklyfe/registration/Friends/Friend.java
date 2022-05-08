package picklyfe.registration.Friends;


import com.fasterxml.jackson.annotation.JsonIgnore;
import picklyfe.registration.Profile.UserProfile;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Friend {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @OneToOne
    @JsonIgnore
    private Friend friends;

    @Column(name = "created_date")
    private Date createdDate;

    @OneToOne
    @JoinColumn(name = "first_user_id", referencedColumnName = "id")
    UserProfile firstUser;

    @OneToOne
    @JoinColumn(name = "second_user_id", referencedColumnName = "id")
    UserProfile secondUser;

    public Friend() {
    }

    public Friend(UserProfile firstUser, UserProfile secondUser) {
        this.firstUser = firstUser;
        this.secondUser = secondUser;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Date getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(Date createdDate) {
        this.createdDate = createdDate;
    }

    public UserProfile getFirstUser() {
        return firstUser;
    }

    public void setFirstUser(UserProfile firstUser) {
        this.firstUser = firstUser;
    }

    public UserProfile getSecondUser() {
        return secondUser;
    }

    public void setSecondUser(UserProfile secondUser) {
        this.secondUser = secondUser;
    }
}
