package de.ostfale.qk.app;

import de.ostfale.qk.db.api.PlayerRepository;
import de.ostfale.qk.db.internal.Player;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.parser.ranking.internal.RankingPlayer;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import jakarta.transaction.Transactional;
import org.jboss.logging.Logger;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;

@Singleton
public class DevSimulation {

    private static final Logger log = Logger.getLogger(DevSimulation.class);

    private static final String PLAYER_RANKING_FILE = "src/main/resources/simulation/Ranking_2025_Sim.xlsx";

    @Inject
    RankingParser rankingParser;

    @Inject
    PlayerRepository playerRepository;

    public void loadSimulationData() {
        log.info("Load simulation data");

        Path path = Paths.get(PLAYER_RANKING_FILE);
        List<RankingPlayer> rankingPlayerList = rankingParser.parseRankingFile(path.toFile());
        log.infof("Loaded %d players", rankingPlayerList.size());
        savePlayer(rankingPlayerList);
    }

    @Transactional
    public void savePlayer(List<RankingPlayer> rankingPlayerList) {
        log.infof("Number of players to save: %d", rankingPlayerList.size());
        for (RankingPlayer rankingPlayer : rankingPlayerList) {
            playerRepository.persist(new Player(rankingPlayer));
            log.infof("Saved player: %s", rankingPlayer.getName());
        }
    }
}
