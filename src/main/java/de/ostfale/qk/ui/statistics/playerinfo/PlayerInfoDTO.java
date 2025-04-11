package de.ostfale.qk.ui.statistics.playerinfo;

import org.jboss.logging.Logger;

import de.ostfale.qk.db.internal.player.Player;

public class PlayerInfoDTO {

    private static final Logger log = Logger.getLogger(PlayerInfoDTO.class);

    private final String playerName;
    private final String gender;
    private final String birthYear;
    private final String ageClass;
    private final String ageClassDetail;
    private final String clubName;
    private final String districtName;
    private final String stateName;
    private final String stateGroup;
    private final String playerId;

    private String playerTournamentId;
    private Boolean favorite;

    // tournament statistics
    private DisciplineStatisticsDTO singleDisciplineStatistics;
    private DisciplineStatisticsDTO doubleDisciplineStatistics;
    private DisciplineStatisticsDTO mixedDisciplineStatistics;

    public PlayerInfoDTO(Player player) {
        log.tracef("PlayerInfoDTO :: init from player %d",player.getPlayerId());
        this.playerName = player.getName();
        this.gender = player.getGender().toString();
        this.playerId = player.getPlayerId();
        this.playerTournamentId = player.getPlayerTournamentId();
        this.favorite = player.getFavorite();
        this.birthYear = player.getYearOfBirth().toString();
        this.ageClass = player.getAgeClassGeneral();
        this.ageClassDetail = player.getAgeClassDetail();
        this.clubName = player.getClubName();
        this.districtName = player.getDistrictName();
        this.stateName = player.getStateName();
        this.stateGroup = player.getStateGroup() == null ? "" : player.getStateGroup().toString();

        this.singleDisciplineStatistics = mapSingleDisciplineStatistics(player);
        this.doubleDisciplineStatistics = mapDoubleDisciplineStatistics(player);
        this.mixedDisciplineStatistics = mapMixedDisciplineStatistics(player);
    }

    @Override
    public String toString() {
        return playerName;
    }
    
    public String getStateName() {
        return stateName;
    }

    public String getStateGroup() {
        return stateGroup;
    }

    public Integer getSinglePoints() {
        return singleDisciplineStatistics.points();
    }

    public Integer getDoublePoints() {
        return doubleDisciplineStatistics.points();
    }

    public Integer getMixedPoints() {
        return mixedDisciplineStatistics.points();
    }

    public String getGender() {
        return gender;
    }

    public String getAgeClassDetail() {
        return ageClassDetail;
    }

    public DisciplineStatisticsDTO getSingleDisciplineStatistics() {
        return singleDisciplineStatistics;
    }

    public void setSingleDisciplineStatistics(DisciplineStatisticsDTO singleDisciplineStatistics) {
        this.singleDisciplineStatistics = singleDisciplineStatistics;
    }

    public DisciplineStatisticsDTO getDoubleDisciplineStatistics() {
        return doubleDisciplineStatistics;
    }

    public void setDoubleDisciplineStatistics(DisciplineStatisticsDTO doubleDisciplineStatistics) {
        this.doubleDisciplineStatistics = doubleDisciplineStatistics;
    }

    public DisciplineStatisticsDTO getMixedDisciplineStatistics() {
        return mixedDisciplineStatistics;
    }

    public void setMixedDisciplineStatistics(DisciplineStatisticsDTO mixedDisciplineStatistics) {
        this.mixedDisciplineStatistics = mixedDisciplineStatistics;
    }

    public String getClubName() {
        return clubName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public String getPlayerTournamentId() {
        return playerTournamentId;
    }

    public void setPlayerTournamentId(String playerTournamentId) {
        this.playerTournamentId = playerTournamentId;
    }

    public Boolean getFavorite() {
        return favorite;
    }

    public void setFavorite(Boolean favorite) {
        this.favorite = favorite;
    }

    public String getBirthYear() {
        return birthYear;
    }

    public String getAgeClass() {
        return ageClass;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    private DisciplineStatisticsDTO mapSingleDisciplineStatistics(Player player) {
        var singlePoints = player.getSinglePoints();
        var singleRanking = player.getSingleRanking();
        var singleAgeRanking = player.getSingleAgeRanking();
        var singleTournaments = player.getSingleTournaments();
        return new DisciplineStatisticsDTO(singleTournaments, singlePoints, singleRanking, singleAgeRanking);
    }

    private DisciplineStatisticsDTO mapDoubleDisciplineStatistics(Player player) {
        var doublePoints = player.getDoublePoints();
        var doubleRanking = player.getDoubleRanking();
        var doubleAgeRanking = player.getDoubleAgeRanking();
        var doubleTournaments = player.getDoubleTournaments();
        return new DisciplineStatisticsDTO(doubleTournaments, doublePoints, doubleRanking, doubleAgeRanking);
    }

    private DisciplineStatisticsDTO mapMixedDisciplineStatistics(Player player) {
        var mixedPoints = player.getMixedPoints();
        var mixedRanking = player.getMixedRanking();
        var mixedAgeRanking = player.getMixedAgeRanking();
        var mixedTournaments = player.getMixedTournaments();
        return new DisciplineStatisticsDTO(mixedTournaments, mixedPoints, mixedRanking, mixedAgeRanking);
    }
}
