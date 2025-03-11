package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.db.internal.player.Player;
import org.jboss.logging.Logger;

public class PlayerInfoDTO {

    private static final Logger log = Logger.getLogger(PlayerInfoDTO.class);

    private String playerName;
    private String birthYear;
    private String ageClass;
    private String clubName;
    private String districtName;

    private String playerId;
    private String playerTournamentId;
    private Boolean favorite;


    // tournament statistics
    private DisciplineStatisticsDTO singleDisciplineStatistics;
    private DisciplineStatisticsDTO doubleDisciplineStatistics;
    private DisciplineStatisticsDTO mixedDisciplineStatistics;

    public PlayerInfoDTO() {
    }

    public PlayerInfoDTO(Player player) {
        this.playerName = player.getName();
        this.playerId = player.getPlayerId();
        this.playerTournamentId = player.getPlayerTournamentId();
        this.favorite = player.getFavorite();
        this.birthYear = player.getYearOfBirth().toString();
        this.ageClass = player.getAgeClassGeneral() + " " + player.getAgeClassDetail();
        this.clubName = player.getClubName();
        this.districtName = player.getDistrictName();

        this.singleDisciplineStatistics = mapSingleDisciplineStatistics(player);
        this.doubleDisciplineStatistics = mapDoubleDisciplineStatistics(player);
        this.mixedDisciplineStatistics = mapMixedDisciplineStatistics(player);
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

    public void setClubName(String clubName) {
        this.clubName = clubName;
    }

    public String getDistrictName() {
        return districtName;
    }

    public void setDistrictName(String districtName) {
        this.districtName = districtName;
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

    public void setBirthYear(String birthYear) {
        this.birthYear = birthYear;
    }

    public String getAgeClass() {
        return ageClass;
    }

    public void setAgeClass(String ageClass) {
        this.ageClass = ageClass;
    }

    public String getPlayerName() {
        return playerName;
    }

    public void setPlayerName(String playerName) {
        this.playerName = playerName;
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    private DisciplineStatisticsDTO mapSingleDisciplineStatistics(Player player) {
        var singlePoints = player.getSinglePoints();
        var singleRanking = player.getSingleRanking();
        var singleAgeRanking = player.getSingleAgeRanking();
        var singleTournaments = player.getSingleTournaments();
        return new DisciplineStatisticsDTO(singlePoints, singleRanking, singleAgeRanking, singleTournaments);
    }

    private DisciplineStatisticsDTO mapDoubleDisciplineStatistics(Player player) {
        var doublePoints = player.getDoublePoints();
        var doubleRanking = player.getDoubleRanking();
        var doubleAgeRanking = player.getDoubleAgeRanking();
        var doubleTournaments = player.getDoubleTournaments();
        return new DisciplineStatisticsDTO(doublePoints, doubleRanking, doubleAgeRanking, doubleTournaments);
    }

    private DisciplineStatisticsDTO mapMixedDisciplineStatistics(Player player) {
        var mixedPoints = player.getMixedPoints();
        var mixedRanking = player.getMixedRanking();
        var mixedAgeRanking = player.getMixedAgeRanking();
        var mixedTournaments = player.getMixedTournaments();
        return new DisciplineStatisticsDTO(mixedPoints, mixedRanking, mixedAgeRanking, mixedTournaments);
    }
}
