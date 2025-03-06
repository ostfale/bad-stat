package de.ostfale.qk.parser.ranking.internal;

public class RankingPlayer {

    private final String playerId;
    private final String firstName;
    private final String lastName;
    private final GenderType genderType;
    private final Integer yearOfBirth;
    private final String ageClassGeneral;
    private final String ageClassDetail;
    private final String clubName;
    private final String districtName;
    private final String stateName;
    private final Group stateGroup;
    private Integer singlePoints = 0;
    private Integer singleRanking = 0;
    private Integer singleAgeRanking = 0;
    private Integer singleTournaments = 0;
    private Integer doublePoints = 0;
    private Integer doubleRanking = 0;
    private Integer doubleAgeRanking = 0;
    private Integer doubleTournaments = 0;
    private Integer mixedPoints = 0;
    private Integer mixedRanking = 0;
    private Integer mixedAgeRanking = 0;
    private Integer mixedTournaments = 0;
    private Boolean isFavorite = false;

    public RankingPlayer(String playerId, String firstName, String lastName, GenderType genderType, Integer yearOfBirth,
                         String ageClassGeneral, String ageClassDetail, String clubName, String districtName, String stateName, Group stateGroup) {
        this.playerId = playerId;
        this.firstName = firstName;
        this.lastName = lastName;
        this.genderType = genderType;
        this.yearOfBirth = yearOfBirth;
        this.ageClassGeneral = ageClassGeneral;
        this.ageClassDetail = ageClassDetail;
        this.clubName = clubName;
        this.districtName = districtName;
        this.stateName = stateName;
        this.stateGroup = stateGroup;
    }

    public Integer getSingleAgeRanking() {
        return singleAgeRanking;
    }

    public void setSingleAgeRanking(Integer singleAgeRanking) {
        this.singleAgeRanking = singleAgeRanking;
    }

    public Integer getDoubleAgeRanking() {
        return doubleAgeRanking;
    }

    public void setDoubleAgeRanking(Integer doubleAgeRanking) {
        this.doubleAgeRanking = doubleAgeRanking;
    }

    public Integer getMixedAgeRanking() {
        return mixedAgeRanking;
    }

    public void setMixedAgeRanking(Integer mixedAgeRanking) {
        this.mixedAgeRanking = mixedAgeRanking;
    }

    public String getName() {
        return firstName + " " + lastName;
    }

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Integer getSingleTournaments() {
        return singleTournaments;
    }

    public Integer getDoubleTournaments() {
        return doubleTournaments;
    }

    public Integer getMixedTournaments() {
        return mixedTournaments;
    }

    public String getPlayerId() {
        return playerId;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public GenderType getGenderType() {
        return genderType;
    }

    public Integer getYearOfBirth() {
        return yearOfBirth;
    }

    public String getAgeClassGeneral() {
        return ageClassGeneral;
    }

    public String getAgeClassDetail() {
        return ageClassDetail;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getStateName() {
        return stateName;
    }

    public Group getStateGroup() {
        return stateGroup;
    }

    public Integer getSinglePoints() {
        return singlePoints;
    }

    public Integer getSingleRanking() {
        return singleRanking;
    }

    public Integer getDoublePoints() {
        return doublePoints;
    }

    public Integer getDoubleRanking() {
        return doubleRanking;
    }

    public Integer getMixedPoints() {
        return mixedPoints;
    }

    public Integer getMixedRanking() {
        return mixedRanking;
    }

    public void setSinglePointsAndRanking(Integer singlePoints, Integer singleRanking, Integer ageRanking, Integer noOfTournaments) {
        this.singlePoints = singlePoints;
        this.singleRanking = singleRanking;
        this.singleAgeRanking = ageRanking;
        this.singleTournaments = noOfTournaments;
    }

    public void setDoublePointsAndRanking(Integer doublePoints, Integer doubleRanking, Integer ageRanking, Integer noOfTournaments) {
        this.doublePoints = doublePoints;
        this.doubleRanking = doubleRanking;
        this.doubleAgeRanking = ageRanking;
        this.doubleTournaments = noOfTournaments;
    }

    public void setMixedPointsAndRanking(Integer mixedPoints, Integer mixedRanking, Integer ageRanking, Integer noOfTournaments) {
        this.mixedPoints = mixedPoints;
        this.mixedRanking = mixedRanking;
        this.mixedAgeRanking = ageRanking;
        this.mixedTournaments = noOfTournaments;
    }
}
