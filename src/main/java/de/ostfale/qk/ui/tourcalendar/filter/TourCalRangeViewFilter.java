package de.ostfale.qk.ui.tourcalendar.filter;

import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.filter.ViewRange;
import de.ostfale.qk.ui.app.events.UpdateTourCalEvent;
import de.ostfale.qk.ui.tourcalendar.PlannedTournamentsHandler;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import javafx.scene.control.ComboBox;

import java.util.List;

@ApplicationScoped
public class TourCalRangeViewFilter implements TournamentFilter {

    @Inject
    Event<UpdateTourCalEvent> updateFilter;

    @Inject
    PlannedTournamentsHandler plannedTournamentsHandler;

    private ComboBox<ViewRange> viewRangeComboBox;

    @Override
    public List<PlannedTournament> filterTournaments(List<PlannedTournament> tournaments) {
        var selectedRange = getSelectedRange();
        switch (selectedRange) {
            case ALL -> {
                return plannedTournamentsHandler.getAllPlannedTournamentsList();
            }
            case REMAINING -> {
                return plannedTournamentsHandler.getAllRemainingTournamentsList();
            }
            case NEXT_YEAR -> {
                return plannedTournamentsHandler.getNextYearsTournamentsList();
            }
        }
        return List.of();
    }

    @Override
    public void resetFilter() {
        viewRangeComboBox.getSelectionModel().select(ViewRange.REMAINING);
    }

    public void setViewRangeComboBox(ComboBox<ViewRange> viewRangeComboBox) {
        this.viewRangeComboBox = viewRangeComboBox;
        this.viewRangeComboBox.getItems().addAll(ViewRange.values());
        this.viewRangeComboBox.getSelectionModel().select(ViewRange.REMAINING);
        this.viewRangeComboBox.valueProperty().addListener((_, _, _) -> updateFilter.fire(new UpdateTourCalEvent()));
    }

    private ViewRange getSelectedRange() {
        var selectedRange = viewRangeComboBox.getSelectionModel().getSelectedItem();
        Log.debugf("Selected range is %s", selectedRange);
        return selectedRange;
    }
}
