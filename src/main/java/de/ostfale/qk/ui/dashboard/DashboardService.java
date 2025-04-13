package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.config.ConfigPersistenceService;
import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.internal.player.PlayerOverview;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import de.ostfale.qk.ui.dashboard.model.DashboardUIModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;

@ApplicationScoped
public class DashboardService {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    @Inject
    RankingDownloader rankingDownloader;

    @Inject
    RankingParser rankingParser;

    @Inject
    PlayerServiceProvider playerService;

    @Inject
    ConfigPersistenceService configPersistenceService;

    public DashboardRankingUIModel updateCurrentRankingStatus() {
        log.info("DashboardService :: prepare current status of ranking information");
        PlayerOverview playerOverview = playerService.getPlayerOverview();
        DashboardRankingUIModel model = new DashboardRankingUIModel(playerOverview);
        getRankingFile().ifPresent(rFile-> model.setDownloadFileName(rFile.getName()));
        return model;
    }

    public DashboardRankingUIModel handleDownloadRankingFileActions() {
        log.info("DashboardService :: handle actions to be done with updating ranking file");
        DashboardRankingUIModel model = new DashboardRankingUIModel();
        updateRankingFile().ifPresent(rfile -> {
            model.setDownloadFileName(rfile.getName());
            try {
                List<RankingPlayer> allPlayers = rankingParser.parseRankingFile(new FileInputStream(rfile));
                log.debugf("DashboardService :: Parser found %d player", allPlayers.size());
                allPlayers.parallelStream().forEach(rankingPlayer -> {
                    var player = new Player(rankingPlayer);
                    playerService.savePlayerIfNotExistsOrHasChanged(player);
                });

            } catch (FileNotFoundException e) {
                log.errorf("DashboardService :: Failed parsing excel ranking file: %s", e.getMessage());
            }
        });
        log.info("DashboardService :: No updates for Ranking!");
        return model;
    }

    private Optional<File> updateRankingFile() {
        log.debug("DashboardService :: update ranking file if possible");
        rankingDownloader.downloadRankingFile();
        List<File> rankingFiles = rankingDownloader.getRankingFiles();
        if (rankingFiles.size() == 1) {
            return Optional.of(rankingFiles.getFirst());
        }
        return Optional.empty();
    }

    private Optional<File> getRankingFile() {
        log.debug("DashboardService :: retrieve ranking file from dir");
        List<File> rankingFiles = rankingDownloader.getRankingFiles();
        if (rankingFiles.size() == 1) {
            return Optional.of(rankingFiles.getFirst());
        }
        return Optional.empty();
    }

    public String downloadRankingFile() {
        log.info("Downloading ranking file");
        rankingDownloader.downloadRankingFile();
        List<File> rankingFiles = rankingDownloader.getRankingFiles();
        if (rankingFiles.size() == 1) {
            return rankingFiles.getFirst().getName();
        }
        return "";
    }

    public DashboardUIModel getDashboardUIModel() {
        log.debug("DashboardService :: retrieve DashboardUIModel");
        return null;
    }
}
