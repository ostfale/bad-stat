package de.ostfale.qk.data.dashboard;

import de.ostfale.qk.app.downloader.ranking.RankingFacade;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@ApplicationScoped
public class RankingPlayerCacheLoader implements RankingFacade {

    @Inject
    RankingParser rankingParser;

    public List<Player> loadRankingPlayerCache() {
        Log.debug("RankingPlayerCacheLoader :: load player data into cache");
        String rankingDirPath = getApplicationRankingDir();
        List<File> rankingFiles = readAllFiles(rankingDirPath);

        if (rankingFiles.isEmpty()) {
            Log.debugf("RankingPlayerCacheLoader :: No ranking files found in directory: %s", rankingDirPath);
            return List.of();
        }

        if (rankingFiles.size() > 1) {
            Log.warnf("RankingPlayerCacheLoader :: Multiple ranking files found, using the first one from: %s", rankingDirPath);
        }

        File localRankingFile = rankingFiles.getFirst();
        return processRankingFile(localRankingFile);
    }

    private List<Player> processRankingFile(File rankingFile) {
        try (FileInputStream rankingFileIS = new FileInputStream(rankingFile)) {
            List<Player> allPlayers = rankingParser.parseRankingFileToPlayers(rankingFileIS);
            Log.debugf("RankingPlayerCacheLoader :: Successfully loaded %d players from ranking file", allPlayers.size());
            return allPlayers;
        } catch (FileNotFoundException e) {
            Log.error("Ranking file not found: {}", rankingFile.getAbsolutePath(), e);
            return List.of();
        } catch (Exception e) {
            Log.error("Failed to process ranking file: {}", rankingFile.getAbsolutePath(), e);
            return List.of();
        }
    }
}
