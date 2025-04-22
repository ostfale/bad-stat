package de.ostfale.qk.db.player;

import java.util.HashMap;
import java.util.Map;

public class PlayerCustomData {

    private String playerId;
    private String name;
    private String playerTournamentId = "";
    private Boolean isFavorite = false;
    private Map<String, Integer> tournamentsForYear = new HashMap<>();


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

    public Boolean getFavorite() {
        return isFavorite;
    }

    public void setFavorite(Boolean favorite) {
        isFavorite = favorite;
    }

    public Map<String, Integer> getTournamentsForYear() {
        return tournamentsForYear;
    }

    public void setTournamentsForYear(Map<String, Integer> tournamentsForYear) {
        this.tournamentsForYear = tournamentsForYear;
    }
}
