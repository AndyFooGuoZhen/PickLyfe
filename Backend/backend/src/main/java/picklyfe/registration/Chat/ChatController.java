package picklyfe.registration.Chat;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Scanner;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import picklyfe.registration.Profile.UserProfile;
import picklyfe.registration.Profile.UserProfileRepository;
import picklyfe.registration.User.User;

@RestController      // this is needed for this to be an endpoint to springboot
@ServerEndpoint(value = "/chat/{userName}")  // this is Websocket url
public class ChatController {

    private static MessageRepository msgRepo;

    private static UserProfileRepository userProfileRepository;

    @Autowired
    public void setMessageRepository(MessageRepository repo) {
        msgRepo = repo;  // we are setting the static variable
    }

    @Autowired
    public void setUserProfileRepository(UserProfileRepository repo) {
        userProfileRepository = repo;
    }

    // Might have to change to MySQL
    private static Map<Session, String> sessionUserProfileMap = new Hashtable<>();
    private static Map<String, Session> userProfileSessionMap = new Hashtable<>();

    private final Logger logger = LoggerFactory.getLogger(ChatController.class);

    @OnOpen
    public void onOpen(Session session, @PathParam("userName") String userName) throws IOException {
        String message = "";
        if (userProfileRepository.findByUserName(userName) != null) {
//            UserProfile userProfile = userProfileRepository.findByUserName(userName);

            logger.info("Entered into Open");

            // store connecting user information
            sessionUserProfileMap.put(session, userName);
            userProfileSessionMap.put(userName, session);

            //Send chat history to the newly connected user
            sendMessageToParticularUser(userName, getChatHistory());

            // broadcast that new user joined
            message = "User:" + userName + " has Joined the Chat";
            broadcast(message);
        } else {
            message = "User: " + userName + "does not exist!";
            broadcast(message);
        }
    }


    @OnMessage
    public void onMessage(Session session, String message) throws IOException {

        // Handle new messages
        logger.info("Entered into Message: Got Message:" + message);
        String userName = sessionUserProfileMap.get(session);

        // Direct message to a user using the format "@username <message>"
        if (message.startsWith("@")) {
            String destUsername = message.split(" ")[0].substring(1);
            // send the message to the sender and receiver
            if (destUsername != null && userProfileRepository.findByUserName(userName) != null && userProfileRepository.findByUserName(destUsername) != null) {
                sendMessageToParticularUser(destUsername, "[DM] " + userName + ": " + message);
                sendMessageToParticularUser(userName, "[DM] " + userName + ": " + message);
                msgRepo.save(new Message(userName, destUsername, message, MessageType.DIRECT_MESSAGE));
                return;
            }
        }
        else { // broadcast
            broadcast(userName + ": " + message);
        }

        // Saving chat history to repository
        msgRepo.save(new Message(userName, message, MessageType.GLOBAL_MESSAGE));
    }


    @OnClose
    public void onClose(Session session) throws IOException {
        logger.info("Entered into Close");

        // remove the user connection information
        String username = sessionUserProfileMap.get(session);
        sessionUserProfileMap.remove(session);
        userProfileSessionMap.remove(username);

        // broadcase that the user disconnected
        String message = username + " disconnected";
        broadcast(message);
    }

    @OnError
    public void onError(Session session, Throwable throwable) {
        // Do error handling here
        logger.info("Entered into Error");
        throwable.printStackTrace();
    }


    private void sendMessageToParticularUser(String userName, String message) {
        try {
            if (userProfileSessionMap.get(userName) != null){
                userProfileSessionMap.get(userName).getBasicRemote().sendText(message);
            }
        }
        catch (IOException e) {
            logger.info("Exception: " + e.getMessage().toString());
            e.printStackTrace();
        }
    }


    private void broadcast(String message) {
        sessionUserProfileMap.forEach((session, userProfile) -> {
            try {
                session.getBasicRemote().sendText(message);
            }
            catch (IOException e) {
                logger.info("Exception: " + e.getMessage().toString());
                e.printStackTrace();
            }

        });

    }


    // Gets the Chat history from the repository
    private String getChatHistory() {
        List<Message> messages = msgRepo.findAll();

        // convert the list to a string
        StringBuilder sb = new StringBuilder();
        if(messages != null && messages.size() != 0) {
            for (Message message : messages) {
                if (message.getMsgType() != MessageType.DIRECT_MESSAGE)
                    sb.append(message.getUserName() + ": " + message.getContent() + "\n");
            }
        }
        return sb.toString();
    }
} // end of Class