package de.ostfale.qk.ui.playerstats.info.tournamentdata;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;

import java.util.ArrayList;
import java.util.List;

public class PlayerTourStatDTO {

    private PlayerId playerId;
    private PlayerTournamentId tournamentId;
    private Integer yearPlayedTournaments = 0;
    private Integer yearDownloadedTournaments = 0;
    private Integer yearMinusOnePlayedTournaments = 0;
    private Integer yearMinusOneDownloadedTournaments = 0;
    private Integer yearMinusTwoPlayedTournaments = 0;
    private Integer yearMinusTwoDownloadedTournaments = 0;
    private Integer yearMinusThreePlayedTournaments = 0;
    private Integer yearMinusThreeDownloadedTournaments = 0;

    public PlayerTourStatDTO(PlayerId playerId, PlayerTournamentId tournamentId) {
        this.playerId = playerId;
        this.tournamentId = tournamentId;
    }

    public List<String> getTournamentsStatisticAsString() {
        List<String> tournamentsStatistic = new ArrayList<>();
        tournamentsStatistic.add(yearPlayedTournaments.toString() + " / " + yearDownloadedTournaments.toString());
        tournamentsStatistic.add(yearMinusOnePlayedTournaments.toString() + " / " + yearMinusOneDownloadedTournaments.toString());
        tournamentsStatistic.add(yearMinusTwoPlayedTournaments.toString() + " / " + yearMinusTwoDownloadedTournaments.toString());
        tournamentsStatistic.add(yearMinusThreePlayedTournaments.toString() + " / " + yearMinusThreeDownloadedTournaments.toString());
        return tournamentsStatistic;
    }

    public PlayerTournamentId getPlayerTournamentId() {
        return tournamentId;
    }

    public void setPlayerTournamentId(PlayerTournamentId playerTournamentId) {
        this.tournamentId = playerTournamentId;
    }

    public PlayerId getPlayerId() {
        return playerId;
    }

    public void setPlayerId(PlayerId playerId) {
        this.playerId = playerId;
    }

    public Integer getYearPlayedTournaments() {
        return yearPlayedTournaments;
    }

    public void setYearPlayedTournaments(Integer yearPlayedTournaments) {
        this.yearPlayedTournaments = yearPlayedTournaments;
    }

    public Integer getYearDownloadedTournaments() {
        return yearDownloadedTournaments;
    }

    public void setYearDownloadedTournaments(Integer yearDownloadedTournaments) {
        this.yearDownloadedTournaments = yearDownloadedTournaments;
    }

    public Integer getYearMinusOnePlayedTournaments() {
        return yearMinusOnePlayedTournaments;
    }

    public void setYearMinusOnePlayedTournaments(Integer yearMinusOnePlayedTournaments) {
        this.yearMinusOnePlayedTournaments = yearMinusOnePlayedTournaments;
    }

    public Integer getYearMinusOneDownloadedTournaments() {
        return yearMinusOneDownloadedTournaments;
    }

    public void setYearMinusOneDownloadedTournaments(Integer yearMinusOneDownloadedTournaments) {
        this.yearMinusOneDownloadedTournaments = yearMinusOneDownloadedTournaments;
    }

    public Integer getYearMinusTwoPlayedTournaments() {
        return yearMinusTwoPlayedTournaments;
    }

    public void setYearMinusTwoPlayedTournaments(Integer yearMinusTwoPlayedTournaments) {
        this.yearMinusTwoPlayedTournaments = yearMinusTwoPlayedTournaments;
    }

    public Integer getYearMinusTwoDownloadedTournaments() {
        return yearMinusTwoDownloadedTournaments;
    }

    public void setYearMinusTwoDownloadedTournaments(Integer yearMinusTwoDownloadedTournaments) {
        this.yearMinusTwoDownloadedTournaments = yearMinusTwoDownloadedTournaments;
    }

    public Integer getYearMinusThreePlayedTournaments() {
        return yearMinusThreePlayedTournaments;
    }

    public void setYearMinusThreePlayedTournaments(Integer yearMinusThreePlayedTournaments) {
        this.yearMinusThreePlayedTournaments = yearMinusThreePlayedTournaments;
    }

    public Integer getYearMinusThreeDownloadedTournaments() {
        return yearMinusThreeDownloadedTournaments;
    }

    public void setYearMinusThreeDownloadedTournaments(Integer yearMinusThreeDownloadedTournaments) {
        this.yearMinusThreeDownloadedTournaments = yearMinusThreeDownloadedTournaments;
    }
}
