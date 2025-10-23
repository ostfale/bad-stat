package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.app.PlannedTournamentsDownloader;
import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.app.cache.RankingPlayerCache;
import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.data.dashboard.DashboardRankingDataJsonHandler;
import de.ostfale.qk.data.dashboard.model.DashboardRankingData;
import de.ostfale.qk.domain.player.PlayerOverview;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import de.ostfale.qk.web.internal.RankingWebService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.File;
import java.nio.file.Path;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;

@ApplicationScoped
public class DashboardService implements TimeHandlerFacade, FileSystemFacade {

    private static final String UNKNOWN_CW_ONLINE_VALUE = "???";

    @Inject
    RankingPlayerCache rankingPlayerCache;

    @Inject
    DashboardRankingDataJsonHandler dashboardRankingDataJsonHandler;

    @Inject
    RankingDownloader rankingDownloader;

    @Inject
    RankingWebService rankingWebService;

    @Inject
    PlannedTournamentsDownloader plannedTournamentsDownloader;

    public CompletableFuture<Void> loadPlannedTournamentsAsync() {
        var target = plannedTournamentsDownloader.prepareDownloadTargetPath(DirTypes.TOURNAMENT.displayName);
        deleteAllFiles(target);

        var yearFileData = getCurrentAndNextYearStrings();
        var thisYearFileData = yearFileData.getFirst();
        var nextYearFileData = yearFileData.getLast();
        return plannedTournamentsDownloader.downloadTwoFiles(thisYearFileData.url, nextYearFileData.url, thisYearFileData.targetPath, nextYearFileData.targetPath);
    }

    public boolean loadCurrentCWRankingFile() {
        Log.debug("DashboardService :: load current ranking file");
        return rankingDownloader.downloadCurrentCWRankingFile();
    }

    public boolean loadLastCWRankingFile() {
        Log.debug("DashboardService :: load last ranking file");
        return rankingDownloader.downloadLastCWRankingFile();
    }

    public DashboardRankingUIModel updateCurrentRankingStatus() {
        Log.info("DashboardService :: prepare current status of ranking information");
        DashboardRankingData rankingData = dashboardRankingDataJsonHandler.readDashboardRankingData();
        DashboardRankingUIModel model = createModelFromRankingData(rankingData);
        populatePlayerStatistics(model);
        return model;
    }

    public DashboardRankingUIModel updateCurrentRankingFile() {
        Log.info("DashboardService :: use current ranking file to update database");
        updatePlayersInCache();
        return updateCurrentRankingStatus();
    }

    public void updatePlayersInCache() {
        Log.info("DashboardService :: read ranking file and update cache");
        rankingPlayerCache.loadPlayerIntoCache();
    }

    public int getCurrentCW() {
        Log.debug("DashboardService :: retrieve current calendar week");
        return getActualCalendarWeek();
    }

    public int getLastCW() {
        Log.debug("DashboardService :: retrieve last calendar week");
        return getLastCalendarWeek();
    }

    public String getOnlineCW() {
        Log.debug("DashboardService :: retrieve online calendar week");
        return rankingWebService.getCalendarWeekForLastUpdate() != null ? rankingWebService.getCalendarWeekForLastUpdate() : UNKNOWN_CW_ONLINE_VALUE;
    }

    private DashboardRankingUIModel createModelFromRankingData(DashboardRankingData rankingData) {
        DashboardRankingUIModel model = new DashboardRankingUIModel();
        model.setDownloadFileName(rankingData.getRankingFileName());
        model.setLastRankingFileDownload(rankingData.getLastRankingFileDownload());
        model.setDbUpdateInCW(rankingData.getPlayerCacheLoadedForCW());
        return model;
    }

    private void populatePlayerStatistics(DashboardRankingUIModel model) {
        PlayerOverview playerOverview = new PlayerOverview(
                rankingPlayerCache.getNumberOfPlayers(),
                rankingPlayerCache.getNumberOfMalePlayers(),
                rankingPlayerCache.getNumberOfFemalePlayers());

        model.setNofPlayers(playerOverview.numberOfPlayer());
        model.setNofMalePlayers(playerOverview.numberOfMalePlayer());
        model.setNofFemalePlayers(playerOverview.numberOfFemalePlayer());

        getRankingFile().ifPresent(rankingFile ->
                model.setDownloadFileName(rankingFile.getName()));
    }

    private Optional<File> getRankingFile() {
        Log.debug("DashboardService :: retrieve ranking file from dir");
        List<File> rankingFiles = rankingDownloader.getRankingFiles();
        if (rankingFiles.size() == 1) {
            return Optional.of(rankingFiles.getFirst());
        }
        return Optional.empty();
    }

    private List<YearUrlAndFileName> getCurrentAndNextYearStrings() {
        var currentYear = getActualCalendarYear();
        var nextYear = currentYear + 1;

        var currentYearData = createYearUrlAndFileName(String.valueOf(currentYear));
        var nextYearData = createYearUrlAndFileName(String.valueOf(nextYear));

        return List.of(currentYearData, nextYearData);
    }

    private YearUrlAndFileName createYearUrlAndFileName(String yearString) {
        var url = plannedTournamentsDownloader.prepareDownloadUrl(yearString);
        var fileName = plannedTournamentsDownloader.prepareDownloadFileName(yearString);
        var targetDir = plannedTournamentsDownloader.prepareDownloadTargetPath(DirTypes.TOURNAMENT.displayName);
        var targetPath = Path.of(targetDir, fileName);

        return new YearUrlAndFileName(yearString, url, targetPath);
    }

    private record YearUrlAndFileName(String year, String url, Path targetPath) {
    }
}

