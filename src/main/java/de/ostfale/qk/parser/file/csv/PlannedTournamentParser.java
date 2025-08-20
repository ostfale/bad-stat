package de.ostfale.qk.parser.file.csv;

import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.parser.file.FileParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

@ApplicationScoped
public class PlannedTournamentParser implements FileParser , TimeHandlerFacade {

    private static final String CSV_SEPARATOR = ";";

    public PlannedTournament parseRow(String row) {
        String fixedRow = fixRow(row);
        String[] splitRow = fixedRow.split(CSV_SEPARATOR);
        return new PlannedTournament(
                splitRow[START_DATE_INDEX],
                splitRow[END_DATE_INDEX],
                splitRow[TOURNAMENT_NAME_INDEX]
        );
    }

    private String fixRow(String row) {
        Log.debug("Fixing row by removing all \" ");
        return row.replace("\"", "");
    }
}
