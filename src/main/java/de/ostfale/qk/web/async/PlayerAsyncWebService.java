package de.ostfale.qk.web.async;

import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.player.PlayerTournamentId;
import de.ostfale.qk.parser.HtmlParserException;
import de.ostfale.qk.ui.playerstats.info.tournamentdata.PlayerTourStatDTO;
import de.ostfale.qk.ui.playerstats.matches.PlayerInfoMatchStatService;
import de.ostfale.qk.web.player.PlayerWebParserService;
import io.quarkus.logging.Log;
import io.smallrye.mutiny.Uni;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class PlayerAsyncWebService {

    @Inject
    PlayerWebParserService playerWebParserService;

    @Inject
    PlayerInfoMatchStatService playerInfoMatchStatService;

    public Uni<PlayerTournamentId> fetchPlayerTournamentId(String playerId) {
        Log.debugf("PlayerAsyncWebService :: Get player tournament id for %s", playerId);
        return Uni.createFrom().item(playerWebParserService.getPlayerTournamentId(playerId));
    }

    public Uni<PlayerTourStatDTO> fetchPlayerTourStatInfo(PlayerId playerId, PlayerTournamentId playerTournamentId) throws HtmlParserException {
        Log.debug("PlayerAsyncWebService :: fetch player tour stat info ");
        return Uni.createFrom().item(playerInfoMatchStatService.readYearlyTournamentStatistics(playerId, playerTournamentId));
    }
}
