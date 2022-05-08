package picklyfe.registration.Controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import picklyfe.registration.Events.Event;
import picklyfe.registration.Events.EventRepository;
import picklyfe.registration.Profile.UserProfile;
import picklyfe.registration.Profile.UserProfileRepository;
import picklyfe.registration.User.User;
import picklyfe.registration.User.UserRepository;

import javax.annotation.PostConstruct;
import java.util.Properties;

@RestController
public class WelcomeController {
    @Autowired
    UserRepository userRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @GetMapping("/")
    public String welcome() {
        return "Welcome to PickLyfe!";
    }

//    @GetMapping("/{user}")
//    public String welcomeUser(@RequestBody User user) {
//        return "Welcome back to PickLyfe! " + user;
//    }

    @PostConstruct
    String demoData(){
        User[] arr = new User[5];

        User user1 = new User("user1@email.com", "name1", "password1");
        User user2 = new User("user2@email.com", "name2", "password2");
        User user3 = new User("user3@email.com", "name3", "password3");
        User user4 = new User("user4@email.com", "name4", "password4");
        User user5 = new User("user5@email.com", "name5", "password5");
        userRepository.save(user1);
        userRepository.save(user2);
        userRepository.save(user3);
        userRepository.save(user4);
        userRepository.save(user5);

        UserProfile userP1 = userProfileRepository.findById(user1.getUserProfile().getId());
        UserProfile userP2 = userProfileRepository.findById(user2.getUserProfile().getId());
        UserProfile userP3 = userProfileRepository.findById(user3.getUserProfile().getId());
        UserProfile userP4 = userProfileRepository.findById(user4.getUserProfile().getId());
        UserProfile userP5 = userProfileRepository.findById(user5.getUserProfile().getId());
        userP1.setHighScore(15301);
        userP2.setHighScore(6362);
        userP3.setHighScore(9260);
        userP4.setHighScore(13202);
        userP5.setHighScore(7501);

        userP1.setLongestEventSurvived(57);
        userP2.setLongestEventSurvived(73);
        userP3.setLongestEventSurvived(31);
        userP4.setLongestEventSurvived(51);
        userP5.setLongestEventSurvived(27);

        userProfileRepository.save(userP1);
        userProfileRepository.save(userP2);
        userProfileRepository.save(userP3);
        userProfileRepository.save(userP4);
        userProfileRepository.save(userP5);

        return "Successfully created demo event";
    }
}
