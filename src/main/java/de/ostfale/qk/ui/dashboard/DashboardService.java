package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.db.dashboard.DashboardRankingData;
import de.ostfale.qk.db.dashboard.DashboardRankingDataJsonHandler;
import de.ostfale.qk.db.internal.player.PlayerOverview;
import de.ostfale.qk.persistence.ranking.RankingPlayerCacheHandler;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import de.ostfale.qk.web.internal.RankingWebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.File;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DashboardService implements TimeHandlerFacade {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    @Inject
    RankingPlayerCacheHandler rankingPlayerCacheHandler;

    @Inject
    DashboardRankingDataJsonHandler dashboardRankingDataJsonHandler;

    @Inject
    RankingDownloader rankingDownloader;

    @Inject
    RankingWebService rankingWebService;

    public boolean loadCurrentCWRankingFile() {
        log.debug("DashboardService :: load current ranking file");
        return rankingDownloader.downloadCurrentCWRankingFile();
    }

    public boolean loadLastCWRankingFile() {
        log.debug("DashboardService :: load last ranking file");
        return rankingDownloader.downloadLastCWRankingFile();
    }

    public DashboardRankingUIModel updateCurrentRankingStatus() {
        log.info("DashboardService :: prepare current status of ranking information");
        var playerCache = rankingPlayerCacheHandler.getRankingPlayerCache();
        if (playerCache != null) {
            DashboardRankingData dashboardRankingData = dashboardRankingDataJsonHandler.readDashboardRankingData();
            PlayerOverview playerOverview = new PlayerOverview(playerCache.getNumberOfPlayers(), playerCache.getNumberOfMalePlayers(), playerCache.getNumberOfFemalePlayers());
            DashboardRankingUIModel model = new DashboardRankingUIModel(playerOverview);
            model.setLastRankingFileDownload(dashboardRankingData.getLastRankingFileDownload());
            model.setDbUpdateInCW(dashboardRankingData.getPlayerCacheLoadedForCW());
            getRankingFile().ifPresent(rFile -> model.setDownloadFileName(rFile.getName()));
            return model;
        }
        return null;
    }

    public DashboardRankingUIModel updateCurrentRankingFile() {
        log.info("DashboardService :: use current ranking file to update database");
        updatePlayersInCache();
        return updateCurrentRankingStatus();
    }

    public int getCurrentCW() {
        log.debug("DashboardService :: retrieve current calendar week");
        return getActualCalendarWeek();
    }

    public int getLastCW() {
        log.debug("DashboardService :: retrieve last calendar week");
        return getLastCalendarWeek();
    }

    public String getOnlineCW() {
        log.debug("DashboardService :: retrieve online calendar week");
        return rankingWebService.getCalendarWeekForLastUpdate();
    }

    private void updatePlayersInCache() {
        log.info("DashboardService :: read ranking file and update cache");
        if (!rankingPlayerCacheHandler.loadExistingRankingFileIntoCache()) {
            log.info("Failed to load existing ranking file into cache ");
        }
    }

    private Optional<File> getRankingFile() {
        log.debug("DashboardService :: retrieve ranking file from dir");
        List<File> rankingFiles = rankingDownloader.getRankingFiles();
        if (rankingFiles.size() == 1) {
            return Optional.of(rankingFiles.getFirst());
        }
        return Optional.empty();
    }
}
