package de.ostfale.qk.db.internal;

import de.ostfale.qk.parser.ranking.internal.GenderType;
import de.ostfale.qk.parser.ranking.internal.Player;
import jakarta.persistence.*;

@Entity
public class PlayerEntity {

    @Id
    @GeneratedValue
    private Long id;

    private String playerId;
    private String firstName;
    private String lastName;
    private Integer yearOfBirth;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    public PlayerEntity(Player player) {
        this.playerId = player.getPlayerId();
        this.firstName = player.getFirstName();
        this.lastName = player.getLastName();
        this.yearOfBirth = player.getYearOfBirth();
        this.gender = player.getGenderType();
    }

    public PlayerEntity() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(Integer yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }
}
