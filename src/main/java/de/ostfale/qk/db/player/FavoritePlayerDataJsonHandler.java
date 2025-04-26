package de.ostfale.qk.db.player;

import de.ostfale.qk.db.json.JsonDBFacade;
import de.ostfale.qk.db.json.JsonFileReader;
import de.ostfale.qk.db.json.JsonFileWriter;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class FavoritePlayerDataJsonHandler implements JsonDBFacade {

    private static final Logger log = Logger.getLogger(FavoritePlayerDataJsonHandler.class);

    @Inject
    JsonFileWriter<FavoritePlayerListHandler> jsonFileWriter;

    @Inject
    JsonFileReader<FavoritePlayerListHandler> jsonFileReader;

    public FavoritePlayerListHandler readFavoritePlayersList(){
        var targetFilePath = getPlayerCustomDataDir() + FAVORITE_PLAYERS_FILE_NAME;
        var playerCustomDataList = jsonFileReader.readFromFile(targetFilePath, FavoritePlayerListHandler.class);
        if (playerCustomDataList.isEmpty()) {
            log.debug("PlayerCustomDataJsonHandler :: No player custom data list found -> will be created and returned");
            return new FavoritePlayerListHandler();
        }
        log.debug("PlayerCustomDataJsonHandler :: player custom data list found -> will be returned");
        return playerCustomDataList.get();
    }

    public void savePlayerCustomDataList(FavoritePlayerListHandler playerCustomDataList) {
        var targetFilePath = getPlayerCustomDataDir() + FAVORITE_PLAYERS_FILE_NAME;
        log.debugf("PlayerCustomDataJsonHandler :: save player custom data to %s", targetFilePath);
        jsonFileWriter.writeToFile(playerCustomDataList, targetFilePath);
    }

    public void savePlayerCustomData(FavoritePlayerData favoritePlayerData) {
        log.debugf("PlayerCustomDataJsonHandler :: save player custom data %s", favoritePlayerData.getPlayerId());
        var playerCustomDataList = readFavoritePlayersList();
        playerCustomDataList.favoritePlayersList().add(favoritePlayerData);
        savePlayerCustomDataList(playerCustomDataList);
    }
}
