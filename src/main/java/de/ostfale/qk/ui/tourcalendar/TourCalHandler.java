package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.app.DirTypes;
import de.ostfale.qk.app.PlannedTournamentsDownloader;
import de.ostfale.qk.domain.converter.PlannedTournamentModelToUIConverter;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.PlannedTournaments;
import de.ostfale.qk.parser.file.csv.PlannedTournamentParser;
import de.ostfale.qk.ui.app.BaseHandler;
import io.quarkiverse.fx.views.FxViewRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.inject.Inject;
import javafx.scene.Node;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@ApplicationScoped
public class TourCalHandler implements BaseHandler {

    private static final String APP_VIEW = "tour-cal-view";

    @Inject
    FxViewRepository fxViewRepository;

    @Inject
    PlannedTournamentsDownloader plannedTournamentsDownloader;

    @Inject
    PlannedTournamentParser plannedTournamentParser;

    private final List<PlannedTournament> tournaments = new ArrayList<>();

    @Override
    public Node getRootNode() {
        return fxViewRepository.getViewData(APP_VIEW).getRootNode();
    }

    public List<PlannedTournament> loadFuturePlannedTournaments() {
        Log.debug("TourCalHandler:: loadFuturePlannedTournaments");

        if (tournaments.isEmpty()) {
            loadAndCacheTournaments();
        }
        return tournaments;
    }

    private void loadAndCacheTournaments() {
        var targetPath = plannedTournamentsDownloader.prepareDownloadTargetPath(DirTypes.TOURNAMENT.displayName);
        var tournamentFiles = plannedTournamentsDownloader.getDownloadFiles(targetPath);

        var currentYearTournaments = parseTournamentFile(tournamentFiles.getFirst());
        var nextYearTournaments = parseTournamentFile(tournamentFiles.getLast());

        tournaments.addAll(currentYearTournaments.getAllNotYetFinishedTournaments(LocalDate.now()));
        tournaments.addAll(nextYearTournaments.getAllPlannedTournaments());
    }

    private PlannedTournaments parseTournamentFile(File tournamentFile) {
        return plannedTournamentParser.parseTournamentCalendar(tournamentFile);
    }


    public void update(List<PlannedTournament> tournaments) {
        Log.debugf("TourCalHandler:: update : found %d ", tournaments.size());
        PlannedTournamentModelToUIConverter converter = new PlannedTournamentModelToUIConverter();
        var controller = getController();
        var migratedTournaments = tournaments.stream()
                .map(converter::convertTo)
                .toList();
        controller.update(migratedTournaments);
    }

    private TourCalController getController() {
        return fxViewRepository.getViewData(APP_VIEW).getController();
    }
}
