package picklyfe.registration.Patch;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@RestController
public class PatchController {

    @Autowired
    PatchRepository patchRepository;

    @GetMapping("/patch/all")
    List<Patch> getAllAnnouncements(){
        return patchRepository.findAll();
    }

    @GetMapping("/patch/{id}")
    Patch getPatchById(@PathVariable long id){
        if(patchRepository.findById(id) != null){
            return patchRepository.findById(id);
        } else {
            return null;
        }
    }

    @PostMapping("/patch/newString/{title}/{newText}")
    String postNewPatchByString(@PathVariable String title, @PathVariable String newText){
        Patch newPatch = new Patch();
        newPatch.setTitle(title);
        newPatch.setDescription(newText);
        patchRepository.save(newPatch);
        return "Successfully created a new patch";
    }

    @PostMapping("/patch/newPost")
    String postNewAnnouncementByObj(@RequestBody Patch patch){
        if(patch != null && patchRepository.findById(patch.getId()) == null) {
            patchRepository.save(patch);
            return "Successfully created a new patch";
        } else {
            return "Failed to create a new patch";
        }
    }

    @PutMapping("/patch/editText/{id}/{newText}")
    String putTextByString(@PathVariable long id, @PathVariable String newText){
        if(patchRepository.findById(id) != null){
            Patch editedPatch = patchRepository.findById(id);
            editedPatch.setDescription(newText);
            editedPatch.setDate(LocalDate.now());
            editedPatch.setTime(LocalTime.now());
            patchRepository.save(editedPatch);
            return "Successfully edited a patch";
        } else {
            return "Failed to edit patch";
        }
    }

    @PutMapping("/patch/editTitle/{id}/{newTitle}")
    String putTitleByString(@PathVariable long id, @PathVariable String newTitle){
        if(patchRepository.findById(id) != null){
            Patch editedPatch = patchRepository.findById(id);
            editedPatch.setTitle(newTitle);
            editedPatch.setDate(LocalDate.now());
            editedPatch.setTime(LocalTime.now());
            patchRepository.save(editedPatch);
            return "Successfully edited a patch";
        } else {
            return "Failed to edit patch";
        }
    }

    @DeleteMapping ("/patch/delete/{id}")
    String deletePatch(@PathVariable long id){
        if(patchRepository.findById(id) != null){
            patchRepository.delete(patchRepository.findById(id));
            return "Successfully deleted a patch";
        } else {
            return "Failed to delete patch";
        }
    }

    @DeleteMapping("/patch/delete/all")
    String deleteAllPatch(){
        patchRepository.deleteAll();
        return "Successfully deleted all patches";
    }

}
