package de.ostfale.qk.data.player.model;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class FavoritePlayerData {

    private String playerId;
    private String name;
    private String playerTournamentId = "";
    private final List<TournamentsStatisticData> tournamentsStatisticData = new ArrayList<>();

    public List<TournamentsStatisticData> getTournamentsStatisticsDTOS() {
        return tournamentsStatisticData;
    }

    public void addTournamentsStatisticsDTO(TournamentsStatisticData tournamentsStatisticData) {
        this.tournamentsStatisticData.add(tournamentsStatisticData);
    }

    // TODO check here for updates within a year or a new years entry
    public void addTournamentsStatisticsDTO(List<TournamentsStatisticData> aTournamentsStatisticData) {
        for (TournamentsStatisticData tournamentsStatisticData : aTournamentsStatisticData) {
            addTournamentsStatisticsDTO(tournamentsStatisticData);
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
                && Objects.equals(tournamentsStatisticData, that.tournamentsStatisticData);
    }

    @Override
    public int hashCode() {
        return Objects.hash(playerId, playerTournamentId);
    }
}
