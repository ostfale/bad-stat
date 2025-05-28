package de.ostfale.qk.data.json;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.FileSystemFacade;
import io.quarkus.logging.Log;

public interface JsonDBFacade extends FileSystemFacade {

    String JSON_SUFFIX = ".json";
    String FILE_NAME_SEPARATOR = "_";

    String DASHBOARD_DIR_NAME = "dashboard";
    String DASHBOARD_RANKING_DATA_FILE_NAME = "dashboardRankingData.json";

    String FAVORITE_PLAYERS_DIR_NAME = "favPlayer";
    String FAVORITE_PLAYERS_FILE_NAME = "favPlayerList.json";

    String FAVORITE_PLAYER_MATCH_DIR_NAME = "matches";

    default String getDashboardDir() {
        var result = getApplicationHomeDir() + SEP + DirTypes.DATA.displayName + SEP + DASHBOARD_DIR_NAME + SEP;
        Log.debugf("JsonDBFacade :: Dashboard dir: %s", result);
        return result;
    }

    default String getPlayerCustomDataDir() {
        var result = getApplicationHomeDir() + SEP + DirTypes.DATA.displayName + SEP + FAVORITE_PLAYERS_DIR_NAME + SEP;
        Log.debugf("JsonDBFacade :: PlayerCustomData dir: %s", result);
        return result;
    }

    default String getFavPlayerMatchesDir() {
        var dirName = getPlayerCustomDataDir() + FAVORITE_PLAYER_MATCH_DIR_NAME + SEP;
        Log.debugf("JsonDBFacade :: FavPlayerMatches dir: %s", dirName);
        return dirName;
    }

    default String createFavPlayerTournamentMatchesFileName(String playerName, String playerId, String year) {
        var fileName = playerId + FILE_NAME_SEPARATOR + playerName + FILE_NAME_SEPARATOR + year + JSON_SUFFIX;
        Log.infof("JsonDBFacade :: FavPlayerTournamentMatchesFileName: %s", fileName);
        return fileName;
    }
}


