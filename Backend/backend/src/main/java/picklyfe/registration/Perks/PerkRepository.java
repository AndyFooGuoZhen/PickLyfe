package picklyfe.registration.Perks;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PerkRepository extends JpaRepository<Perk, Long> {
    Perk findById(long id);
    Perk findByPerkName(String name);
}
