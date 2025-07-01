package de.ostfale.qk.ui.playerstats.info.tournamentdata;

import de.ostfale.qk.data.player.PlayerTournamentMatchesJsonHandler;
import de.ostfale.qk.domain.converter.TournamentMatchesDTOToUIConverter;
import de.ostfale.qk.domain.converter.TournamentMatchesParserModelToDTOConverter;
import de.ostfale.qk.domain.converter.TournamentModelToUIConverter;
import de.ostfale.qk.domain.tournament.Tournament;
import de.ostfale.qk.domain.tournament.TournamentMatchesDTO;
import de.ostfale.qk.domain.tournament.TournamentMatchesListDTO;
import de.ostfale.qk.parser.tournament.model.TournamentParserModel;
import de.ostfale.qk.ui.playerstats.info.favplayer.FavPlayerService;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoMasterDTO;
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
    TournamentMatchesParserModelToDTOConverter converter;

    @Inject
    TournamentMatchesDTOToUIConverter uiConverter;

    @Inject
    PlayerTournamentMatchesJsonHandler playerTournamentMatchesJsonHandler;

    @Inject
    FavPlayerService favPlayerService;

    public void loadAndSavePlayerTournamentsForYear(int year, PlayerInfoDTO player) {
        Log.debugf("PlayerTournamentsService :: Loading tournaments for player %s for year %d", player.toString(), year);

        var playerMasterData = player.getPlayerInfoMasterDataDTO();
        List<Tournament> tournaments = fetchTournamentsFromWebService(year, playerMasterData.getPlayerTournamentId());
       // var allTournamentInUiFormat = tournaments.stream().map(tournamentModelToUIConverter::convertTo).toList();
        favPlayerService.updateDownloadedTournaments(tournaments, playerMasterData.getPlayerId(), year);

        System.out.println("dddddddd");

        //  var tournamentDTOs = convertToTournamentDTOs(tournaments);
        // var tournamentMatchesList = createTournamentMatchesList(year, playerMasterData, tournamentDTOs);
        //   playerTournamentMatchesJsonHandler.saveToFile(tournamentMatchesList);
    }


    public List<PlayerMatchStatisticsUIModel> readPlayerTournamentsForLastFourYears(PlayerInfoDTO player) {
        Log.debugf("PlayerTournamentsService ::Reading tournaments for player %s for last four years", player.toString());
        var playerMasterData = player.getPlayerInfoMasterDataDTO();
        List<TournamentMatchesListDTO> allPlayerFiles = playerTournamentMatchesJsonHandler.readFromFile(playerMasterData.getPlayerName(), playerMasterData.getPlayerId());
        Log.debugf("PlayerTournamentsService ::Found %d tournaments for player %s for last four years", allPlayerFiles.size(), playerMasterData.getPlayerName());
        return allPlayerFiles.stream()
                .flatMap(tournamentList -> tournamentList.getTournamentMatchesList().stream())
                .map(uiConverter::convertTo)
                .toList();
    }

    private List<Tournament> fetchTournamentsFromWebService(int year, String playerTournamentId) {
        // return tournamentWebService.getTournamentsForYearAndPlayer(year, playerTournamentId);
        return tournamentWebService.scrapeAllTournamentsForPlayerAndYear(year, playerTournamentId);
    }

    private List<TournamentMatchesDTO> convertToTournamentDTOs(List<TournamentParserModel> tournaments) {
        return tournaments.stream()
                .map(converter::convertTo)
                .toList();
    }

    private TournamentMatchesListDTO createTournamentMatchesList(
            int year,
            PlayerInfoMasterDTO playerMasterData,
            List<TournamentMatchesDTO> tournamentDTOs) {

        var matchesList = new TournamentMatchesListDTO();
        matchesList.setPlayerId(playerMasterData.getPlayerId());
        matchesList.setPlayerName(playerMasterData.getPlayerName());
        matchesList.setTournamentYear(String.valueOf(year));
        matchesList.getTournamentMatchesList().addAll(tournamentDTOs);

        return matchesList;
    }
}
