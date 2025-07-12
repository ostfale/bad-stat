package de.ostfale.qk.ui.playerstats.info.tournamentdata;

import de.ostfale.qk.data.player.PlayerTournamentMatchesJsonHandler;
import de.ostfale.qk.domain.converter.TournamentModelToUIConverter;
import de.ostfale.qk.domain.player.PlayerId;
import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.domain.tournament.TournamentYearWrapper;
import de.ostfale.qk.ui.playerstats.info.favplayer.FavPlayerService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.matches.PlayerMatchStatisticsUIModel;
import de.ostfale.qk.web.internal.TournamentWebService;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.util.List;

@ApplicationScoped
public class PlayerTournamentsService {

    @Inject
    TournamentWebService tournamentWebService;

    @Inject
    TournamentModelToUIConverter tournamentModelToUIConverter;


    @Inject
    PlayerTournamentMatchesJsonHandler playerTournamentMatchesJsonHandler;

    @Inject
    FavPlayerService favPlayerService;

    public void loadAndSavePlayerTournamentsForYear(int year, PlayerInfoDTO player) {
        Log.debugf("PlayerTournamentsService :: Loading tournaments for player %s for year %d", player.toString(), year);

        var playerMasterData = player.getPlayerInfoMasterDataDTO();
        List<Tournament> tournaments = fetchTournamentsFromWebService(year, playerMasterData.getPlayerTournamentId());

        String playerName = playerMasterData.getPlayerName();
        PlayerId playerId = new PlayerId(playerMasterData.getPlayerId());
        TournamentYearWrapper tournamentYearWrapper = new TournamentYearWrapper(playerName, playerId, year, tournaments);

        favPlayerService.updateDownloadedTournaments(tournaments, playerMasterData.getPlayerId(), year);
        playerTournamentMatchesJsonHandler.saveToFile(tournamentYearWrapper);
    }

    public List<PlayerMatchStatisticsUIModel> readPlayerTournamentsForLastFourYears(PlayerInfoDTO player) {
        Log.debugf("PlayerTournamentsService ::Reading tournaments for player %s for last four years", player.toString());
        var playerMasterData = player.getPlayerInfoMasterDataDTO();
        List<TournamentYearWrapper> tournamentYearWrappers = playerTournamentMatchesJsonHandler.readFromFile(
                playerMasterData.getPlayerName(),
                playerMasterData.getPlayerId()
        );
        Log.debugf("PlayerTournamentsService ::Found %d tournaments for player %s for last four years",
                tournamentYearWrappers.size(),
                playerMasterData.getPlayerName());

        return convertTournamentsToUIModels(tournamentYearWrappers);
    }

    private List<PlayerMatchStatisticsUIModel> convertTournamentsToUIModels(List<TournamentYearWrapper> tournamentYearWrappers) {
        return tournamentYearWrappers.stream()
                .flatMap(wrapper -> wrapper.tournaments().stream())
                .map(tournamentModelToUIConverter::convertTo)
                .toList();
    }

    private List<Tournament> fetchTournamentsFromWebService(int year, String playerTournamentId) {
        return tournamentWebService.scrapeAllTournamentsForPlayerAndYear(year, playerTournamentId);
    }
}
