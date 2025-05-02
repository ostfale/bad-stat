package de.ostfale.qk.db.dashboard;

import de.ostfale.qk.persistence.json.JsonDBFacade;
import de.ostfale.qk.persistence.json.JsonFileReader;
import de.ostfale.qk.persistence.json.JsonFileWriter;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class DashboardRankingDataJsonHandler implements JsonDBFacade {

    private static final Logger log = Logger.getLogger(DashboardRankingDataJsonHandler.class);

    @Inject
    JsonFileWriter<DashboardRankingData> jsonFileWriter;

    @Inject
    JsonFileReader<DashboardRankingData> jsonFileReader;

    public DashboardRankingData readDashboardRankingData() {
        var targetFilePath = getDashboardDir() + DASHBOARD_RANKING_DATA_FILE_NAME;
        var rankingData = jsonFileReader.readFromFile(targetFilePath, DashboardRankingData.class);
        if (rankingData.isEmpty()) {
            log.debug("DashboardRankingDataJsonHandler :: No dashboard ranking data found -> will be created and returned");
            return new DashboardRankingData();
        }
        log.debug("DashboardRankingDataJsonHandler :: dashboard ranking data found -> will be returned");
        return rankingData.get();
    }

    public void saveDashboardRankingData(DashboardRankingData rankingData) {
        var targetFilePath = getDashboardDir() + DASHBOARD_RANKING_DATA_FILE_NAME;
        log.debugf("DashboardRankingDataJsonHandler :: save dashboard ranking data to %s", targetFilePath);
        jsonFileWriter.writeToFile(rankingData, targetFilePath);
    }
}
