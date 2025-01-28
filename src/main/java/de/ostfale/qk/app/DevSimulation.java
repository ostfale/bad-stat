package de.ostfale.qk.app;

import de.ostfale.qk.db.api.PlayerRepository;
import de.ostfale.qk.db.internal.PlayerEntity;
import de.ostfale.qk.parser.ranking.api.RankingParser;
import de.ostfale.qk.parser.ranking.internal.Player;
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
        List<Player> playerList = rankingParser.parseRankingFile(path.toFile());
        log.infof("Loaded %d players", playerList.size());
        savePlayer(playerList);
    }

    @Transactional
    public void savePlayer(List<Player> playerList) {
        log.infof("Number of players to save: %d", playerList.size());
        for (Player player : playerList) {
            playerRepository.persist(new PlayerEntity(player));
            log.infof("Saved player: %s", player.getName());
        }
    }
}
