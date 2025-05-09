package de.ostfale.qk.persistence.player.matches;

import de.ostfale.qk.domain.tournament.TournamentMatchesListDTO;
import de.ostfale.qk.persistence.json.JsonDBFacade;
import de.ostfale.qk.persistence.json.JsonFileReader;
import de.ostfale.qk.persistence.json.JsonFileWriter;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

@Singleton
public class PlayerTournamentMatchesJsonHandler implements JsonDBFacade {

    private static final Logger log = Logger.getLogger(PlayerTournamentMatchesJsonHandler.class);

    @Inject
    JsonFileWriter<TournamentMatchesListDTO> jsonFileWriter;

    @Inject
    JsonFileReader<TournamentMatchesListDTO> jsonFileReader;

    public void saveToFile(TournamentMatchesListDTO listDTO) {
        var targetFilePath = getFavPlayerMatchesDir();
        var targetFileName = createFavPlayerTournamentMatchesFileName(listDTO.getPlayerName(), listDTO.getPlayerId(), listDTO.getTournamentYear());
        log.debugf("Save player tournament matches to dir %s with file name %s", targetFilePath,targetFileName);
        jsonFileWriter.writeToFile(listDTO, targetFilePath + targetFileName);
    }
}
