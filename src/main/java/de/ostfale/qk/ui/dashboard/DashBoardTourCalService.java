package de.ostfale.qk.ui.dashboard;

import de.ostfale.qk.app.FileSystemFacade;
import de.ostfale.qk.ui.dashboard.model.DashBoardTourCalDTO;
import de.ostfale.qk.ui.tourcalendar.PlannedTournamentsHandler;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;

@ApplicationScoped
public class DashBoardTourCalService implements FileSystemFacade {

    @Inject
    PlannedTournamentsHandler plannedTournamentsHandler;

    public void reloadDataFromFileSystem() {
        Log.debug("DashBoardTourCalService :: reload data from file system");
        plannedTournamentsHandler.reload();
    }

    public DashBoardTourCalDTO loadData() {
        Log.debug("DashBoardTourCalService :: load data");
        var remainingTournaments = plannedTournamentsHandler.getAllRemainingTournamentsList().size();
        var thisYearTournaments = plannedTournamentsHandler.getThisYearsTournamentsList().size();
        var nextYearTournaments = plannedTournamentsHandler.getNextYearsTournamentsList().size();
        var lastDownloadDate = plannedTournamentsHandler.getLastDownloadDate();

        return new DashBoardTourCalDTO(
                lastDownloadDate,
                String.valueOf(thisYearTournaments),
                String.valueOf(remainingTournaments),
                String.valueOf(nextYearTournaments));
    }
}
