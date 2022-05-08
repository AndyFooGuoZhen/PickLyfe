package picklyfe.registration.Settings;

import com.fasterxml.jackson.annotation.JsonIgnore;
import picklyfe.registration.Profile.UserProfile;
import javax.persistence.*;

@Entity
public class Setting {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    private int volume;

    @OneToOne(cascade = CascadeType.ALL)
    @JsonIgnore
    private UserProfile userProfile;

    public Setting(){

    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(int volume) {
        this.volume = volume;
    }


}
