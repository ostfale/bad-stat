package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.db.app.BadStatConfig;
import de.ostfale.qk.db.app.BadStatConfigService;
import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.internal.player.PlayerOverview;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import de.ostfale.qk.web.internal.RankingWebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

@ApplicationScoped
public class DashboardService implements TimeHandlerFacade {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    @Inject
    RankingDownloader rankingDownloader;

    @Inject
    RankingParser rankingParser;

    @Inject
    PlayerServiceProvider playerService;

    @Inject
    RankingWebService rankingWebService;

    @Inject
    BadStatConfigService badStatConfigService;

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
        PlayerOverview playerOverview = playerService.getPlayerOverview();
        DashboardRankingUIModel model = new DashboardRankingUIModel(playerOverview);
        BadStatConfig badStatConfig = badStatConfigService.readConfiguration();
        model.setLastRankingFileDownload(badStatConfig.getLastRankingFileDownload());
        model.setDbUpdateInCW(badStatConfig.getDatabaseCW());
        getRankingFile().ifPresent(rFile -> model.setDownloadFileName(rFile.getName()));
        return model;
    }

    public DashboardRankingUIModel updateCurrentRankingFile() {
        log.info("DashboardService :: use current ranking file to update database");
        updatePlayersInDatabase();
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

    private void updatePlayersInDatabase() {
        log.info("DashboardService :: handle actions to be done with updating ranking file");
        Optional<File> rankingFile = getRankingFile();
        if (rankingFile.isPresent()) {
            File rFile = rankingFile.get();
                updateDBSourceCW(rFile);
            try {
                List<RankingPlayer> allPlayers = rankingParser.parseRankingFile(new FileInputStream(rFile));
                List<Player> playerList = allPlayers.stream().map(Player::new).toList();
                if (playerService.getNofPlayers() == 0) {
                    log.infof("DashboardService :: Save all %d player!", playerList.size());
                    playerService.save(playerList);
                } else {
                    log.info("DashboardService :: Update players in database");
                    playerList.forEach(player -> {
                        Player existingPlayer = playerService.findPlayerById(player.getPlayerId());
                        if (!player.equals(existingPlayer)){
                            playerService.updatePlayer(player);
                        }
                    });
                }
            } catch (FileNotFoundException e) {
                log.errorf("DashboardService :: Failed parsing excel ranking file: %s", e.getMessage());
            }
        }
    }

    private void updateDBSourceCW(File rFile) {
        String rankingFileCW = rankingDownloader.getCalendarWeekFromRankingFile(rFile);
        BadStatConfig badStatConfig = badStatConfigService.readConfiguration();
        badStatConfig.setDatabaseCW(rankingFileCW);
        badStatConfigService.saveConfig(badStatConfig);
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
