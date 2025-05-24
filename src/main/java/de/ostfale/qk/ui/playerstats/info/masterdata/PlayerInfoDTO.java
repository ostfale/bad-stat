package de.ostfale.qk.ui.playerstats.info.masterdata;

import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.ui.playerstats.info.rankingdata.PlayerDiscStatDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import io.quarkus.logging.Log;

public class PlayerInfoDTO {

    // all players static master data
    private PlayerInfoMasterDTO playerInfoMasterDTO;

    // tournaments statistics for favorite players
    private PlayerTourStatDTO playerTourStatDTO;

    // ranking statistics
    private PlayerDiscStatDTO singleDiscStat;
    private PlayerDiscStatDTO doubleDiscStat;
    private PlayerDiscStatDTO mixedDiscStat;


    public PlayerTourStatDTO getPlayerTourStatDTO() {
        return playerTourStatDTO;
    }

    public void setPlayerTourStatDTO(PlayerTourStatDTO playerTourStatDTO) {
        this.playerTourStatDTO = playerTourStatDTO;
    }

    public PlayerInfoDTO(Player player) {
        Log.tracef("PlayerInfoDTO :: init from player %d", player.getPlayerId());
        this.playerInfoMasterDTO = new PlayerInfoMasterDTO(player);
    }

    @Override
    public String toString() {
        return playerInfoMasterDTO.getPlayerName();
    }

    public PlayerInfoMasterDTO getPlayerInfoMasterDataDTO() {
        return playerInfoMasterDTO;
    }

    public void setPlayerInfoMasterDataDTO(PlayerInfoMasterDTO playerInfoMasterDTO) {
        this.playerInfoMasterDTO = playerInfoMasterDTO;
    }

    public PlayerTourStatDTO getTournamentsStatisticDTO() {
        return playerTourStatDTO;
    }

    public void setTournamentsStatisticDTO(PlayerTourStatDTO playerTourStatDTO) {
        this.playerTourStatDTO = playerTourStatDTO;
    }

    public Integer getSinglePoints() {
        return singleDiscStat.points();
    }

    public Integer getDoublePoints() {
        return doubleDiscStat.points();
    }

    public Integer getMixedPoints() {
        return mixedDiscStat.points();
    }

    public PlayerDiscStatDTO getSingleDiscStat() {
        return singleDiscStat;
    }

    public void setSingleDiscStat(PlayerDiscStatDTO singleDiscStat) {
        this.singleDiscStat = singleDiscStat;
    }

    public PlayerDiscStatDTO getDoubleDiscStat() {
        return doubleDiscStat;
    }

    public void setDoubleDiscStat(PlayerDiscStatDTO doubleDiscStat) {
        this.doubleDiscStat = doubleDiscStat;
    }

    public PlayerDiscStatDTO getMixedDiscStat() {
        return mixedDiscStat;
    }

    public void setMixedDiscStat(PlayerDiscStatDTO mixedDiscStat) {
        this.mixedDiscStat = mixedDiscStat;
    }
}
