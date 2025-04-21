package de.ostfale.qk.db.json;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.FileSystemFacade;
import org.jboss.logging.Logger;

public interface JsonDBFacade extends FileSystemFacade {

    Logger log = Logger.getLogger(JsonDBFacade.class);

    String DASHBOARD_DIR_NAME = "dashboard";
    String DASHBOARD_RANKING_DATA_FILE_NAME = "dashboardRankingData.json";

    String FAV_PLAYERS_DIR_NAME = "favPlayers";

    default String getDashboardDir() {
        var result = getApplicationHomeDir() + SEP + DirTypes.DATA.displayName + SEP + DASHBOARD_DIR_NAME + SEP;
        log.debugf("JsonDBFacade :: Dashboard dir: %s", result);
        return result;
    }
}
