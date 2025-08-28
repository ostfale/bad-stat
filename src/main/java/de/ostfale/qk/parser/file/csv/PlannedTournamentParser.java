package de.ostfale.qk.parser.file.csv;

import de.ostfale.qk.app.TimeHandlerFacade;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.PlannedTournaments;
import de.ostfale.qk.domain.tourcal.TourCategory;
import de.ostfale.qk.domain.tourcal.TournamentType;
import de.ostfale.qk.parser.file.FileParser;
import de.ostfale.qk.parser.file.FileParserException;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

@ApplicationScoped
public class PlannedTournamentParser implements FileParser, TimeHandlerFacade {

    private static final int DEFAULT_TOURNAMENT_ORDER = 0;
    private static final String HEADER_START_MARKER = "Start-Datum";

    public PlannedTournaments parseTournamentCalendar(File aFile) {
        Log.debugf("PlannedTournamentParser :: Parsing file: %s", aFile.getName());
        List<PlannedTournament> tournamentsList = new ArrayList<>();

        try (Scanner scanner = new Scanner(aFile)) {
            while (scanner.hasNext()) {
                String currentLine = scanner.nextLine().trim();

                if (isHeaderOrEmptyLine(currentLine)) {
                    continue;
                }

                try {
                    tournamentsList.add(parseRow(currentLine));
                } catch (FileParserException e) {
                    Log.errorf("PlannedTournamentParser :: Failed to parse line '%s': %s", currentLine, e.getMessage());
                    // Continue processing other lines despite parsing error
                }
            }
        } catch (FileNotFoundException e) {
            Log.errorf("PlannedTournamentParser :: File not found: %s", aFile.getName());
        }
        return new PlannedTournaments(tournamentsList);
    }

    private boolean isHeaderOrEmptyLine(String line) {
        return line.startsWith(HEADER_START_MARKER) || line.isBlank();
    }

    public PlannedTournament parseRow(String row) throws FileParserException {
        String fixedRow = fixRow(row);
        String[] splitRow = fixedRow.split(CSV_SEPARATOR);
        return buildPlannedTournament(splitRow);
    }

    private PlannedTournament buildPlannedTournament(String[] splitRow) throws FileParserException {
        var startDate = readCSVValue(splitRow, START_DATE_INDEX);
        var endDate = readCSVValue(splitRow, END_DATE_INDEX);
        var tournamentName = readCSVValue(splitRow, TOURNAMENT_NAME_INDEX);
        var tournamentType = TournamentType.lookup(splitRow[TOURNAMENT_TYPE_INDEX]);
        var tournamentOrderNo = getTournamentOrderNo(splitRow);
        var countryCode = readCSVValue(splitRow, COUNTRY_INDEX);
        var location = readCSVValue(splitRow, LOCATION_INDEX);
        var postalCode = readCSVValue(splitRow, POSTAL_CODE_INDEX);
        var region = readCSVValue(splitRow, REGION_INDEX);
        var openName = readCSVValue(splitRow, OPEN_NAME_INDEX);
        var organizer = readCSVValue(splitRow, ORGANIZER_INDEX);
        var category = getCategory(splitRow);
        var closeDate = readCSVValue(splitRow, CLOSE_DATE_INDEX);
        var webLinkUrl = readCSVValue(splitRow, WEB_URL_INDEX);
        var pdfLinkUrl = readCSVValue(splitRow, PDF_URL_INDEX);
        var pdfAvailable = readCSVValue(splitRow, PDF_AVAILABLE_INDEX);
        var tourCreationDate = readCSVValue(splitRow, TOUR_CREATION_DATE_INDEX);
        var tourVisibleDate = readCSVValue(splitRow, TOUR_VISIBLE_DATE_INDEX);
        var tourInvitationCreationDate = readCSVValue(splitRow, INVITATION_CREATION_DATE_INDEX);
        var tourLinkCreationDate = readCSVValue(splitRow, TURNIER_LINK_CREATION_DATE_INDEX);
        var akU9 = readCSVValue(splitRow, AK_U9_INDEX);
        var akU11 = readCSVValue(splitRow, AK_U11_INDEX);
        var akU13 = readCSVValue(splitRow, AK_U13_INDEX);
        var akU15 = readCSVValue(splitRow, AK_U15_INDEX);
        var akU17 = readCSVValue(splitRow, AK_U17_INDEX);
        var akU19 = readCSVValue(splitRow, AK_U19_INDEX);
        var akU22 = readCSVValue(splitRow, AK_U22_INDEX);
        var akO19 = readCSVValue(splitRow, AK_O19_INDEX);
        var akO35 = readCSVValue(splitRow, AK_O35_INDEX);

        return new PlannedTournament(
                startDate, endDate, tournamentName, tournamentType, tournamentOrderNo, countryCode, location, postalCode,
                region, openName, organizer, category, closeDate, webLinkUrl, pdfLinkUrl, pdfAvailable, tourCreationDate,
                tourVisibleDate, tourInvitationCreationDate, tourLinkCreationDate,
                akU9, akU11, akU13, akU15, akU17, akU19, akU22, akO19, akO35
        );
    }

    private static String readCSVValue(String[] splitRow, int index) {
        if (index >= splitRow.length) {
            return EMPTY_STRING;
        }
        return splitRow[index].trim();
    }

    private static int getTournamentOrderNo(String[] splitRow) {
        String tournamentOrderNo = splitRow[TOURNAMENT_ORD_NO_INDEX];

        if (tournamentOrderNo.isBlank()) {
            return DEFAULT_TOURNAMENT_ORDER;
        }

        if (!isNumeric(tournamentOrderNo)) {
            return DEFAULT_TOURNAMENT_ORDER;
        }

        return Integer.parseInt(tournamentOrderNo);
    }

    private static TourCategory getCategory(String[] splitRow) throws FileParserException {
        String category = splitRow[CATEGORY_INDEX];
        return TourCategory.lookup(category);
    }

    private String fixRow(String row) {
        return row.replace("\"", "");
    }

    static boolean isNumeric(String str) {
        if (str == null || str.isEmpty()) {
            return false;
        }
        return str.trim().matches("\\d+");
    }
}
