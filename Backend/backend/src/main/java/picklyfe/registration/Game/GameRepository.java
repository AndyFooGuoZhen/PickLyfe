package picklyfe.registration.Game;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picklyfe.registration.Profile.UserProfile;

public interface GameRepository extends JpaRepository<Game, Long> {
    Game findByUserProfile(UserProfile userProfile);
    Game findById(long id);

    //void deleteByUserProfile(UserProfile userProfile);
}
