package de.ostfale.qk.ui.playerstats.info.tournamentdata;

import de.ostfale.qk.domain.converter.TournamentMatchesParserModelToDTOConverter;
import de.ostfale.qk.domain.tournament.TournamentMatchesDTO;
import de.ostfale.qk.domain.tournament.TournamentMatchesListDTO;
import de.ostfale.qk.parser.tournament.model.TournamentParserModel;
import de.ostfale.qk.persistence.player.matches.PlayerTournamentMatchesJsonHandler;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoDTO;
import de.ostfale.qk.ui.playerstats.info.masterdata.PlayerInfoMasterDataDTO;
import de.ostfale.qk.web.internal.TournamentWebService;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import org.jboss.logging.Logger;

import java.util.List;

@ApplicationScoped
public class PlayerTournamentsService {

    private static final Logger log = Logger.getLogger(PlayerTournamentsService.class);

    @Inject
    TournamentWebService tournamentWebService;

    @Inject
    TournamentMatchesParserModelToDTOConverter converter;

    @Inject
    PlayerTournamentMatchesJsonHandler playerTournamentMatchesJsonHandler;

    public void loadPlayerTournamentsForYear(int year, PlayerInfoDTO player) {
        log.debugf("Loading tournaments for player %s for year %d", player.toString(), year);

        var playerMasterData = player.getPlayerInfoMasterDataDTO();
        var tournaments = fetchTournamentsFromWebService(year, playerMasterData.getPlayerTournamentId());
        var tournamentDTOs = convertToTournamentDTOs(tournaments);
        var tournamentMatchesList = createTournamentMatchesList(year, playerMasterData, tournamentDTOs);

        playerTournamentMatchesJsonHandler.saveToFile(tournamentMatchesList);
    }

    private List<TournamentParserModel> fetchTournamentsFromWebService(int year, String playerTournamentId) {
        return tournamentWebService.getTournamentsForYearAndPlayer(year, playerTournamentId);
    }

    private List<TournamentMatchesDTO> convertToTournamentDTOs(List<TournamentParserModel> tournaments) {
        return tournaments.stream()
                .map(converter::convertTo)
                .toList();
    }

    private TournamentMatchesListDTO createTournamentMatchesList(
            int year,
            PlayerInfoMasterDataDTO playerMasterData,
            List<TournamentMatchesDTO> tournamentDTOs) {

        var matchesList = new TournamentMatchesListDTO();
        matchesList.setPlayerId(playerMasterData.getPlayerId());
        matchesList.setPlayerName(playerMasterData.getPlayerName());
        matchesList.setTournamentYear(String.valueOf(year));
        matchesList.getTournamentMatchesList().addAll(tournamentDTOs);

        return matchesList;
    }
}
