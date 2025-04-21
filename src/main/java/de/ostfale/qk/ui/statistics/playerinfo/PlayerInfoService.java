package de.ostfale.qk.ui.statistics.playerinfo;

import de.ostfale.qk.persistence.ranking.RankingPlayerCacheHandler;
import de.ostfale.qk.ui.dashboard.DashboardService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerInfoService {

    private static final Logger log = Logger.getLogger(DashboardService.class);

    @Inject
    RankingPlayerCacheHandler rankingPlayerCacheHandler;

    public List<PlayerInfoDTO> getPlayerInfoList() {
        log.debug("PlayerInfoService :: map all players from cache into PlayerInfoDTOs ");
        var rankingPlayerCache = rankingPlayerCacheHandler.getRankingPlayerCache();
        if (rankingPlayerCache != null) {
           return rankingPlayerCache.players().stream().map(PlayerInfoDTO::new).toList();
        }
        return List.of();
    }
}
