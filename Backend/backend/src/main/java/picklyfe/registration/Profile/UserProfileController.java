package picklyfe.registration.Profile;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picklyfe.registration.Perks.Perk;
import picklyfe.registration.Perks.PerkRepository;
import picklyfe.registration.User.UserRepository;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.sql.Blob;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

@RestController
public class UserProfileController {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PerkRepository perkRepository;

    private String failure = "{\"message\":\"failure\"}";

    @GetMapping("/userprofile/{id}")
    UserProfile getUserProfile(@PathVariable long id) {
        if (userProfileRepository.findById(id) != null) {
            getUserHoursPlayed(id);
            return userRepository.findById(id).getUserProfile();
        } else
            return null;
    }

    //testpurpose only
    @PostMapping("/userprofile/post/{name}")
    String postUserProfile(@PathVariable String name) {
        if (userProfileRepository.findByUserName(name) == null) {
            UserProfile userP = new UserProfile(name, LocalDateTime.now());
            userProfileRepository.save(userP);
            return "Successful creating user: " + name;
        }else {
        return failure;
        }
    }

    //requesting json
    @PostMapping("/userprofile/post")
    String postUserProfile(@RequestBody UserProfile userP){
        userProfileRepository.save(userP);
        return "Successful creating userProfile ";
    }

    @PutMapping("/userprofile/put")
    String putUserProfile(@RequestBody UserProfile userP){
        userProfileRepository.save(userP);
        return "Successful creating userProfile ";
    }

    //admin uses
    @PutMapping("/userprofile/{id}/name/{newName}")
    String putUserName(@PathVariable long id, @PathVariable String newName){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return failure;
        userP.setUserName(newName);
        userProfileRepository.save(userP);
        return "newName edited to " + newName;
    }

    @PutMapping("/userprofile/{id}/perkID/{perkId}")
    String equipUserPerkID(@PathVariable long id, @PathVariable long perkId){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        List <Perk> perk = userRepository.findById(id).getUserProfile().getPerk();
        if(userP == null || perk == null)
            return failure;
        if(perk.contains(perkRepository.findById(perkId))){
            userP.setEquippedPerk(perkId);
        }
        userProfileRepository.save(userP);
        return "User has equipped " + perkRepository.findById(perkId).getPerkName();
    }

//    @PutMapping("userprofile/{id}/perkName/{perkName}")
//    String equipUserPerkName(@PathVariable long id, @PathVariable String perkName){
//        UserProfile userP = userRepository.findById(id).getUserProfile();
//        List <Perk> perk = userRepository.findById(id).getUserProfile().getPerk();
//        if(userP == null || perk == null)
//            return failure;
//        if(perk.contains(perkRepository.findByPerkName(perkName))){
//            userP.setEquippedPerk(perkRepository.findByPerkName(perkName));
//        }
//        userProfileRepository.save(userP);
//        return "User has equipped " + perkRepository.findByPerkName(perkName).getPerkName();
//    }

    @PutMapping("/userprofile/{id}/perk/{perkID}")
    String setUserPerk(@PathVariable long id, @PathVariable long perkID){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        List <Perk> perk = userRepository.findById(id).getUserProfile().getPerk();
        if(userP == null)
            return failure;
        if(!perk.contains(perkRepository.findById(perkID))) {
            userP.addPerk(perkRepository.findById(perkID));
        } else {
            return "User already has this perk";
        }
        userProfileRepository.save(userP);
        return "User was given " + perkRepository.findById(perkID).getPerkName();
    }

    //edit method later
    @GetMapping("/userprofile/getImg/{id}")
    byte[] getImg(@PathVariable long id){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return null;
        //userP.setProfilePicture("https://i.guim.co.uk/img/media/327e46c3ab049358fad80575146be9e0e65686e7/0_0_1023_742/master/1023.jpg?width=700&quality=85&auto=format&fit=max&s=3d74c30c02485d03b0166f4908ddaa35");
        userProfileRepository.save(userP);
        return userProfileRepository.findById(id).getFileData();
    }

    //admin uses
    @PutMapping("/userprofile/upload/{id}")
    String uploadDb(@RequestParam("file") MultipartFile multipartFile, @PathVariable long id) throws Exception{
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return failure;

        try {
            userP.setFileData(multipartFile.getBytes());
            userP.setProfilePictureType(multipartFile.getContentType());
            userP.setProfilePicture(multipartFile.getOriginalFilename());
        }catch(IOException e) {
            e.printStackTrace();
        }
        userProfileRepository.save(userP);
        return "profilePicture successfully edited";
    }

    @PutMapping("/userprofile/upload/test/{id}")
    String testImageUpload(@RequestParam("pic") String imageByteArr, @PathVariable long id) {
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if (userP == null) {
            return "User Doesn't Exist";
        }
        userP.setProfilePictureBytes(imageByteArr);
        userProfileRepository.save(userP);

        return "Successfully uploaded image";
    }

    //admin uses
    @PutMapping("/userprofile/{id}/hs/{highScore}")
    String putUserHighScore(@PathVariable long id, @PathVariable long highScore){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return failure;
        userP.setHighScore(highScore);
        userProfileRepository.save(userP);
        return "highScore edited to " + highScore;
    }

