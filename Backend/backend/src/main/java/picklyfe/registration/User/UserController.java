package picklyfe.registration.User;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import picklyfe.registration.EmailConfiguration.EmailService;
import picklyfe.registration.Profile.UserProfile;
import picklyfe.registration.Profile.UserProfileRepository;

import javax.annotation.PostConstruct;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserController {

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    EmailService emailService;

    private String cause = "";

    @GetMapping("/user/all")
    List<User> getAllUsers() {
        return userRepository.findAll();
    }

    @GetMapping("/user/{id}")
    User getUserById(@PathVariable long id){
        if (userRepository.findById(id) != null)
            return userRepository.findById(id);
        else
            return null;
    }

    @GetMapping("user/name/{name}")
    User getUserByName(@PathVariable String name) {
        if (userRepository.findByUserName(name) != null)
            return userRepository.findByUserName(name);
        else
            return null;
    }

    @GetMapping("/user/login/{nameOrEmail}/{password}")
    String loginUser(@PathVariable String nameOrEmail, @PathVariable String password) {
        User user;

        if (userRepository.findByUserName(nameOrEmail) != null)
            user = userRepository.findByUserName(nameOrEmail);
        else if (userRepository.findByEmail(nameOrEmail) != null)
            user = userRepository.findByEmail(nameOrEmail);
        else
            return "Login failure, username or email does not exist. Please try again!";

        if (user.getPassword().equals(password))
            return "Login success! Welcome " + user.getUserName();
        else
            return "Login failure, password is incorrect. Please try again!";
    }

    @GetMapping("/user/forgotpassword/{email}")
    String triggerMail(@PathVariable String email){
        emailService.sendForgotEmail(emailService.userEmail(email),
                                    emailService.userBody(email),
                                    emailService.userSubject(email));
        User user = userRepository.findByEmail(email);
        user.setPasswordStatus(1);
        userRepository.save(user);
        return "Mailing successful";
    }

    @PostMapping("/user/post")
    String postUserByObj(@RequestBody User user) {
        if (user != null && userRepository.findByUserName(user.getUserName()) == null && userRepository.findByEmail(user.getEmail()) == null) {
            userRepository.save(user);
            return "Successful creating user: " + user.getUserName();
        } else {
            return "Failed to create user";
        }
    }

    @PostMapping("/user/post/{email}/{userName}/{password}")
    String postUserByRegister(@PathVariable String email, @PathVariable String userName, @PathVariable String password) {
        if (userRepository.findByUserName(userName) == null && userRepository.findByEmail(email) == null) {
            User user = new User(email, userName, password);
            userRepository.save(user);
            return "Successful creating user: " + user.getUserName();
        } else {
            return "Failed to create user";
        }
    }

    @PutMapping("/user/updateJson/{userName}")
    String updateUserProfileJSON(@RequestBody UserProfile userP, @PathVariable String userName){
        if (userRepository.findByUserName(userName) == null) {
            return "Failed to find user";
        } else{
            User user = userRepository.findByUserName(userName);
            user.setUserProfile(userP);
            userRepository.save(user);
            return "Successful editing userProfile";
        }
    }

    @PutMapping("/user/updateName/{id}/{newName}")
    String updateUserName(@PathVariable long id, @PathVariable String newName) {
        User user = null;
        if (userRepository.findByUserName(newName) == null && userRepository.findById(id) != null) {
            user = userRepository.findById(id);
            user.setUserName(newName);
            userRepository.save(user);
            return "Successful updated username to: " + user.getUserName();
        }
        else {
            return "Failed to update user.";
        }
    }

    @PutMapping("/user/updateEmail/{id}/{email}")
    String updateUserEmail(@PathVariable long id, @PathVariable String email) {
        User user = null;
        if (userRepository.findByEmail(email) == null && userRepository.findById(id) != null) {
            user = userRepository.findById(id);
            user.setEmail(email);
            userRepository.save(user);
            return "Successful updated email to: " + userRepository.findById(id).getEmail();
        } else {
            return "Failed to update email.";
        }
    }

    @PutMapping("/user/updateLogin/{id}/{lastLogin}")
    String updateUserLastLogin(@PathVariable long id, @PathVariable LocalDateTime lastLogin) {
        User user = null;
        if (userRepository.findById(id) != null) {
            user = userRepository.findById(id);
            user.setLastLogin(lastLogin);
            userRepository.save(user);
            return "Successful updated last login to: " + userRepository.findById(id).getLastLogin();
        } else {
            return "Failed to update user.";
        }
    }

    @PutMapping("/user/updatePassword/{id}/{password}/{confirmPassword}")
    String updateUserPassword(@PathVariable long id, @PathVariable String password, @PathVariable String confirmPassword) {
        User user = null;
        if (userRepository.findById(id) != null){
            if (password.equals(confirmPassword)) {
                user = userRepository.findById(id);
                user.setPassword(confirmPassword);
                user.setPasswordStatus(0);
                userRepository.save(user);
                return "Successful updated user password";
            }
            else
                cause = "Both password are not the same.";
        } else {
            cause = "Invalid ID.";
        }
        return "Failed to update user. " + cause + " Please try again.";
    }

    @PutMapping("/user/updateRole/{id}/{newRole}")
    String updateUserRole(@PathVariable long id, @PathVariable String newRole){
        User user = null;
        if (userRepository.findById(id) != null){
            user = userRepository.findById(id);
            user.setRole(newRole);
            userRepository.save(user);
            return "Successful updated user role";
        } else {
            cause = "Invalid ID";
        }
        return "Failed to update user. " + cause + " Please try again.";
    }

    @DeleteMapping("/user/delete/{userName}/{password}")
    String deleteUserByNamePass(@PathVariable String userName, @PathVariable String password) {
        if (userRepository.findByUserName(userName) != null) {
            User user = userRepository.findByUserName(userName);
            if (user.getPassword().equals(password)) {
                userRepository.delete(user);
                return "Successful deleted user";
            } else {
                cause = "Incorrect password.";
            }
        } else
            cause = "User does not exist.";
        return "Failed to delete user, " + cause + " Please try again.";
    }

    @DeleteMapping("/user/deleteAll")
    String deleteAllUser() {
        //userProfileRepository.deleteAll();
        userRepository.deleteAll();
        return "Successfully deleted all users.";
    }

    @GetMapping("/user/demo")
    String demoData(){
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
        return "Successfully created demo users";
    }

    void deleteUserWithoutUserProfile(@PathVariable long id){
        User user = userRepository.findById(id);
        user.setUserProfile(null);
        userRepository.delete(user);
    }
}
