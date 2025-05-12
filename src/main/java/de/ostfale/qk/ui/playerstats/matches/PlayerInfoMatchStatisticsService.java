package de.ostfale.qk.ui.playerstats.matches;

import de.ostfale.qk.domain.tournament.RecentYears;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.data.player.model.TournamentsStatisticData;
import de.ostfale.qk.web.api.WebService;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.util.List;
import java.util.stream.Stream;

@Singleton
public class PlayerInfoMatchStatisticsService {

    private static final Logger log = Logger.getLogger(PlayerInfoMatchStatisticsService.class);

    @Inject
    WebService webService;

    public List<TournamentsStatisticData> readPlayersTournamentsForLastFourYears(PlayerInfoDTO player) {
        return Stream.of(RecentYears.values())
                .map(year -> createTournamentStatistics(year, player))
                .toList();
    }

    private TournamentsStatisticData createTournamentStatistics(RecentYears year, PlayerInfoDTO player) {
        int yearValue = year.getValue();
        String playerTournamentId = player.getPlayerInfoMasterDataDTO().getPlayerTournamentId();

        Integer tournamentCount = webService.getNumberOfTournamentsForYearAndPlayer(yearValue, playerTournamentId);
        log.debugf("Read tournaments for player %s for year %d: %d", player.toString(), yearValue, tournamentCount);
        // TODO add here the value for the already persisted matches from the last years
        return new TournamentsStatisticData(yearValue, tournamentCount, 0);
    }
}
