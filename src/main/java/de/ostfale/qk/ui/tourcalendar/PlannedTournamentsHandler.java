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

    @Inject
    PlannedTournamentParser parser;

    private PlannedTournaments plannedTournaments;

    public PlannedTournaments getPlannedTournaments() {
        if (plannedTournaments == null) {
            Log.debug("PlannedTournamentsHandler:: getPlannedTournaments - load data");
            plannedTournaments = loadPlannedTournaments();
        }
        return plannedTournaments;
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
            case THIS_YEAR -> {
                return allTournaments.getThisYearsTournaments();
            }
            case NEXT_YEAR -> {
                return allTournaments.getNextYearsTournaments();
            }
            default -> {
                throw new IllegalArgumentException("Unsupported view range: " + viewRange);
            }
        }
    }

    public void reload() {
        Log.debug("PlannedTournamentsHandler:: reload - load data");
        plannedTournaments = loadPlannedTournaments();
    }

    private PlannedTournaments loadPlannedTournaments() {

        String plannedTournamentsFileDir = getApplicationSubDir(DirTypes.TOURNAMENT.displayName);
        var tournamentFiles = readAllFiles(plannedTournamentsFileDir);
        if (tournamentFiles.isEmpty()) {
            return new PlannedTournaments();
        }

        var currentYearTournaments = parser.parseTournamentCalendar(tournamentFiles.getFirst());
        var nextYearTournaments = parser.parseTournamentCalendar(tournamentFiles.getLast());

        if (nextYearTournaments.getAllPlannedTournaments().isEmpty()) {
            return currentYearTournaments;
        }
        currentYearTournaments.setNextYearsTournaments(nextYearTournaments.getAllPlannedTournaments());
        return currentYearTournaments;
    }

}
