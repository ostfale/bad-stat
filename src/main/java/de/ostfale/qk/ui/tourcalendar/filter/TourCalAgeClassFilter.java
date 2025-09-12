package de.ostfale.qk.ui.tourcalendar.filter;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import org.controlsfx.control.CheckComboBox;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class TourCalAgeClassFilter {

    private final CheckComboBox<AgeClass> ageClassComboBox;

    public TourCalAgeClassFilter(CheckComboBox<AgeClass> ageClassComboBox) {
        this.ageClassComboBox = ageClassComboBox;
    }


    public List<TourCalUIModel> filter(List<TourCalUIModel> tournaments) {
        Set<AgeClass> checkedAgeClasses = getCheckedAgeClasses();

        // If no age classes are checked, return all tournaments
        if (checkedAgeClasses.isEmpty()) {
            return tournaments;
        }

        return tournaments.stream()
                .filter(tournament -> matchesAnyCheckedAgeClass(tournament, checkedAgeClasses))
                .collect(Collectors.toList());
    }


    private Set<AgeClass> getCheckedAgeClasses() {
        return new HashSet<>(ageClassComboBox.getCheckModel().getCheckedItems());
    }

    private boolean matchesAnyCheckedAgeClass(TourCalUIModel tournament, Set<AgeClass> checkedAgeClasses) {
        String categoryName = tournament.categoryName();
        if (categoryName == null || categoryName.isEmpty()) {
            return false;
        }

        // Check if the category name contains any of the checked age classes
        return checkedAgeClasses.stream()
                .anyMatch(ageClass -> categoryContainsAgeClass(categoryName, ageClass));
    }

    private boolean categoryContainsAgeClass(String categoryName, AgeClass ageClass) {
        // Convert to uppercase for case-insensitive comparison
        String upperCategoryName = categoryName.toUpperCase();
        String ageClassStr = ageClass.name();

        // Simple contains check - you might need more sophisticated matching
        return upperCategoryName.contains(ageClassStr);
    }

}
