package de.ostfale.qk.db.player;

import de.ostfale.qk.ui.statistics.playerinfo.tournamentdata.TournamentsStatistic;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritePlayerData {

    private String playerId;
    private String name;
    private String playerTournamentId = "";
    private final List<TournamentsStatistic> tournamentsStatistics = new ArrayList<>();

    public List<TournamentsStatistic> getTournamentsStatisticsDTOS() {
        return tournamentsStatistics;
    }

    public void addTournamentsStatisticsDTO(TournamentsStatistic tournamentsStatistic) {
        tournamentsStatistics.add(tournamentsStatistic);
    }

    // TODO check here for updates within a year or a new years entry
    public void addTournamentsStatisticsDTO(List<TournamentsStatistic> tournamentsStatistics) {
        for (TournamentsStatistic tournamentsStatistic : tournamentsStatistics) {
            addTournamentsStatisticsDTO(tournamentsStatistic);
        }
    }

    public String getPlayerId() {
        return playerId;
    }

    public void setPlayerId(String playerId) {
        this.playerId = playerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPlayerTournamentId() {
        return playerTournamentId;
    }

    public void setPlayerTournamentId(String playerTournamentId) {
        this.playerTournamentId = playerTournamentId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        FavoritePlayerData that = (FavoritePlayerData) o;
        return Objects.equals(playerId, that.playerId)
                && Objects.equals(name, that.name)
                && Objects.equals(playerTournamentId, that.playerTournamentId)
                && Objects.equals(tournamentsStatistics, that.tournamentsStatistics);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, playerTournamentId);
    }
}
