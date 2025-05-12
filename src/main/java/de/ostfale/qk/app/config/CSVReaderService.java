package de.ostfale.qk.app.config;

import com.opencsv.CSVParser;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import jakarta.inject.Singleton;
import org.jboss.logging.Logger;

import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Singleton
public class CSVReaderService {

    private static final Logger log = Logger.getLogger(CSVReaderService.class);

    private static final Map<String, String> playerTournamentIdMap = new HashMap<>();

    public Optional<String> getPlayerTournamentId(String playerName) {
        log.debugf("Get player tournament id for player %s", playerName);

        if (playerTournamentIdMap.isEmpty()) {
            log.debug("No player tournament id map found -> will be created and returned");
            initMap();
        }

        if (playerTournamentIdMap.containsKey(playerName)) {
            log.debugf("Found player tournament id for player %s", playerName);
            return Optional.of(playerTournamentIdMap.get(playerName));
        }
        log.debugf("No player tournament id found for player %s", playerName);
        return Optional.empty();
    }


    private void initMap() {
        try {
            var inputStream = getClass().getClassLoader().getResourceAsStream("player/player_tournament_ids.csv");
            if (inputStream == null) {
                throw new FileNotFoundException("Could not find player_tournament_ids.csv in resources");
            }

            CSVParser parser = new CSVParserBuilder()
                    .withSeparator(';')
                    .build();

            CSVReaderBuilder csvReaderBuilder = new CSVReaderBuilder(new InputStreamReader(inputStream))
                    .withCSVParser(parser);

            try (var reader = csvReaderBuilder.build()) {
                String[] nextLine;
                while ((nextLine = reader.readNext()) != null) {
                    if (nextLine.length >= 3) {
                        playerTournamentIdMap.put(nextLine[1], nextLine[2]);
                    }
                }
            }
        } catch (Exception e) {
            log.errorf("Error reading CSV file: %s", e.getMessage());
        }
    }
}
