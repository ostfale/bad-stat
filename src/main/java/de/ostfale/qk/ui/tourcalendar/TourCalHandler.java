package de.ostfale.qk.ui.tourcalendar;

import de.ostfale.qk.domain.converter.PlannedTournamentModelToUIConverter;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.filter.ViewRange;
import de.ostfale.qk.ui.app.BaseHandler;
import de.ostfale.qk.ui.app.events.UpdateTourCalEvent;
import io.quarkiverse.fx.RunOnFxThread;
import io.quarkiverse.fx.views.FxViewRepository;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Observes;
import jakarta.inject.Inject;
import javafx.scene.Node;

import java.util.List;

@ApplicationScoped
public class TourCalHandler implements BaseHandler {

    private static final String APP_VIEW = "tour-cal-view";

    @Inject
    FxViewRepository fxViewRepository;

    @Inject
    PlannedTournamentsHandler plannedTournamentsHandler;

    @Override
    public Node getRootNode() {
        return fxViewRepository.getViewData(APP_VIEW).getRootNode();
    }

    @RunOnFxThread
    void onUpdateTourCalEvent(@Observes UpdateTourCalEvent event) {
        Log.debug("TourCalHandler:: received UpdateTourCalEvent -> enable refresh button");
        var controller = getController();
        if (controller != null) {
            controller.enableRefreshButton();
        }
    }

    public void update() {
        var controller = getController();
        List<PlannedTournament> filteredTournaments = plannedTournamentsHandler.getTournamensList(ViewRange.REMAINING);
        Log.debugf("TourCalHandler:: update : found %d ", filteredTournaments.size());
        PlannedTournamentModelToUIConverter converter = new PlannedTournamentModelToUIConverter();
        var migratedTournaments = filteredTournaments.stream()
                .map(converter::convertTo)
                .toList();
        controller.update(migratedTournaments);
    }

    private TourCalController getController() {
        return fxViewRepository.getViewData(APP_VIEW).getController();
    }
}
