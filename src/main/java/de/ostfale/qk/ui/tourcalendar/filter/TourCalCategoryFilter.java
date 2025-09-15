package de.ostfale.qk.ui.tourcalendar.filter;

import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.domain.tourcal.TourCategory;
import de.ostfale.qk.ui.app.events.UpdateTourCalEvent;
import io.quarkus.logging.Log;
import jakarta.enterprise.context.ApplicationScoped;
import jakarta.enterprise.event.Event;
import jakarta.inject.Inject;
import javafx.collections.ListChangeListener;
import org.controlsfx.control.CheckComboBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@ApplicationScoped
public class TourCalCategoryFilter implements TournamentFilter {

    @Inject
    Event<UpdateTourCalEvent> updateFilter;

    private Set<TourCategory> checkedAgeClasses;

    private CheckComboBox<TourCategory> categoryComboBox;

    @Override
    public List<PlannedTournament> filterTournaments(List<PlannedTournament> tournaments) {
        var currentSelection = getCheckedAgeClasses();

        if (currentSelection.isEmpty()) {
            Log.debug("TourCalCategoryFilter:: filterTournaments - checkedAgeClasses is empty -> no filter! ");
            return tournaments;
        }

        var filteredTournaments = tournaments.stream()
                .filter(tournament -> matchesAnyCheckedAgeClass(tournament, currentSelection))
                .toList();

        Log.debugf("TourCalCategoryFilter:: filterTournaments by category : found %d ", filteredTournaments.size());
        return filteredTournaments;
    }

    @Override
    public void resetFilter() {
        categoryComboBox.getCheckModel().clearChecks();
    }

    public void setCategoryComboBox(CheckComboBox<TourCategory> categoryComboBox) {
        this.categoryComboBox = categoryComboBox;
        this.categoryComboBox.getItems().addAll(TourCategory.getFilterValues());
        this.categoryComboBox.getCheckModel().getCheckedItems()
                .addListener((ListChangeListener<TourCategory>) c -> updateFilter.fire(new UpdateTourCalEvent()));
    }

    public void setCheckedAgeClasses(Set<TourCategory> checkedAgeClasses) {
        this.checkedAgeClasses = checkedAgeClasses;
    }

    private Set<TourCategory> getCheckedAgeClasses() {
        if (checkedAgeClasses == null) {
            return new HashSet<>(categoryComboBox.getCheckModel().getCheckedItems());
        }
        return checkedAgeClasses;
    }

    private boolean matchesAnyCheckedAgeClass(PlannedTournament tournament, Set<TourCategory> checkedAgeClasses) {
        return checkedAgeClasses.stream().anyMatch(tc -> tc.name().equalsIgnoreCase(tournament.tourCategory().getBaseCategory()));
    }
}
