package de.ostfale.qk.ui.playerstats.matches;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import io.quarkus.logging.Log;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.function.BiConsumer;

@Singleton
public class PlayerInfoMatchStatService {

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
        Log.debugf("Reading tournament statistics for player %s, tournament %s, year %s", playerStats.getPlayerId(), tournamentId, recentYear.getValue());

        int tournamentCount = getTournamentCountForYear(recentYear.getValue(), tournamentId);
        yearStatSetters.get(recentYear).accept(playerStats, tournamentCount);
    }

    private int getTournamentCountForYear(int year, String tournamentId) {
        // TODO Fix nof Tournaments
        //return webService.getNumberOfTournamentsForYearAndPlayer(year, tournamentId);
        return 42;
    }
}
