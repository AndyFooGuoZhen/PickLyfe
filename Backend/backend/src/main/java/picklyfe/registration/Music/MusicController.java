package picklyfe.registration.Music;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picklyfe.registration.Perks.Perk;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class MusicController {

    @Autowired
    MusicRepository musicRepository;

    @GetMapping("/music/all")
    List<Music> getAllMusic() {
        return musicRepository.findAll();
    }

    @GetMapping("music/get/{id}")
    Music getSongsById(@PathVariable long id){
        if (musicRepository.findById(id) != null)
            return musicRepository.findById(id);
        else
            return null;
    }

    /*@PostMapping("music/post")
    String postMusic(@RequestPart("song") Music music, @RequestParam("file") MultipartFile multipartFile) {
        if (musicRepository.existsMusicByFileNameEquals(music.getFileName()) || musicRepository.existsMusicByTitleEquals(music.getTitle()))
            return "There exists a file with the same name";

        music.setFileName(multipartFile.getOriginalFilename());

        musicRepository.save(music);
        return "Music successfully uploaded";
    }*/

    @PostConstruct
    @GetMapping("/music/onstart")
    public String musicCSV(){
        try {
            List<Music> musicList = new ArrayList<>();
            File file = ResourceUtils.getFile("classpath:TestFiles/Music.csv");
            InputStream inputStream = new FileInputStream(file);
            CsvParserSettings setting = new CsvParserSettings();
            setting.setHeaderExtractionEnabled(true);
            CsvParser parser = new CsvParser(setting);
            List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
            parseAllRecords.forEach(record -> {
                Music music = new Music();
                music.setName(record.getString("Name"));
                music.setMusicURL(record.getString("MusicURL"));
                music.setLength(record.getInt("Length"));
                musicList.add(music);
            });
            musicRepository.saveAll(musicList);
            return "Upload successful";
        } catch (IOException e){
            return "Upload failed";
        }
    }

    @DeleteMapping("music/delete/{id}")
    String deleteMusicById(@PathVariable long id){
        if(musicRepository.existsById(id)){
            musicRepository.deleteById(id);
            return "Music at " + id + " successfully deleted";
        }
        else{
            return "Failed to delete non existing music";
        }
    }

    @DeleteMapping("music/delete/all")
    public String deleteMusic(){
        musicRepository.deleteAll();
        return "Successfully deleted";
    }
}
