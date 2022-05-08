package picklyfe.registration.Game;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import picklyfe.registration.Events.Event;
import picklyfe.registration.Events.EventRepository;
import picklyfe.registration.Perks.PerkRepository;
import picklyfe.registration.Profile.UserProfile;
import picklyfe.registration.Profile.UserProfileRepository;

import java.util.List;
import java.util.Random;

@RestController
public class GameController {
    @Autowired
    UserProfileRepository userProfileRepository;

    @Autowired
    EventRepository eventRepository;

    @Autowired
    GameRepository gameRepository;

    @Autowired
    PerkRepository perkRepository;

//    private String cause = "";

    @GetMapping("/game/all")
    List<Game> getAllGames() {
        return gameRepository.findAll();
    }

    @GetMapping("/game/{id}")
    Game getGameByUserId(@PathVariable long id){
        if (userProfileRepository.findById(id) != null && gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null)
            return gameRepository.findByUserProfile(userProfileRepository.findById(id));
        else
            return null;
    }

    @GetMapping("/game/score/{id}")
    int[] getGameStatusById(@PathVariable long id){
        if (gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null) {
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(id));
            return new int[]{game.getStatus1(), game.getStatus2(), game.getStatus3(), game.getStatus4()};
        } else
            return null;
    }

    @GetMapping("/game/perk/{id}")
    String[] getPerkDetailsById(@PathVariable long id){
        if (gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null) {
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(id));
            return new String[]{game.getPerk().getPerkName(), "" + game.getPerk().getRarity(), game.getPerk().getDescription(),
                                "" + game.getPerk().getStatusMultiplier(), "" + game.getPerk().getScoreMultiplier(),
                                "" + game.getPerk().getIgnoreDeath(), "" + game.getPerk().getRevive()};
        } else
            return null;
    }

    @PutMapping("/game/getEvent")
    Game getGameEvent(@RequestBody UserProfile userProfile) {
        if (userProfile != null && userProfileRepository.findById(userProfile.getId()) != null) {
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(userProfile.getId()));
            Random rand = game.getRand();

            // might be able to remove Math.abs if MySQL table is fixed
            game.setEvent(eventRepository.findById(Math.abs(rand.nextInt()%(eventRepository.count())) + 1));
            game.setNumEventsSurvived(game.getNumEventsSurvived() + 1);

            gameRepository.save(game);
            return game;
//            "Successful get event for user: " + userProfile.getUserName();
        }else {
//            return "Failed to get event.";
            return null;
        }
    }

    @PostMapping("/game/post/{difficulty}")
    String postGame(@RequestBody UserProfile userProfile, @PathVariable int difficulty) {
        if (userProfile != null && userProfileRepository.findById(userProfile.getId()) != null) {
            Random rand = new Random();

            Game game = new Game(userProfile, difficulty, Math.abs(rand.nextInt()));
            if(game.getPerk() != null) {
                game.setPerk(perkRepository.findById(userProfile.getEquippedPerk()));
            }

            //game.setEvent(eventRepository.findById(rand.nextInt()%(eventRepository.count() + 1)));
            gameRepository.save(game);
            userProfile.setNumGamesPlayed(userProfile.getNumGamesPlayed() + 1);
            userProfileRepository.save(userProfile);
            return "Successful created game for user: " + userProfile.getUserName() + " with seed (" + game.getSeed() + ")";
        }else {
            return "Failed to create game.";
        }
    }

    @PostMapping("/game/postId/{difficulty}/{id}")
    String postGameById(@PathVariable long id, @PathVariable int difficulty) {
        if (userProfileRepository.findById(id) != null) {
            UserProfile userProfile = userProfileRepository.findById(id);
            Random rand = new Random();

            Game game = new Game(userProfile, difficulty, Math.abs(rand.nextInt()));
            if(userProfile.getEquippedPerk() != 0){
                game.setPerk(perkRepository.findById(userProfile.getEquippedPerk()));
            }

            //game.setEvent(eventRepository.findById(rand.nextInt()%(eventRepository.count() + 1)));
            gameRepository.save(game);
            userProfile.setNumGamesPlayed(userProfile.getNumGamesPlayed() + 1);
            userProfileRepository.save(userProfile);
            return "Successful created game for user: " + userProfile.getUserName() + " with seed (" + game.getSeed() + ")";
        }else {
            return "Failed to create game.";
        }
    }

    @PostMapping("/game/post/{difficulty}/{seed}")
    String postGameWithSeed(@RequestBody UserProfile userProfile, @PathVariable int difficulty, @PathVariable int seed) {
        if (userProfile != null && userProfileRepository.findByUserName(userProfile.getUserName()) != null) {
            Game game = new Game(userProfile, difficulty, seed);
//            Random rand = new Random(seed);

//            game.setEvent(eventRepository.findById(rand.nextInt()%(eventRepository.count() + 1)));
            gameRepository.save(game);
            userProfile.setNumGamesPlayed(userProfile.getNumGamesPlayed() + 1);
            userProfileRepository.save(userProfile);
            return "Successful created game for user: " + userProfile.getUserName() + " with seed (" + game.getSeed() + ")";
        }else {
            return "Failed to create game.";
        }
    }

    // Choose
    @PutMapping("/game/updateStatus/{id}/{choice}")
    String updateGameStatus1(@PathVariable long id, @PathVariable int choice) {
        int values[] = new int[4];
        if (userProfileRepository.findById(id) != null && gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null){
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(id));
            double multi = game.getMultiplier();

            if (choice < 3 && choice > 0) {
                if (choice == 1)
                    values = game.getEvent().getOption1();
                if (choice == 2)
                    values = game.getEvent().getOption2();
                if(game.getPerk() != null) {
                    game.setStatus1((int) (game.getStatus1() + (int) (values[0] * multi) * game.getPerk().getStatusMultiplier()));
                    game.setStatus2((int) (game.getStatus2() + (int) (values[1] * multi) * game.getPerk().getStatusMultiplier()));
                    game.setStatus3((int) (game.getStatus3() + (int) (values[2] * multi) * game.getPerk().getStatusMultiplier()));
                    game.setStatus4((int) (game.getStatus4() + (int) (values[3] * multi) * game.getPerk().getStatusMultiplier()));
                    game.setCurrScore((int)(game.getCurrScore() + (int)(multi * (Math.abs(values[0]) + Math.abs(values[1]) + Math.abs(values[2]) + Math.abs(values[3]))) * game.getPerk().getScoreMultiplier()));
                } else {
                    game.setStatus1(game.getStatus1() + (int) (values[0] * multi));
                    game.setStatus2(game.getStatus2() + (int) (values[1] * multi));
                    game.setStatus3(game.getStatus3() + (int) (values[2] * multi));
                    game.setStatus4(game.getStatus4() + (int) (values[3] * multi));
                    game.setCurrScore(game.getCurrScore() + (int)(multi * (Math.abs(values[0]) + Math.abs(values[1]) + Math.abs(values[2]) + Math.abs(values[3]))));
                }
            } else
                return "Failed to update status. Invalid choice.";
            gameRepository.save(game);
            return "Successful updated status: " + game.getStatus1() + ", " + game.getStatus2() + ", " + game.getStatus3() + ", " + game.getStatus4();
        }
        else {
            return "Failed to update status.";
        }
    }

    @PutMapping("/game/updateAllStatus/{id}/{status}")
    String updateAllStatus(@PathVariable long id, @PathVariable int status) {
        if (gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null) {
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(id));
            game.setStatus1(status);
            game.setStatus2(status);
            game.setStatus3(status);
            game.setStatus4(status);
            gameRepository.save(game);
            return "Successfully updated all status to: " + game.getStatus1();
        }
        else
            return "Failed to revive with status";
    }

    //no longer needed
    @PutMapping("/game/updateScore/{id}/{score}")
    String updateGameScore(@PathVariable long id, @PathVariable int score) {
        if (gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null) {
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(id));

            game.setCurrScore(score);

            gameRepository.save(game);
            return "Successful updated score to: " + game.getCurrScore();
        }
        else {
            return "Failed to update score.";
        }
    }

    @PutMapping("/game/updateNumEvents/{id}/{numEvents}")
    String updateNumEvents(@PathVariable long id, @PathVariable int numEvents) {
        if (gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null) {
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(id));

            game.setNumEventsSurvived(numEvents);

            gameRepository.save(game);
            return "Successful updated number of events survived: " + game.getNumEventsSurvived();
        } else {
            return "Failed to update status.";
        }
    }

    //might have issues with userProfiles, needs testing
    @PutMapping("/game/end")
    String endGame(@RequestBody UserProfile userProfile) {
        if (userProfile != null && gameRepository.findByUserProfile(userProfileRepository.findById(userProfile.getId())) != null) {
            Game game = gameRepository.findByUserProfile(userProfileRepository.findById(userProfile.getId()));
            if (userProfile != null && userProfile.getHighScore() < game.getCurrScore())
                userProfile.setHighScore(game.getCurrScore());
            if (userProfile != null && userProfile.getLongestEventSurvived() < game.getNumEventsSurvived())
                userProfile.setLongestEventSurvived(game.getNumEventsSurvived());

            game.setEvent(null);
            gameRepository.delete(game);
//            Random rand = new Random();
//            int rand_Perk = rand.nextInt() % 10;
//            int given_Perk;
//            if(rand_Perk < 4) {
//                do {
//                    given_Perk = rand.nextInt() % 12;
//                } while (userProfile.getPerk().contains(perkRepository.findById(given_Perk)));
//                userProfile.addPerk(perkRepository.findById(given_Perk));
//            }
            userProfileRepository.save(userProfile);
            return "Successful end game for user: " + userProfile.getUserName();
        }else {
            return "Failed to end game.";
        }
    }

    @DeleteMapping("/game/delete/all")
    String deleteAllGame() {

        gameRepository.deleteAll();
        return "Successfully deleted all ongoing games";
    }

    @DeleteMapping("/game/delete/{id}")
    String deleteGameByID(@PathVariable long id) {
        Game game = null;
        if (userProfileRepository.findById(id) != null && gameRepository.findByUserProfile(userProfileRepository.findById(id)) != null) {
            game = gameRepository.findByUserProfile(userProfileRepository.findById(id));
            game.setEvent(null);
            gameRepository.delete(game);
        }
        return "Successfully deleted game for user";
    }
}
