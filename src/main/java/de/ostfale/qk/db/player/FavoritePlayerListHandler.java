package de.ostfale.qk.db.player;

import com.fasterxml.jackson.annotation.JsonIgnore;
import org.jboss.logging.Logger;

import java.util.ArrayList;
import java.util.List;

public class FavoritePlayerListHandler {

    Logger log = Logger.getLogger(FavoritePlayerListHandler.class);

    private  List<FavoritePlayerData> favoritePlayersList = new ArrayList<>();

    public List<FavoritePlayerData> favoritePlayersList() {
        return favoritePlayersList;
    }

    public List<FavoritePlayerData> getFavoritePlayersList() {
        return favoritePlayersList;
    }

    @JsonIgnore
    public boolean doesPlayerExist(String playerName) {
        return favoritePlayersList.stream().anyMatch(favoritePlayerData -> favoritePlayerData.getName().equalsIgnoreCase(playerName));
    }

    @JsonIgnore
    public void addPlayerCustomData(FavoritePlayerData favoritePlayerData) {
        log.debugf("PlayerCustomDataListHandler :: addPlayerCustomData(%s)", favoritePlayerData.getPlayerId());
        favoritePlayersList.add(favoritePlayerData);
    }
}
