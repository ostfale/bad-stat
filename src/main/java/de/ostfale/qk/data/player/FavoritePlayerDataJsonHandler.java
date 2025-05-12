package de.ostfale.qk.data.player;

import de.ostfale.qk.data.json.JsonDBFacade;
import de.ostfale.qk.data.json.JsonFileReader;
import de.ostfale.qk.data.json.JsonFileWriter;
import de.ostfale.qk.data.player.model.FavoritePlayerData;
import de.ostfale.qk.data.player.model.FavoritePlayerListData;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class FavoritePlayerDataJsonHandler implements JsonDBFacade {

    private static final Logger log = Logger.getLogger(FavoritePlayerDataJsonHandler.class);

    @Inject
    JsonFileWriter<FavoritePlayerListData> jsonFileWriter;

    @Inject
    JsonFileReader<FavoritePlayerListData> jsonFileReader;

    public FavoritePlayerListData readFavoritePlayersList(){
        var targetFilePath = getPlayerCustomDataDir() + FAVORITE_PLAYERS_FILE_NAME;
        var playerCustomDataList = jsonFileReader.readFromFile(targetFilePath, FavoritePlayerListData.class);
        if (playerCustomDataList.isEmpty()) {
            log.debug("PlayerCustomDataJsonHandler :: No player custom data list found -> will be created and returned");
            return new FavoritePlayerListData();
        }
        log.debug("PlayerCustomDataJsonHandler :: player custom data list found -> will be returned");
        return playerCustomDataList.get();
    }

    public void savePlayerCustomDataList(FavoritePlayerListData playerCustomDataList) {
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
