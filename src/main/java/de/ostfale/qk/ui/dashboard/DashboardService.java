package de.ostfale.qk.ui.dashboard;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;
import java.util.Optional;

import org.jboss.logging.Logger;

import de.ostfale.qk.app.downloader.ranking.RankingDownloader;
import de.ostfale.qk.db.app.BadStatConfigService;
import de.ostfale.qk.db.internal.player.Player;
import de.ostfale.qk.db.internal.player.PlayerOverview;
import de.ostfale.qk.db.service.PlayerServiceProvider;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import de.ostfale.qk.ui.dashboard.model.DashboardRankingUIModel;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

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
    BadStatConfigService badStatConfigService;

    public DashboardRankingUIModel updateCurrentRankingStatus() {
        log.info("DashboardService :: prepare current status of ranking information");
        PlayerOverview playerOverview = playerService.getPlayerOverview();
        DashboardRankingUIModel model = new DashboardRankingUIModel(playerOverview);
        model.setLastRankingFileDownload(badStatConfigService.readConfiguration().getLastRankingFileDownload());
        getRankingFile().ifPresent(rFile -> model.setDownloadFileName(rFile.getName()));
        return model;
    }

    public void updateCurrentRankingFile() {
        log.info("DashboardService :: check if ranking is up-to-date and download newer file if available");
        if (rankingDownloader.downloadRankingFileDialog() == true) {
            updatePlayersInDatabase();
        }
    }

    private void updatePlayersInDatabase() {
        log.info("DashboardService :: handle actions to be done with updating ranking file");

        Optional<File> rankingFile = getRankingFile();
        if (rankingFile.isPresent()) {
            File rFile = rankingFile.get();
            List<RankingPlayer> allPlayers;
            try {
                allPlayers = rankingParser.parseRankingFile(new FileInputStream(rFile));
                List<Player> playerList = allPlayers.stream().map(Player::new).toList();
                if (playerService.getNofPlayers() == 0) {
                    log.infof("DashboardService :: Save all %d player!", playerList.size());
                    playerService.save(playerList);
                    return;
                }

            } catch (FileNotFoundException e) {
                log.errorf("DashboardService :: Failed parsing excel ranking file: %s", e.getMessage());
            }
        }

        /*
         * getRankingFile().ifPresent(rfile -> { try { List<RankingPlayer> allPlayers =
         * rankingParser.parseRankingFile(new FileInputStream(rfile)); List<Player>
         * playerList = allPlayers.stream().map(Player::new).toList();
         * 
         * if (playerService.getNofPlayers() == 0) {
         * log.infof("DashboardService :: Save %d player!", playerList.size());
         * playerService.save(playerList); return; }
         * 
         * log.debugf("DashboardService :: Parser found %d player", allPlayers.size());
         * allPlayers.stream().forEach(rankingPlayer -> {
         * playerService.savePlayerIfNotExistsOrHasChanged(rankingPlayer); }); } catch
         * (FileNotFoundException e) {
         * log.errorf("DashboardService :: Failed parsing excel ranking file: %s",
         * e.getMessage()); } });
         */
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
