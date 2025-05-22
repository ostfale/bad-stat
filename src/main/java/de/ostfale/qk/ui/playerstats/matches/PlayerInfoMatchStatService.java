package de.ostfale.qk.ui.playerstats.matches;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import de.ostfale.qk.web.api.WebService;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.Map;
import java.util.function.BiConsumer;

@Singleton
public class PlayerInfoMatchStatService {

    private static final Logger log = Logger.getLogger(PlayerInfoMatchStatService.class);

    @Inject
    WebService webService;

    public PlayerTourStatDTO readYearlyTournamentStatistics(PlayerId playerId, PlayerTournamentId tournamentId) {
        PlayerTourStatDTO playerStats = new PlayerTourStatDTO(playerId, tournamentId);
        playerStats.setPlayerTournamentId(tournamentId);

        // Iterate over enum values
        for (RecentYears recentYear : RecentYears.values()) {
            setTournamentStatsForYear(playerStats, recentYear, tournamentId.tournamentId());
        }

        return playerStats;
    }

    private final Map<RecentYears, BiConsumer<PlayerTourStatDTO, Integer>> yearStatSetters = Map.of(
            RecentYears.CURRENT_YEAR, PlayerTourStatDTO::setYearPlayedTournaments,
            RecentYears.YEAR_MINUS_1, PlayerTourStatDTO::setYearMinusOnePlayedTournaments,
            RecentYears.YEAR_MINUS_2, PlayerTourStatDTO::setYearMinusTwoPlayedTournaments,
            RecentYears.YEAR_MINUS_3, PlayerTourStatDTO::setYearMinusThreePlayedTournaments
    );

    private void setTournamentStatsForYear(PlayerTourStatDTO playerStats, RecentYears recentYear, String tournamentId) {
        log.debugf("Reading tournament statistics for player %s, tournament %s, year %s", playerStats.getPlayerId(), tournamentId, recentYear.getValue());

        int tournamentCount = getTournamentCountForYear(recentYear.getValue(), tournamentId);
        yearStatSetters.get(recentYear).accept(playerStats, tournamentCount);
    }

    private int getTournamentCountForYear(int year, String tournamentId) {
        return webService.getNumberOfTournamentsForYearAndPlayer(year, tournamentId);
    }
}
