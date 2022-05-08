package picklyfe.registration.Perks;

import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.common.record.Record;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picklyfe.registration.Events.EventRepository;
import picklyfe.registration.Game.GameRepository;
import picklyfe.registration.Profile.UserProfileController;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class PerkController {

    @Autowired
    PerkRepository perkRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GameRepository gameRepository;

    @GetMapping("/perk/all")
    List<Perk> getAllPerks() {
        return perkRepository.findAll();
    }

    @GetMapping("perk/get/{id}")
    public Perk getPerk(@PathVariable long id){
        if(perkRepository.findById(id) != null) {
            return perkRepository.findById(id);
        }
        else{
            return null;
        }
    }

    @PostConstruct
    @GetMapping("/perk/onstart")
    public String perkCSV(){
        try {
            List<Perk> perkList = new ArrayList<>();
            File file = ResourceUtils.getFile("classpath:TestFiles/Perks.csv");
            InputStream inputStream = new FileInputStream(file);
            CsvParserSettings setting = new CsvParserSettings();
            setting.setHeaderExtractionEnabled(true);
            CsvParser parser = new CsvParser(setting);
            List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
            parseAllRecords.forEach(record -> {
                Perk perk = new Perk();
                perk.setPerkName(record.getString("Name"));
                perk.setRarity(record.getInt("Rarity"));
                perk.setDescription(record.getString("Description"));
                perk.setScoreMultiplier(record.getDouble("scoreMultiplier"));
                perk.setStatusMultiplier(record.getDouble("statusMultiplier"));
                perk.setIgnoreDeath(record.getInt("ignoreDeath"));
                perk.setRevive(record.getInt("Revive"));
                perkList.add(perk);
            });
            perkRepository.saveAll(perkList);
            return "Upload successful";
        } catch (IOException e){
            return "Upload failed";
        }
    }

    @GetMapping("/perk/use/{id}")
    public String usePerks(@PathVariable long id){
        int perkNumber = (int)id;
        switch(perkNumber) {
            case 1:
                System.out.println("Score multiplier: " + perkRepository.findById(id).getScoreMultiplier());
                break;
            case 2:
                System.out.println("Ignore death turn: " + perkRepository.findById(id).getIgnoreDeath());
                break;
            case 3:
                System.out.println("Status multiplier reduction: " + perkRepository.findById(id).getStatusMultiplier());
                break;
            case 4:
                System.out.println("Status multiplier reduction: " + perkRepository.findById(id).getStatusMultiplier());
                break;
            case 5:
                System.out.println("Score multiplier: " + perkRepository.findById(id).getScoreMultiplier());
                break;
            case 6:
                System.out.println("Status multiplier reduction: " + perkRepository.findById(id).getStatusMultiplier());
                System.out.println("Score multiplier: " + perkRepository.findById(id).getScoreMultiplier());
                break;
            case 7:
                System.out.println("Ignore death turn: " + perkRepository.findById(id).getIgnoreDeath());
                System.out.println("Score multiplier: " + perkRepository.findById(id).getScoreMultiplier());
                break;
            case 8:
                System.out.println("Ignore death turn: " + perkRepository.findById(id).getIgnoreDeath());
                System.out.println("Status multiplier reduction: " + perkRepository.findById(id).getStatusMultiplier());
                break;
            case 9:
                System.out.println("Status multiplier reduction: " + perkRepository.findById(id).getStatusMultiplier());
                break;
            case 10:
                System.out.println("Score multiplier: " + perkRepository.findById(id).getScoreMultiplier());
                System.out.println("Status multiplier reduction: " + perkRepository.findById(id).getStatusMultiplier());
                break;
            case 11:
                System.out.println("Score multiplier: " + perkRepository.findById(id).getScoreMultiplier());
                break;
            case 12:
                System.out.println("Revive with preset amount of stats: " + perkRepository.findById(id).getRevive());
                break;
        }
        return "Successful selection";
    }

    @PutMapping("/perk/name/{id}/{name}")
    String updatePerkNameById(@PathVariable long id, @PathVariable String name){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perk.setPerkName(name);
            perkRepository.save(perk);
            return "Perk's name at " + id + " successfully updated to " + name;
        }
        else{
            return "Failed to update non existing perk";
        }
    }

    @PutMapping("/perk/rarity/{id}/{rarity}")
    String updatePerkRarityById(@PathVariable long id, @PathVariable int rarity){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perk.setRarity(rarity);
            perkRepository.save(perk);
            return "Perk's rarity at " + id + " successfully updated to " + rarity + " rarity";
        }
        else{
            return "Failed to update non existing perk";
        }
    }

    @PutMapping("/perk/description/{id}/{description}")
    String updatePerkDescriptionById(@PathVariable long id, @PathVariable String description){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perk.setDescription(description);
            perkRepository.save(perk);
            return "Perk's description at " + id + " successfully updated to " + description;
        }
        else{
            return "Failed to update non existing perk";
        }
    }

    @PutMapping("/perk/scoremultiplier/{id}/{value}")
    String updatePerkScoreMultiplierById(@PathVariable long id, @PathVariable double value){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perk.setScoreMultiplier(value);
            perkRepository.save(perk);
            return "Perk's score multiplier at " + id + " successfully updated to " + value;
        }
        else{
            return "Failed to update non existing perk";
        }
    }

    @PutMapping("/perk/statusmultiplier/{id}/{value}")
    String updatePerkStatusMultiplierById(@PathVariable long id, @PathVariable double value){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perk.setStatusMultiplier(value);
            perkRepository.save(perk);
            return "Perk's status multiplier at " + id + " successfully updated to " + value;
        }
        else{
            return "Failed to update non existing perk";
        }
    }


    @PutMapping("/perk/revive/{id}/{value}")
    String updatePerkReviveById(@PathVariable long id, @PathVariable int value){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perk.setRevive(value);
            perkRepository.save(perk);
            return "Perk's revive at " + id + " successfully updated to " + value;
        }
        else{
            return "Failed to update non existing perk";
        }
    }


    @PutMapping("/perk/ignoredeath/{id}/{value}")
    String updatePerkIgnoreDeathById(@PathVariable long id, @PathVariable int value){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perk.setIgnoreDeath(value);
            perkRepository.save(perk);
            return "Perk's ignore death at " + id + " successfully updated to " + value;
        }
        else{
            return "Failed to update non existing perk";
        }
    }


    @DeleteMapping("/perk/delete/{id}")
    String deletePerkById(@PathVariable long id){
        Perk perk = perkRepository.findById(id);
        if(perk != null){
            perkRepository.deleteById(id);
            perkRepository.save(perk);
            return "Perk at " + id + " successfully deleted";
        }
        else{
            return "Failed to delete non existing perk";
        }
    }

    @DeleteMapping("/perk/delete/all")
    public String deletePerks(){
        perkRepository.deleteAll();
        return "Successfully deleted";
    }
}
