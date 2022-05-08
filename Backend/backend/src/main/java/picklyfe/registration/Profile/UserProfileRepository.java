package picklyfe.registration.Profile;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface UserProfileRepository extends JpaRepository<UserProfile, Long>{
    UserProfile findByUserName(String userName);
    UserProfile findById(long id);
    @Transactional
    void deleteByUserName(String userName);
}
