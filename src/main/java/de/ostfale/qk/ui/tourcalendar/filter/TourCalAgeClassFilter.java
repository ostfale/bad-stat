package de.ostfale.qk.ui.tourcalendar.filter;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
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
public class TourCalAgeClassFilter implements TournamentFilter {

    @Inject
    Event<UpdateTourCalEvent> updateFilter;

    private CheckComboBox<AgeClass> ageClassComboBox;

    @Override
    public List<PlannedTournament> filterTournaments(List<PlannedTournament> tournaments) {
        Set<AgeClass> checkedAgeClasses = getCheckedAgeClasses();

        if (checkedAgeClasses.isEmpty()) {
            Log.debug("TourCalAgeClassFilter:: filterTournaments - checkedAgeClasses is empty -> no filter! ");
            return tournaments;
        }

        var filteredTournaments = tournaments.stream()
                .filter(tournament -> matchesAnyCheckedAgeClass(tournament, checkedAgeClasses))
                .toList();

        Log.debugf("TourCalAgeClassFilter:: filterTournaments by age class : found %d ", filteredTournaments.size());
        return filteredTournaments;
    }

    @Override
    public void resetFilter() {
        this.ageClassComboBox.getCheckModel().clearChecks();
    }

    public void setCheckComboBox(CheckComboBox<AgeClass> checkComboBox) throws RuntimeException {
        this.ageClassComboBox = checkComboBox;
        this.ageClassComboBox.getItems().addAll(AgeClass.getFilterValues());
        this.ageClassComboBox.getCheckModel().getCheckedItems()
                .addListener((ListChangeListener<AgeClass>) c -> updateFilter.fire(new UpdateTourCalEvent()));
    }

    private Set<AgeClass> getCheckedAgeClasses() {
        return new HashSet<>(ageClassComboBox.getCheckModel().getCheckedItems());
    }

    private boolean matchesAnyCheckedAgeClass(PlannedTournament tournament, Set<AgeClass> checkedAgeClasses) {
        return checkedAgeClasses.stream()
                .anyMatch(tournament::isForAgeClass);
    }
}
