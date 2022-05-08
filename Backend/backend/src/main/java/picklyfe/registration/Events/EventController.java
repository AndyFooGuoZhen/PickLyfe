package picklyfe.registration.Events;

import com.univocity.parsers.common.record.Record;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.ResourceUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import picklyfe.registration.Game.Game;
import picklyfe.registration.Game.GameRepository;

import javax.annotation.PostConstruct;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

@RestController
public class EventController {

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GameRepository gameRepository;

    //Get all available events
    @GetMapping("/event/all")
    List<Event> getAllEvents() {
        return eventRepository.findAll();
    }

    //Get Event by Id
    @GetMapping("/event/{id}")
    Event getEventById(@PathVariable long id) {
        if (eventRepository.findById(id) != null)
            return eventRepository.findById(id);
        else
            return null;
    }

    //Get num Events
    @GetMapping("/event/num")
    long numEvents() {
        return eventRepository.count();
    }

    //Posting Event
    @PostMapping("/event/post/{name}/{desc}/{option1_desc}/{option1}/{option2_desc}/{option2}")
    Event postEvent(@PathVariable String name, @PathVariable String desc, @PathVariable String option1_desc, @PathVariable int[] option1, @PathVariable String option2_desc, @PathVariable int[] option2) {
        if (eventRepository.findByName(name) != null || name.isEmpty())
            return null;
        if (desc.isEmpty() || option1_desc.isEmpty() || option2_desc.isEmpty())
            return null;

        Event event = new Event(name, desc, option1, option2);
        event.setOption1_desc(option1_desc);
        event.setOption2_desc(option2_desc);
        eventRepository.save(event);
        return event;
    }

    //Posting Event
    @PostMapping("/event/post")
    Event postEvent(@RequestBody Event event) {
        if (event != null && eventRepository.findById(event.getId()) == null && eventRepository.findByName(event.getName()) == null)
            eventRepository.save(event);
        return event;
    }

    //Get option values
    @PutMapping("/event/choose/1")
    int[] updateStats1(@RequestBody Game game) {
        int[] value = null;
        if (game != null && gameRepository.findById(game.getId()) != null) {
            value = game.getEvent().getOption2();
        }
        return value;
    }

    @PutMapping("/event/choose/2")
    int[] updateStats2(@RequestBody Game game) {
        int[] value = null;
        if (game != null && gameRepository.findById(game.getId()) != null) {
            value = game.getEvent().getOption2();
        }
        return value;
    }

    //Delete event
    @DeleteMapping("/event/delete/{id}")
    String deleteEvent(@PathVariable long id) {
        if (eventRepository.findById(id) != null) {
            eventRepository.delete(eventRepository.findById(id));
            return "Successful deleted event";
        } else {
            return "Failed to delete event.";
        }
    }

    @DeleteMapping("/event/delete/all")
    String deleteAllEvent() {
        eventRepository.deleteAll();
        return "Successful deleted all event";
    }

    @GetMapping("/event/demo")
    String demoEvent() {
        Event event1 = new Event("University Invitation", "You are invited to Iowa State University to further your studies. " +
                "You are given a scholarship and living costs are covered. As the only child of the family, your family are " +
                "worried as you are living at another state alone.", new int[]{10, 10, -20, -10}, new int[]{-20, 10, -10, 10});

        event1.setOption1_desc("Stay with your family");
        event1.setOption2_desc("Further your studies at ISU");

        event1.setOption1_response("You decided that staying with your family is the better choice. Plus, the local university isn't half bad.");
        event1.setOption2_response("You decided that this is a once in a lifetime opportunity. You took the opportunity and embarked on a new journey.");

        eventRepository.save(event1);
        return "Successfully created demo event";
    }

    @PostMapping("/event/upload")
    public String uploadEvents(@RequestParam("file") MultipartFile file) throws Exception {
        List<Event> eventsList = new ArrayList<>();
        InputStream inputStream = file.getInputStream();
        CsvParserSettings setting = new CsvParserSettings();
        setting.setHeaderExtractionEnabled(true);
        CsvParser parser = new CsvParser(setting);
        List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
        parseAllRecords.forEach(record -> {
            Event event = new Event();
            event.setName(record.getString("name"));
            event.setDescription(record.getString("description"));
            event.setOption1_desc(record.getString("option1_desc"));
            event.setOption1_response(record.getString("option1_response"));
            event.setOption2_desc(record.getString("option2_desc"));
            event.setOption2_response(record.getString("option2_response"));
            int[] option1 = {record.getInt("1_Money"), record.getInt("1_Health"), record.getInt("1_Intelligence"), record.getInt("1_Relationship")};
            int[] option2 = {record.getInt("2_Money"), record.getInt("2_Health"), record.getInt("2_Intelligence"), record.getInt("2_Relationship")};
            event.setOption1(option1);
            event.setOption2(option2);

            if (eventRepository.findByName(event.getName()) == null)
                eventsList.add(event);

        });
        eventRepository.saveAll(eventsList);
        return "Successful upload events";
    }

    @PostConstruct
    @PostMapping("/event/onstart")
    public String initialEvents(){
        try {
            if (eventRepository.count() == 0) {
                List<Event> eventsList = new ArrayList<>();
                File file = ResourceUtils.getFile("classpath:TestFiles/Events.csv");
                InputStream inputStream = new FileInputStream(file);
                CsvParserSettings setting = new CsvParserSettings();
                setting.setHeaderExtractionEnabled(true);
                CsvParser parser = new CsvParser(setting);
                List<Record> parseAllRecords = parser.parseAllRecords(inputStream);
                parseAllRecords.forEach(record -> {
                    Event event = new Event();
                    event.setName(record.getString("name"));
                    event.setDescription(record.getString("description"));
                    event.setOption1_desc(record.getString("option1_desc"));
                    event.setOption1_response(record.getString("option1_response"));
                    event.setOption2_desc(record.getString("option2_desc"));
                    event.setOption2_response(record.getString("option2_response"));
                    int option1[] = {record.getInt("1_Money"), record.getInt("1_Health"), record.getInt("1_Intelligence"), record.getInt("1_Relationship")};
                    int option2[] = {record.getInt("2_Money"), record.getInt("2_Health"), record.getInt("2_Intelligence"), record.getInt("2_Relationship")};
                    event.setOption1(option1);
                    event.setOption2(option2);
                    eventsList.add(event);
                });
                eventRepository.saveAll(eventsList);
                return "Successful upload events";
            }
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return "Upload failed";
    }
}
