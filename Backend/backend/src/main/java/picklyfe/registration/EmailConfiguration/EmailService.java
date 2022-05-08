package picklyfe.registration.EmailConfiguration;

import org.passay.CharacterData;
import org.passay.CharacterRule;
import org.passay.EnglishCharacterData;
import org.passay.PasswordGenerator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import picklyfe.registration.User.User;
import picklyfe.registration.User.UserRepository;

import java.util.Random;

import static org.passay.DictionarySubstringRule.ERROR_CODE;

@Service
public class EmailService {

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private UserRepository userRepository;

    public void sendForgotEmail(String toEmail, String body, String subject){
        SimpleMailMessage message = new SimpleMailMessage();

        message.setFrom("picklyfe@gmail.com");
        message.setTo(toEmail);
        message.setText(body);
        message.setSubject(subject);

        mailSender.send(message);
        System.out.println("Message sent...");
    }

    @GetMapping("/user/forgotpasswordEmail/{email}")
    public String userEmail(@PathVariable String email){
        if (userRepository.findByEmail(email) != null)
            return userRepository.findByEmail(email).getEmail();
        else
            return null;
    }

    CharacterData specialChars = new CharacterData() {
        public String getErrorCode() {
            return ERROR_CODE;
        }

        public String getCharacters() {
            return "!@#$%^&*()_+";
        }
    };

    @GetMapping("/user/forgotpasswordBody/{email}")
    public String userBody(@PathVariable String email){
        Random rand = new Random();
        PasswordGenerator gen = new PasswordGenerator();
        CharacterData lowerCaseChars = EnglishCharacterData.LowerCase;
        CharacterRule lowerCaseRule = new CharacterRule(lowerCaseChars);
        lowerCaseRule.setNumberOfCharacters(2);

        CharacterData upperCaseChars = EnglishCharacterData.UpperCase;
        CharacterRule upperCaseRule = new CharacterRule(upperCaseChars);
        upperCaseRule.setNumberOfCharacters(2);

        CharacterData digitChars = EnglishCharacterData.Digit;
        CharacterRule digitRule = new CharacterRule(digitChars);
        digitRule.setNumberOfCharacters(2);


        CharacterRule splCharRule = new CharacterRule(specialChars);
        splCharRule.setNumberOfCharacters(2);
        String number = gen.generatePassword(10, splCharRule, lowerCaseRule, upperCaseRule, digitRule);
        User user = userRepository.findByEmail(email);
        user.setPassword(number);
        userRepository.save(user);
        String message = "Please do not share this code with other people and enter it into the text box\n\n " + number;
        if (userRepository.findByEmail(email) != null)
            return message;
        else
            return null;
    }

    @GetMapping("/user/forgotpasswordEmail/{email}")
    public String userSubject(@PathVariable String email){
        String message = "Forgot Password";
        if (userRepository.findByEmail(email) != null)
            return message;
        else
            return null;
    }
}
