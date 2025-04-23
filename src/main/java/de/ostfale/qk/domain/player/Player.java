package de.ostfale.qk.domain.player;

import org.jboss.logging.Logger;

public class Player {
    private static final Logger log = Logger.getLogger(Player.class);

    private PlayerId playerId;
    private String firstName;
    private String lastName;
    private GenderType gender;
    private int yearOfBirth;

    private PlayerInfo playerInfo;

    private RankingInformation singleRankingInformation;
    private RankingInformation doubleRankingInformation;
    private RankingInformation mixedRankingInformation;

    public Player(String playerId, String firstName, String lastName, GenderType gender, int yearOfBirth) {
        setPlayerId(playerId);
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.yearOfBirth = yearOfBirth;
    }

    public Player() {
    }

    @Override
    public String toString() {
        return getFullName();
    }

    public int getSinglePoints() {
        return singleRankingInformation.rankingPoints();
    }

    public int getDoublePoints() {
        return doubleRankingInformation.rankingPoints();
    }

    public int getMixedPoints() {
        return mixedRankingInformation.rankingPoints();
    }

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public PlayerInfo getPlayerInfo() {
        return playerInfo;
    }

    public void setPlayerInfo(PlayerInfo playerInfo) {
        this.playerInfo = playerInfo;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = new PlayerId(playerId);
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

    public GenderType getGender() {
        return gender;
    }

    public void setGender(GenderType gender) {
        this.gender = gender;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }

    public RankingInformation getSingleRankingInformation() {
        return singleRankingInformation;
    }

    public void setSingleRankingInformation(RankingInformation singleRankingInformation) {
        this.singleRankingInformation = singleRankingInformation;
    }

    public RankingInformation getDoubleRankingInformation() {
        return doubleRankingInformation;
    }

    public void setDoubleRankingInformation(RankingInformation doubleRankingInformation) {
        this.doubleRankingInformation = doubleRankingInformation;
    }

    public RankingInformation getMixedRankingInformation() {
        return mixedRankingInformation;
    }

    public void setMixedRankingInformation(RankingInformation mixedRankingInformation) {
        this.mixedRankingInformation = mixedRankingInformation;
    }
}
