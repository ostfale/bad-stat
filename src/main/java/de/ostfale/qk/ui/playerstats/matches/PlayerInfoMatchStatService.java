package de.ostfale.qk.ui.playerstats.matches;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import de.ostfale.qk.web.internal.TournamentWebService;
import io.quarkus.logging.Log;
import jakarta.inject.Singleton;

import java.util.Map;
import java.util.function.BiConsumer;

@Singleton
public class PlayerInfoMatchStatService {

    private final TournamentWebService webService;

    public PlayerInfoMatchStatService(TournamentWebService webService) {
        this.webService = webService;
    }

    public PlayerTourStatDTO readYearlyTournamentStatistics(PlayerId playerId, PlayerTournamentId tournamentId) throws HtmlParserException {
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

    private void setTournamentStatsForYear(PlayerTourStatDTO playerStats, RecentYears recentYear, String tournamentId) throws HtmlParserException {
        Log.debugf("Reading tournament statistics for player %s, tournament %s, year %s", playerStats.getPlayerId(), tournamentId, recentYear.getValue());

        int tournamentCount = getTournamentCountForYear(recentYear.getValue(), tournamentId);
        yearStatSetters.get(recentYear).accept(playerStats, tournamentCount);
    }

    private int getTournamentCountForYear(int year, String tournamentId) throws HtmlParserException {
        return webService.getNumberOfTournamentsForYearAndPlayer(year, tournamentId);
    }
}
