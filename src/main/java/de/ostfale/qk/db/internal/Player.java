package de.ostfale.qk.db.internal;

import de.ostfale.qk.parser.ranking.internal.GenderType;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import jakarta.persistence.*;

@Entity
public class Player {

    @Id
    @GeneratedValue
    private Long id;

    private String playerId;
    private String firstName;
    private String lastName;
    private Integer yearOfBirth;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    @OneToOne(fetch = FetchType.LAZY,cascade = CascadeType.ALL)
    private PlayerInfo playerInfo;

    public Player(RankingPlayer rankingPlayer) {
        this.playerId = rankingPlayer.getPlayerId();
        this.firstName = rankingPlayer.getFirstName();
        this.lastName = rankingPlayer.getLastName();
        this.yearOfBirth = rankingPlayer.getYearOfBirth();
        this.gender = rankingPlayer.getGenderType();
    }

    public Player() {

    }

    public PlayerInfo getPlayerMasterData() {
        return playerInfo;
    }

    public void setPlayerMasterData(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
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
