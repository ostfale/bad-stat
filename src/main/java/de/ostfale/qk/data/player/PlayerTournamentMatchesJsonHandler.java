package de.ostfale.qk.data.player;

import de.ostfale.qk.data.json.JsonDBFacade;
import de.ostfale.qk.data.json.JsonFileReader;
import de.ostfale.qk.data.json.JsonFileWriter;
import de.ostfale.qk.domain.tournament.TournamentMatchesListDTO;
import io.quarkus.logging.Log;
import jakarta.inject.Inject;
import jakarta.inject.Singleton;

import java.io.File;
import java.util.List;
import java.util.Optional;

@Singleton
public class PlayerTournamentMatchesJsonHandler implements JsonDBFacade {

    @Inject
    JsonFileWriter<TournamentMatchesListDTO> jsonFileWriter;

    @Inject
    JsonFileReader<TournamentMatchesListDTO> jsonFileReader;

    public void saveToFile(TournamentMatchesListDTO listDTO) {
        var targetFilePath = getFavPlayerMatchesDir();
        var targetFileName = createFavPlayerTournamentMatchesFileName(listDTO.getPlayerName(), listDTO.getPlayerId(), listDTO.getTournamentYear());
        Log.debugf("PlayerTournamentMatchesJsonHandler :: Save player tournament matches to dir %s with file name %s", targetFilePath, targetFileName);
        jsonFileWriter.writeToFile(listDTO, targetFilePath + targetFileName);
    }

    public List<TournamentMatchesListDTO> readFromFile(String playerName, String playerId) {
        Log.debugf("PlayerTournamentMatchesJsonHandler:: Read player tournament matches from file for player %s", playerName);
        List<File> allPlayerFiles = readAllFilesForPlayerId(playerId);
        List<TournamentMatchesListDTO> dtoList = allPlayerFiles.stream()
                .map(it -> jsonFileReader.readFromFile(it.toPath(), TournamentMatchesListDTO.class))
                .filter(Optional::isPresent)
                .map(Optional::get).toList();
        Log.debugf("PlayerTournamentMatchesJsonHandler:: Found %d tournament matches for player %s", dtoList.size(), playerName);
        return dtoList;
    }

    private List<File> readAllFilesForPlayerId(String playerId) {
        var playerMatchesDir = getFavPlayerMatchesDir();
        var allFilesInDir = readAllFiles(playerMatchesDir);
        allFilesInDir.removeIf(file -> !file.getName().contains(playerId));
        Log.debugf("PlayerTournamentMatchesJsonHandler:: Found %d files for player id %s", allFilesInDir.size(), playerId);
        return allFilesInDir;
    }
}
