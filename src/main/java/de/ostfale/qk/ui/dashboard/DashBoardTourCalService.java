package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.app.PlannedTournamentsDownloader;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.PlannedTournaments;
import de.ostfale.qk.parser.file.csv.PlannedTournamentParser;
import de.ostfale.qk.ui.dashboard.model.DashBoardTourCalDTO;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class DashBoardTourCalService implements FileSystemFacade {

    private static final String TOURNAMENT_CALENDAR_FILE_DIR = DirTypes.TOURNAMENT.displayName;

    @Inject
    PlannedTournamentsDownloader plannedTournamentsDownloader;

    @Inject
    PlannedTournamentParser plannedTournamentParser;

    private PlannedTournaments plannedTournaments;

    private final List<PlannedTournament> tournaments = new ArrayList<>();

    public DashBoardTourCalDTO readData() {
        var targetDir = plannedTournamentsDownloader.prepareDownloadTargetPath(TOURNAMENT_CALENDAR_FILE_DIR);

        if (plannedTournaments == null) {
            Log.debug("DashBoardTourCalService :: no planned tournaments loaded -> load tournaments");
            plannedTournaments = loadAllTournamentsFromFile(targetDir);
            loadAndCacheTournaments(targetDir);
        }
        var foundDownloadDate = plannedTournamentsDownloader.getLastDownloadDate(targetDir);
        var nofTournamentsThisYear = plannedTournaments.getAllTournamentsForThisYear().size();
        var nofTournamentsNextYear = plannedTournaments.getAllTournamentsForNextYear().size();

        return new DashBoardTourCalDTO(foundDownloadDate, String.valueOf(nofTournamentsThisYear), String.valueOf(nofTournamentsNextYear));
    }

    private PlannedTournaments loadAllTournamentsFromFile(String targetDir) {

        var tournamentFiles = plannedTournamentsDownloader.getDownloadFiles(targetDir);
        var nextYearsTournaments = parseTournamentFile(tournamentFiles.getLast());
        plannedTournaments = parseTournamentFile(tournamentFiles.getFirst());

        if (!nextYearsTournaments.getAllPlannedTournaments().isEmpty()) {
            plannedTournaments.addAllPlannedTournaments(nextYearsTournaments.getAllPlannedTournaments());
        }
        return plannedTournaments;
    }


    private void loadAndCacheTournaments(String targetDir) {
        var tournamentFiles = plannedTournamentsDownloader.getDownloadFiles(targetDir);

        var currentYearTournaments = parseTournamentFile(tournamentFiles.getFirst());
        var nextYearTournaments = parseTournamentFile(tournamentFiles.getLast());

        tournaments.addAll(currentYearTournaments.getAllNotYetFinishedTournaments(LocalDate.now()));
        tournaments.addAll(nextYearTournaments.getAllPlannedTournaments());
    }

    private PlannedTournaments parseTournamentFile(File tournamentFile) {
        return plannedTournamentParser.parseTournamentCalendar(tournamentFile);
    }
}
