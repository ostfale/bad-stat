package de.ostfale.qk.ui.statistics.model;

import org.jboss.logging.Logger;

public class PlayerInfoStatisticsDTO {

    private static final Logger log = Logger.getLogger(PlayerInfoStatisticsDTO.class);

    private String playerName;
    private String playerId;



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
}
