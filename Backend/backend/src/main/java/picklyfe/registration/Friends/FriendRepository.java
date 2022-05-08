package picklyfe.registration.Friends;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import picklyfe.registration.Profile.UserProfile;

import java.util.List;

@Repository
public interface FriendRepository extends JpaRepository<Friend,Long>{
    boolean existsByFirstUserAndSecondUser(UserProfile first,UserProfile second);

    List<Friend> findByFirstUser(UserProfile user);
    List<Friend> findBySecondUser(UserProfile user);

}