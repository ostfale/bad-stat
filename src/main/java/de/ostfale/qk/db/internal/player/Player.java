package de.ostfale.qk.db.internal.player;

import de.ostfale.qk.parser.ranking.internal.GenderType;
import de.ostfale.qk.parser.ranking.internal.Group;
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
    private String fullName;
    private Integer yearOfBirth;
    private Boolean favorite = false;

    // general info
    private String clubName;
    private String districtName;
    private String stateName;
    private String ageClassGeneral;
    private String ageClassDetail;

    @Enumerated(EnumType.STRING)
    private Group stateGroup;

    // points info
    private Integer singlePoints = 0;
    private Integer singleRanking = 0;
    private Integer singleTournaments = 0;
    private Integer doublePoints = 0;
    private Integer doubleRanking = 0;
    private Integer doubleTournaments = 0;
    private Integer mixedPoints = 0;
    private Integer mixedRanking = 0;
    private Integer mixedTournaments = 0;

    @Enumerated(EnumType.STRING)
    private GenderType gender;

    public Player(RankingPlayer rankingPlayer) {
        this.playerId = rankingPlayer.getPlayerId();
        this.firstName = rankingPlayer.getFirstName();
        this.lastName = rankingPlayer.getLastName();
        this.fullName = rankingPlayer.getName();
        this.yearOfBirth = rankingPlayer.getYearOfBirth();
        this.gender = rankingPlayer.getGenderType();
        this.singlePoints = rankingPlayer.getSinglePoints();
        this.singleRanking = rankingPlayer.getSingleRanking();
        this.singleTournaments = rankingPlayer.getSingleTournaments();
        this.doublePoints = rankingPlayer.getDoublePoints();
        this.doubleRanking = rankingPlayer.getDoubleRanking();
        this.doubleTournaments = rankingPlayer.getDoubleTournaments();
        this.mixedPoints = rankingPlayer.getMixedPoints();
        this.mixedRanking = rankingPlayer.getMixedRanking();
        this.mixedTournaments = rankingPlayer.getMixedTournaments();
    }

    public Player() {

    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    @Override
    public String toString() {
        return firstName + " " + lastName;
    }

    public Group getStateGroup() {
        return stateGroup;
    }

    public void setStateGroup(Group stateGroup) {
        this.stateGroup = stateGroup;
    }

    public String getClubName() {
        return clubName;
    }

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public void setStateName(String stateName) {
        this.stateName = stateName;
    }

    public String getAgeClassGeneral() {
        return ageClassGeneral;
    }

    public void setAgeClassGeneral(String ageClassGeneral) {
        this.ageClassGeneral = ageClassGeneral;
    }

    public String getAgeClassDetail() {
        return ageClassDetail;
    }

    public void setAgeClassDetail(String ageClassDetail) {
        this.ageClassDetail = ageClassDetail;
    }

    public Integer getSinglePoints() {
        return singlePoints;
    }

    public void setSinglePoints(Integer singlePoints) {
        this.singlePoints = singlePoints;
    }

    public Integer getSingleRanking() {
        return singleRanking;
    }

    public void setSingleRanking(Integer singleRanking) {
        this.singleRanking = singleRanking;
    }

    public Integer getSingleTournaments() {
        return singleTournaments;
    }

    public void setSingleTournaments(Integer singleTournaments) {
        this.singleTournaments = singleTournaments;
    }

    public Integer getDoublePoints() {
        return doublePoints;
    }

    public void setDoublePoints(Integer doublePoints) {
        this.doublePoints = doublePoints;
    }

    public Integer getDoubleRanking() {
        return doubleRanking;
    }

    public void setDoubleRanking(Integer doubleRanking) {
        this.doubleRanking = doubleRanking;
    }

    public Integer getDoubleTournaments() {
        return doubleTournaments;
    }

    public void setDoubleTournaments(Integer doubleTournaments) {
        this.doubleTournaments = doubleTournaments;
    }

    public Integer getMixedPoints() {
        return mixedPoints;
    }

    public void setMixedPoints(Integer mixedPoints) {
        this.mixedPoints = mixedPoints;
    }

    public Integer getMixedRanking() {
        return mixedRanking;
    }

    public void setMixedRanking(Integer mixedRanking) {
        this.mixedRanking = mixedRanking;
    }

    public Integer getMixedTournaments() {
        return mixedTournaments;
    }

    public void setMixedTournaments(Integer mixedTournaments) {
        this.mixedTournaments = mixedTournaments;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
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
