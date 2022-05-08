package picklyfe.registration.Settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RestController;
import picklyfe.registration.Profile.UserProfileRepository;

@RestController
public class SettingController {

    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    SettingRepository settingRepository;

    @GetMapping("/setting/{id}")
    Setting getSetting(@PathVariable long id) {
        Setting setting = userProfileRepository.findById(id).getSetting();
        if (setting != null) {
            return setting;
        } else {
            return null;
        }
    }

    @GetMapping("/faq")
    String FAQ(){
        return "How to play this game?\n " +
                "Choose between two options. This will change your resources at the top of your screen. " +
                "Try to hit 200 total points to win the game! Anything below 0 will result in a game end!";
    }

    @PutMapping("/setting/volume/{id}/{volume}")
    String setUserVolume(@PathVariable long id, @PathVariable int volume){
        Setting setting = userProfileRepository.findById(id).getSetting();
        if (setting != null) {
            setting.setVolume(volume);
        } else
            return "Unable to change volume length";
        settingRepository.save(setting);
        return "Volume setting successfully changed to " + volume;
    }

}
