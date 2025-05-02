package de.ostfale.qk.persistence.json;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.FileSystemFacade;
import org.jboss.logging.Logger;

public interface JsonDBFacade extends FileSystemFacade {

    Logger log = Logger.getLogger(JsonDBFacade.class);

    String DASHBOARD_DIR_NAME = "dashboard";
    String DASHBOARD_RANKING_DATA_FILE_NAME = "dashboardRankingData.json";

    String FAVORITE_PLAYERS_DIR_NAME = "favPlayer";
    String FAVORITE_PLAYERS_FILE_NAME = "favPlayerList.json";

    default String getDashboardDir() {
        var result = getApplicationHomeDir() + SEP + DirTypes.DATA.displayName + SEP + DASHBOARD_DIR_NAME + SEP;
        log.debugf("JsonDBFacade :: Dashboard dir: %s", result);
        return result;
    }

    default String getPlayerCustomDataDir() {
        var result = getApplicationHomeDir() + SEP + DirTypes.DATA.displayName + SEP + FAVORITE_PLAYERS_DIR_NAME + SEP;
        log.debugf("JsonDBFacade :: PlayerCustomData dir: %s", result);
        return result;
    }
}
