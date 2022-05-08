package picklyfe.registration.Friends;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import picklyfe.registration.Profile.UserProfile;
import picklyfe.registration.Profile.UserProfileRepository;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@RestController
public class FriendController {

    @Autowired
    FriendRepository friendRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

//    @Autowired
//    ModelMapper modelMapper;
//
//    @Autowired
//    SecurityService securityService;

    @PutMapping("/user/add/{userName}")
    public String saveFriend(@PathVariable String userName, @RequestBody UserProfile user) throws NullPointerException {
        UserProfile firstUser = null, secondUser = null;

        if (user == null) {
            return "Unable to find User1, please try again!";
        }
        if (userName != null && userProfileRepository.findByUserName(userName) != null) {
            UserProfile user2 = userProfileRepository.findByUserName(userName);

            if (user.getId() > user2.getId()) {
                firstUser = user2;
                secondUser = user;
            } else {
                firstUser = user;
                secondUser = user2;
            }

            if (!(friendRepository.existsByFirstUserAndSecondUser(firstUser, secondUser))) {
                Friend friend = new Friend(firstUser, secondUser);
                friend.setCreatedDate(new Date());
//                friend.setFirstUser(firstUser);
//                friend.setSecondUser(secondUser);
                friendRepository.save(friend);
                return "Successfully added friend";
            }
        }
        return "Unable to find User2, please try again!";
    }

        @GetMapping("/user/friendList/{id}")
        public List<UserProfile> getFriends(@PathVariable long id) {
        UserProfile currentUser = userProfileRepository.findById(id);
        List<Friend> friendsByFirstUser = friendRepository.findByFirstUser(currentUser);
        List<Friend> friendsBySecondUser = friendRepository.findBySecondUser(currentUser);
        List<UserProfile> friendUsers = new ArrayList<>();

        /*
            suppose there are 3 users with id 1,2,3.
            if user1 add user2 as friend database record will be first user = user1 second user = user2
            if user3 add user2 as friend database record will be first user = user2 second user = user3
            it is because of lexicographical order
            while calling get friends of user 2 we need to check as a both first user and the second user
         */
        for (Friend friend : friendsByFirstUser) {
            friendUsers.add(userProfileRepository.findById(friend.getSecondUser().getId()));
        }
        for (Friend friend : friendsBySecondUser) {
            friendUsers.add(userProfileRepository.findById(friend.getFirstUser().getId()));
        }
        return friendUsers;
    }
}