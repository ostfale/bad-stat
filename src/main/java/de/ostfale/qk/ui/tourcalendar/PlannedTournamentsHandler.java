package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.PlannedTournaments;
import de.ostfale.qk.domain.tourcal.filter.ViewRange;
import de.ostfale.qk.parser.file.csv.PlannedTournamentParser;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.time.LocalDate;
import java.util.List;

@ApplicationScoped
public class PlannedTournamentsHandler implements FileSystemFacade {

    private static final String APP_TOURNAMENT_SUB_DIR = DirTypes.TOURNAMENT.displayName;

    @Inject
    PlannedTournamentParser parser;

    private PlannedTournaments plannedTournaments;

    private String lastDownloadDate;

    public void reload() {
        Log.debug("PlannedTournamentsHandler:: reload - load data");
        plannedTournaments = loadPlannedTournaments(APP_TOURNAMENT_SUB_DIR);
    }

    public List<PlannedTournament> getAllRemainingTournamentsList() {
        return getPlannedTournaments().getAllRemainingTournaments(LocalDate.now());
    }

    public List<PlannedTournament> getNextYearsTournamentsList() {
        return getPlannedTournaments().getNextYearsTournaments();
    }

    public List<PlannedTournament> getThisYearsTournamentsList() {
        return getPlannedTournaments().getThisYearsTournaments();
    }

    public List<PlannedTournament> getTournamensList(ViewRange viewRange) {
        var allTournaments = getPlannedTournaments();
        switch (viewRange) {
            case ALL -> {
                return allTournaments.getAllPlannedTournaments();
            }
            case REMAINING -> {
                return allTournaments.getAllRemainingTournaments(LocalDate.now());
            }
            case NEXT_YEAR -> {
                return allTournaments.getNextYearsTournaments();
            }
            default -> {
                throw new IllegalArgumentException("Unknown view range: " + viewRange);
            }
        }
    }

    public String getLastDownloadDate() {
        return lastDownloadDate;
    }

    private PlannedTournaments getPlannedTournaments() {
        if (plannedTournaments == null || plannedTournaments.getAllPlannedTournaments().isEmpty()) {
            Log.debug("PlannedTournamentsHandler:: getPlannedTournaments - load data");
            plannedTournaments = loadPlannedTournaments(APP_TOURNAMENT_SUB_DIR);
        }
        return plannedTournaments;
    }

    private PlannedTournaments loadPlannedTournaments(String subDirName) {

        String plannedTournamentsFileDir = getApplicationSubDir(subDirName);
        var tournamentFiles = readAllFiles(plannedTournamentsFileDir);
        if (tournamentFiles.isEmpty()) {
            return new PlannedTournaments();
        }

        lastDownloadDate = extractDateFromFileName(tournamentFiles.getFirst().getName());
        var currentYearTournaments = parser.parseTournamentCalendar(tournamentFiles.getFirst());
        var nextYearTournaments = parser.parseTournamentCalendar(tournamentFiles.getLast());

        if (nextYearTournaments.getAllPlannedTournaments().isEmpty()) {
            return currentYearTournaments;
        }
        currentYearTournaments.setNextYearsTournaments(nextYearTournaments.getAllPlannedTournaments());
        return currentYearTournaments;
    }
}
