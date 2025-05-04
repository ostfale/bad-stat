package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.persistence.dashboard.DashboardRankingData;
import de.ostfale.qk.persistence.dashboard.DashboardRankingDataJsonHandler;
import de.ostfale.qk.domain.player.PlayerOverview;
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
        DashboardRankingData rankingData = dashboardRankingDataJsonHandler.readDashboardRankingData();
        DashboardRankingUIModel model = createModelFromRankingData(rankingData);
        populatePlayerStatistics(model);
        return model;
    }

    public DashboardRankingUIModel updateCurrentRankingFile() {
        log.info("DashboardService :: use current ranking file to update database");
        updatePlayersInCache();
        return updateCurrentRankingStatus();
    }

    public void updatePlayersInCache() {
        log.info("DashboardService :: read ranking file and update cache");
        if (rankingPlayerCacheHandler.loadLocalRankingFileIntoCache()) {
            log.info("Failed to load existing ranking file into cache ");
        }
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

    private DashboardRankingUIModel createModelFromRankingData(DashboardRankingData rankingData) {
        DashboardRankingUIModel model = new DashboardRankingUIModel();
        model.setDownloadFileName(rankingData.getRankingFileName());
        model.setLastRankingFileDownload(rankingData.getLastRankingFileDownload());
        model.setDbUpdateInCW(rankingData.getPlayerCacheLoadedForCW());
        return model;
    }

    private void populatePlayerStatistics(DashboardRankingUIModel model) {
        var playerRepository = rankingPlayerCacheHandler.getRankingPlayerCache();
        if (playerRepository != null) {
            PlayerOverview playerOverview = new PlayerOverview(
                    playerRepository.getNumberOfPlayers(),
                    playerRepository.getNumberOfMalePlayers(),
                    playerRepository.getNumberOfFemalePlayers());

            model.setNofPlayers(playerOverview.numberOfPlayer());
            model.setNofMalePlayers(playerOverview.numberOfMalePlayer());
            model.setNofFemalePlayers(playerOverview.numberOfFemalePlayer());

            getRankingFile().ifPresent(rankingFile ->
                    model.setDownloadFileName(rankingFile.getName()));
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
