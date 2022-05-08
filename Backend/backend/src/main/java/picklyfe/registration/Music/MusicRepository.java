package picklyfe.registration.Music;

import org.springframework.data.jpa.repository.JpaRepository;

public interface MusicRepository extends JpaRepository<Music, Long> {
    Music findById(long id);
//    boolean existsMusicByFileNameEquals(String filename);
//    boolean existsMusicByTitleEquals(String title);
}
