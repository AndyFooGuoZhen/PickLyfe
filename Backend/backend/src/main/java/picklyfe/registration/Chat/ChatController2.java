package picklyfe.registration.Chat;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import picklyfe.registration.Profile.UserProfile;
import picklyfe.registration.Profile.UserProfileRepository;

import java.util.List;

@RestController
public class ChatController2 {

    @Autowired
    MessageRepository msgRepo;

    @Autowired
    UserProfileRepository userProfileRepository;

    @GetMapping("/chat/getDirectMessageHistory/{user1}/{user2}")
    String getDirectChatHistory(@PathVariable String user1, @PathVariable String user2) {
        UserProfile userProfile = userProfileRepository.findByUserName(user1);
        UserProfile otherUser = userProfileRepository.findByUserName(user2);

        if (userProfile != null && userProfileRepository.findAll().contains(userProfile)
                && otherUser != null && userProfileRepository.findAll().contains(otherUser)) {
            return getDMHistory(userProfile, otherUser);
        }
        else {
            if (userProfile == null)
                System.out.println("UserProfile is NULL");
            if (otherUser == null)
                System.out.println("OtherUser is NULL");
            if (userProfile != null && !userProfileRepository.findAll().contains(userProfile))
                System.out.println("User:" + userProfile.getUserName() + "not found!");
            if (otherUser != null && !userProfileRepository.findAll().contains(otherUser))
                System.out.println("User:" + otherUser.getUserName() + "not found!");
        }
        return null;
    }

    private String getDMHistory(UserProfile userProfile, UserProfile otherUser) {
        List<Message> directMessages = msgRepo.findMessagesByMessageType(MessageType.DIRECT_MESSAGE);

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        for (Message message : directMessages){
            if ((message.getUserName().equals(userProfile.getUserName()) && message.getToWhom().equals(otherUser.getUserName()))
                    || (message.getUserName().equals(otherUser.getUserName()) && message.getToWhom().equals(userProfile.getUserName())))
                sb.append("[DM] " + message.getUserName() + ": " + message.getContent() + "\n");

//                for (Message checkMsg : user2_messages) {
//                    if (checkMsg.getMsgType() == MessageType.DIRECT_MESSAGE && checkMsg.getContent().contains(message.getContent()))
//                        sb.append(message.getUserName() + "[DM] " + message.getUserName() + ": " + message.getContent() + "\n");
//                }
        }
        return sb.toString();
    }
}
