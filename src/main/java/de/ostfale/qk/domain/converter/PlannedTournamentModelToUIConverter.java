package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.domain.tourcal.PlannedTournament;
import de.ostfale.qk.ui.tourcalendar.model.TourCalAgeDiscipline;
import de.ostfale.qk.ui.tourcalendar.model.TourCalUIModel;
import io.quarkus.logging.Log;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class PlannedTournamentModelToUIConverter implements Converter<PlannedTournament, TourCalUIModel> {

    private static final String EINZEL = "Einzel";
    private static final String DOPPEL = "Doppel";
    private static final String MIXED = "Mixed";

    @Override
    public TourCalUIModel convertTo(PlannedTournament source) {
        return new TourCalUIModel(
                source.startDate(),
                source.closingDate(),
                source.tournamentName(),
                source.tourCategory().getDisplayName(),
                source.location(),
                source.organizer(),
                source.webLinkUrl(),
                source.pdfLinkUrl(),
                convertToAgeDisciplines(source)
        );
    }

    public List<TourCalAgeDiscipline> convertToAgeDisciplines(PlannedTournament source) {
        Map<AgeClass, String> ageClassMappings = createAgeClassMappings(source);

        return ageClassMappings.entrySet().stream()
                .map(entry -> convertToAgeDiscipline(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private Map<AgeClass, String> createAgeClassMappings(PlannedTournament source) {
        Map<AgeClass, String> mappings = new LinkedHashMap<>();
        mappings.put(AgeClass.U9, source.AK_U09());
        mappings.put(AgeClass.U11, source.AK_U11());
        mappings.put(AgeClass.U13, source.AK_U13());
        mappings.put(AgeClass.U15, source.AK_U15());
        mappings.put(AgeClass.U17, source.AK_U17());
        mappings.put(AgeClass.U19, source.AK_U19());
        mappings.put(AgeClass.U22, source.AK_U22());
        mappings.put(AgeClass.O19, source.AK_O19());
        mappings.put(AgeClass.O35, source.AK_O35());
        return mappings;
    }

    private TourCalAgeDiscipline convertToAgeDiscipline(AgeClass ageClass, String ageClassDisciplines) {
        if (ageClassDisciplines == null || ageClassDisciplines.isBlank()) {
            return createAgeClassWithoutDisciplines(ageClass);
        }
        boolean isSingle = isSingle(ageClassDisciplines);
        boolean isDouble = isDouble(ageClassDisciplines);
        boolean isMixed = isMixed(ageClassDisciplines);
        return new TourCalAgeDiscipline(ageClass, isSingle, isDouble, isMixed);
    }

    private TourCalAgeDiscipline createAgeClassWithoutDisciplines(AgeClass ageClass) {
        Log.debugf("PlannedTournamentModelToUIConverter :: Creating age class without disciplines for age class %s", ageClass);
        return new TourCalAgeDiscipline(ageClass, false, false, false);
    }

    private boolean isMixed(String ageClassDisciplines) {
        return ageClassDisciplines.contains(MIXED);
    }

    private boolean isDouble(String ageClassDisciplines) {
        return ageClassDisciplines.contains(DOPPEL);
    }

    private boolean isSingle(String ageClassDisciplines) {
        return ageClassDisciplines.contains(EINZEL);
    }
}
