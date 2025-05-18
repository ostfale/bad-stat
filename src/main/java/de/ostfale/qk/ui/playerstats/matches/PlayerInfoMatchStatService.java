package de.ostfale.qk.ui.playerstats.matches;

import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import de.ostfale.qk.web.api.WebService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class PlayerInfoMatchStatService {

    private static final Logger log = Logger.getLogger(PlayerInfoMatchStatService.class);

    @Inject
    WebService webService;

    public PlayerTourStatDTO readYearlyTournamentStatistics(PlayerInfoDTO player) {
        PlayerTourStatDTO stats = new PlayerTourStatDTO(player.getPlayerInfoMasterDataDTO().getPlayerId());
        String tournamentId = player.getPlayerInfoMasterDataDTO().getPlayerTournamentId();

        for (RecentYears year : RecentYears.values()) {
            setTournamentStatForYear(stats, year, tournamentId);
        }
        return stats;
    }

    private void setTournamentStatForYear(PlayerTourStatDTO stats, RecentYears year, String tournamentId) {
        int tournaments = webService.getNumberOfTournamentsForYearAndPlayer(year.getValue(), tournamentId);
        switch (year) {
            case CURRENT_YEAR -> stats.setYearPlayedTournaments(tournaments);
            case YEAR_MINUS_1 -> stats.setYearMinusOnePlayedTournaments(tournaments);
            case YEAR_MINUS_2 -> stats.setYearMinusTwoPlayedTournaments(tournaments);
            case YEAR_MINUS_3 -> stats.setYearMinusThreePlayedTournaments(tournaments);
        }
    }
}
