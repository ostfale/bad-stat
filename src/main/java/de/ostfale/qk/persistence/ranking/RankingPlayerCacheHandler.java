package de.ostfale.qk.persistence.ranking;

import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.domain.player.Player;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.util.List;

@Singleton
public class RankingPlayerCacheHandler implements FileSystemFacade {

    private static final Logger log = Logger.getLogger(RankingPlayerCacheHandler.class);

    @Inject
    RankingParser rankingParser;

    private RankingPlayerCache rankingPlayerCache;


    public boolean loadExistingRankingFileIntoCache() {
        log.debug("Loading existing ranking file into cache");
        String rankingDirPath = getApplicationRankingDir();
        List<File> rankingFiles = readAllFiles(rankingDirPath);

        if (rankingFiles.isEmpty()) {
            log.debugf("No ranking files found in directory: %s", rankingDirPath);
            return false;
        }

        if (rankingFiles.size() > 1) {
            log.warnf("Multiple ranking files found, using the first one from: %s", rankingDirPath);
        }

        return processRankingFile(rankingFiles.getFirst());
    }


    public RankingPlayerCache getRankingPlayerCache() {
        return rankingPlayerCache;
    }

    public void setRankingPlayerCache(RankingPlayerCache rankingPlayerCache) {
        this.rankingPlayerCache = rankingPlayerCache;
    }

    private boolean processRankingFile(File rankingFile) {
        try (FileInputStream rankingFileIS = new FileInputStream(rankingFile)) {
            List<Player> allPlayers = rankingParser.parseRankingFileToPlayers(rankingFileIS);
            setRankingPlayerCache(new RankingPlayerCache(allPlayers));
            log.debugf("Successfully loaded %d players from ranking file", allPlayers.size());
            return true;
        } catch (FileNotFoundException e) {
            log.error("Ranking file not found: {}", rankingFile.getAbsolutePath(), e);
            return false;
        } catch (Exception e) {
            log.error("Failed to process ranking file: {}", rankingFile.getAbsolutePath(), e);
            return false;
        }
    }
}
