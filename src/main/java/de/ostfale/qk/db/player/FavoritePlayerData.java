package de.ostfale.qk.db.player;

import java.util.HashMap;
import java.util.Map;

public class FavoritePlayerData {

    private String playerId;
    private String name;
    private String playerTournamentId = "";
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

    public Map<String, Integer> getTournamentsForYear() {
        return tournamentsForYear;
    }

    public void setTournamentsForYear(Map<String, Integer> tournamentsForYear) {
        this.tournamentsForYear = tournamentsForYear;
    }
}
