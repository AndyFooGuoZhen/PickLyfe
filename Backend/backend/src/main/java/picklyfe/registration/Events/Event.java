package picklyfe.registration.Events;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import picklyfe.registration.Game.Game;

import javax.persistence.*;
import java.util.Set;

@Entity
@Table(name = "event")
@SQLDelete(sql = "UPDATE event SET deleted = true WHERE id=?")
@Where(clause = "deleted=false")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @OneToMany
    @JsonIgnore
    private Set<Game> game;

    private String name;
    @Column(length = 3000)
    private String description;
    private int score;

    @Column(length = 3000)
    private String option1_desc;
    private int[] option1;
    @Column(length = 3000)
    private String option1_response;
    @Column(length = 3000)
    private String option2_desc;
    private int[] option2;
    @Column(length = 3000)
    private String option2_response;

    private boolean deleted = Boolean.FALSE;

    public Event() {
    }

    public Event(String name, String description, int[] option1, int[] option2) {
        this.name = name;
        this.description = description;
        this.option1 = option1;
        this.option2 = option2;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public Set<Game> getGame() {
        return game;
    }

    public void setGame(Set<Game> game) {
        this.game = game;
    }

    public Set<Game> addGame(Game game){
        this.game.add(game);
        return this.game;
    }

    public void removeGame(Game game){
        this.game.remove(game);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getScore() {
        return score;
    }

    public void setScore(int score) {
        this.score = score;
    }

    public int[] getOption1() {
        return option1;
    }

    public void setOption1(int[] option1) {
        this.option1 = option1;
    }

    public int[] getOption2() {
        return option2;
    }

    public void setOption2(int[] option2) {
        this.option2 = option2;
    }

    public String getOption1_desc() {
        return option1_desc;
    }

    public void setOption1_desc(String option1_desc) {
        this.option1_desc = option1_desc;
    }

    public String getOption2_desc() {
        return option2_desc;
    }

    public void setOption2_desc(String option2_desc) {
        this.option2_desc = option2_desc;
    }

    public String getOption1_response() {
        return option1_response;
    }

    public void setOption1_response(String option1_response) {
        this.option1_response = option1_response;
    }

    public String getOption2_response() {
        return option2_response;
    }

    public void setOption2_response(String option2_response) {
        this.option2_response = option2_response;
    }

    public boolean isDeleted() {
        return deleted;
    }

    public void setDeleted(boolean deleted) {
        this.deleted = deleted;
    }
}
