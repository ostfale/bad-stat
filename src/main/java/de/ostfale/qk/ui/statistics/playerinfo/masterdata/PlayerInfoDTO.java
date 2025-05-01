package de.ostfale.qk.ui.statistics.playerinfo.masterdata;

import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.ui.statistics.playerinfo.rankingdata.DisciplineStatisticsDTO;
import de.ostfale.qk.ui.statistics.playerinfo.tournamentdata.TournamentsStatisticDTO;
import org.jboss.logging.Logger;

public class PlayerInfoDTO {

    private static final Logger log = Logger.getLogger(PlayerInfoDTO.class);

    private static final String DATE_TIME_FORMAT = "dd-MM-yyyy HH:mm";

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

    // ranking statistics
    private DisciplineStatisticsDTO singleDisciplineStatistics;
    private DisciplineStatisticsDTO doubleDisciplineStatistics;
    private DisciplineStatisticsDTO mixedDisciplineStatistics;

    // tournaments statistics for favorite players
    private TournamentsStatisticDTO tournamentsStatisticDTO;

    public PlayerInfoDTO(Player player) {
        log.tracef("PlayerInfoDTO :: init from player %d", player.getPlayerId());
        this.playerName = player.getFullName();
        this.gender = player.getGender().toString();
        this.playerId = player.getPlayerId().playerId();
        this.birthYear = String.valueOf(player.getYearOfBirth());
        this.ageClass = player.getPlayerInfo().getAgeClassGeneral();
        this.ageClassDetail = player.getPlayerInfo().getAgeClassSpecific();
        this.clubName = player.getPlayerInfo().getClubName() == null ? "" : player.getPlayerInfo().getClubName();
        this.districtName = player.getPlayerInfo().getDistrictName() == null ? "" : player.getPlayerInfo().getDistrictName();
        this.stateName = player.getPlayerInfo().getStateName();
        this.stateGroup = player.getPlayerInfo().getGroupName() == null ? "" : player.getPlayerInfo().getGroupName().getDisplayName();
    }

    @Override
    public String toString() {
        return playerName;
    }

    public TournamentsStatisticDTO getTournamentsStatisticDTO() {
        return tournamentsStatisticDTO;
    }

    public void setTournamentsStatisticDTO(TournamentsStatisticDTO tournamentsStatisticDTO) {
        this.tournamentsStatisticDTO = tournamentsStatisticDTO;
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
}
