package de.ostfale.qk.data.player;

import de.ostfale.qk.data.json.JsonDBFacade;
import de.ostfale.qk.data.json.JsonFileReader;
import de.ostfale.qk.data.json.JsonFileWriter;
import de.ostfale.qk.data.player.model.FavPlayerListData;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

@Singleton
public class FavoritePlayerDataJsonHandler implements JsonDBFacade {

    @Inject
    JsonFileWriter<FavPlayerListData> jsonFileWriter;

    @Inject
    JsonFileReader<FavPlayerListData> jsonFileReader;

    public FavPlayerListData readFavoritePlayersList(){
        var targetFilePath = getPlayerCustomDataDir() + FAVORITE_PLAYERS_FILE_NAME;
        var playerCustomDataList = jsonFileReader.readFromFile(targetFilePath, FavPlayerListData.class);
        if (playerCustomDataList.isEmpty()) {
            Log.debug("PlayerCustomDataJsonHandler :: No player custom data list found -> will be created and returned");
            return new FavPlayerListData();
        }
        Log.debug("PlayerCustomDataJsonHandler :: player custom data list found -> will be returned");
        return playerCustomDataList.get();
    }

    public void writeFavoritePlayersList(FavPlayerListData favPlayerListData) {
        var targetFilePath = getPlayerCustomDataDir() + FAVORITE_PLAYERS_FILE_NAME;
        Log.debugf("PlayerCustomDataJsonHandler :: save player custom data to %s", targetFilePath);
        jsonFileWriter.writeToFile(favPlayerListData, targetFilePath);
    }
}
