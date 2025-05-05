package de.ostfale.qk.ui.playerstats.info.masterdata;

import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.ui.playerstats.info.rankingdata.DisciplineStatisticsDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.TournamentsStatisticDTO;
import org.jboss.logging.Logger;

public class PlayerInfoDTO {

    private static final Logger log = Logger.getLogger(PlayerInfoDTO.class);

    // all players static master data
    private PlayerInfoMasterDataDTO  playerInfoMasterDataDTO;

    // tournaments statistics for favorite players
    private TournamentsStatisticDTO tournamentsStatisticDTO;

    // ranking statistics
    private DisciplineStatisticsDTO singleDisciplineStatistics;
    private DisciplineStatisticsDTO doubleDisciplineStatistics;
    private DisciplineStatisticsDTO mixedDisciplineStatistics;



    public PlayerInfoDTO(Player player) {
        log.tracef("PlayerInfoDTO :: init from player %d", player.getPlayerId());
        this.playerInfoMasterDataDTO = new PlayerInfoMasterDataDTO(player);
    }

    @Override
    public String toString() {
        return playerInfoMasterDataDTO.getPlayerName();
    }

    public PlayerInfoMasterDataDTO getPlayerInfoMasterDataDTO() {
        return playerInfoMasterDataDTO;
    }

    public void setPlayerInfoMasterDataDTO(PlayerInfoMasterDataDTO playerInfoMasterDataDTO) {
        this.playerInfoMasterDataDTO = playerInfoMasterDataDTO;
    }

    public TournamentsStatisticDTO getTournamentsStatisticDTO() {
        return tournamentsStatisticDTO;
    }

    public void setTournamentsStatisticDTO(TournamentsStatisticDTO tournamentsStatisticDTO) {
        this.tournamentsStatisticDTO = tournamentsStatisticDTO;
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
}