    @PutMapping("/userprofile/{id}/les/{longestEventSurvived}")
    String putUserLongestEventSurvived(@PathVariable long id, @PathVariable long longestEventSurvived){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return failure;
        userP.setLongestEventSurvived(longestEventSurvived);
        userProfileRepository.save(userP);
        return "LongestEventSurvived edited to " + longestEventSurvived;
    }

    @PutMapping("/userprofile/{id}/am/{aboutMe}")
    String putUserAboutMe(@PathVariable long id, @PathVariable String aboutMe){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return failure;
        userP.setAboutMe(aboutMe);
        userProfileRepository.save(userP);
        return "aboutMe edited to " + aboutMe;
    }

    //admin uses
    @PutMapping("/userprofile/{id}/hp/{hoursPlayed}")
    String putUserHoursPlayed(@PathVariable long id, @PathVariable double hoursPlayed){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return failure;
        userP.setHoursPlayed(hoursPlayed);
        userProfileRepository.save(userP);
        return "hoursPlayed edited to " + hoursPlayed;
    }

    //testpurpose only
    @GetMapping("/userprofile/timeplayed/{id}")
    String getUserHoursPlayed(@PathVariable long id){
        UserProfile userP = userRepository.findById(id).getUserProfile();
        if(userP == null)
            return failure;

        userP.setLastLogin(LocalDateTime.now());
        LocalDateTime to = userP.getLastLogin();
        LocalDateTime from = userP.getFirstLogin();

        Duration duration = Duration.between(from, to);
        double minutesPlayed = (double) duration.toMinutes();
        double hoursPlayed = minutesPlayed / 60.0;

        if(userP.getHoursPlayed() != 0){
            userP.setHoursPlayed(userP.getHoursPlayed() + hoursPlayed);
        }
        else {
            userP.setHoursPlayed(hoursPlayed);
        }
        userProfileRepository.save(userP);
        return "hoursPlayed edited to " + String.format("%.1f", hoursPlayed);
    }

    //score leaderboard
    @GetMapping("/userprofile/leaderboard/score")
    List<UserProfile> getSortedLeaderboardScore(){
        return userProfileRepository.findAll(Sort.by(Sort.Direction.DESC, "highScore"));
    }

    //event leaderboard
    @GetMapping("/userprofile/leaderboard/event")
    List<UserProfile> getSortedLeaderboardEvents(){
        return userProfileRepository.findAll(Sort.by(Sort.Direction.DESC, "longestEventSurvived"));
    }

    //all
    @GetMapping("/userprofile/all")
    List<UserProfile> getAll(){
        return userProfileRepository.findAll();
    }

    //admin uses
    @DeleteMapping("/userprofile/delete/{name}")
    String deleteUser(@PathVariable String name){
        UserProfile userP = userProfileRepository.findByUserName(name);
        if(userP == null)
            return failure;
        userProfileRepository.deleteByUserName(userP.getUserName());
        userProfileRepository.save(userP);
        return "profile " + name + " has been successfully deleted";
    }

    @DeleteMapping("/userprofile/perk/delete/{id}/{perkID}")
    String deleteUserPerk(@PathVariable long id, @PathVariable long perkID){
        UserProfile userP = userProfileRepository.findById(id);
        Perk perk = perkRepository.findById(perkID);
        if(userP == null || perk == null)
            return failure;
        if(userP.getPerk().contains(perk)) {
            userProfileRepository.findById(id).deletePerk(perk);
            userProfileRepository.save(userP);
        } else {
            return "User does not have perk with id " + perkID;
        }
        return "User " + id + " perk's has been successfully deleted";
    }

    @GetMapping("/userprofile/demo/score")
    String demoScore(){
        UserProfile userP1 = userProfileRepository.findByUserName("name1");
        UserProfile userP2 = userProfileRepository.findByUserName("name2");
        UserProfile userP3 = userProfileRepository.findByUserName("name3");
        UserProfile userP4 = userProfileRepository.findByUserName("name4");
        UserProfile userP5 = userProfileRepository.findByUserName("name5");
        userP1.setHighScore(15301);
        userP2.setHighScore(6362);
        userP3.setHighScore(9260);
        userP4.setHighScore(13202);
        userP5.setHighScore(7501);

        userProfileRepository.save(userP1);
        userProfileRepository.save(userP2);
        userProfileRepository.save(userP3);
        userProfileRepository.save(userP4);
        userProfileRepository.save(userP5);
        return "Successfully updated demo user's highscore";
    }

    @PostConstruct
    @GetMapping("/userprofile/demo/event")
    String demoEvent(){
        UserProfile userP1 = userProfileRepository.findByUserName("name1");
        UserProfile userP2 = userProfileRepository.findByUserName("name2");
        UserProfile userP3 = userProfileRepository.findByUserName("name3");
        UserProfile userP4 = userProfileRepository.findByUserName("name4");
        UserProfile userP5 = userProfileRepository.findByUserName("name5");

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
        return "Successfully updated demo user's num event survived";
    }
}
