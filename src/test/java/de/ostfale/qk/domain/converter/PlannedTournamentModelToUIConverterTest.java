package de.ostfale.qk.domain.converter;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.parser.BaseTournamentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

@DisplayName("Test planned tournament model to UI converter")
@Tag("unittest")
class PlannedTournamentModelToUIConverterTest extends BaseTournamentTest {

    private final PlannedTournamentModelToUIConverter sut = new PlannedTournamentModelToUIConverter();

    @Test
    @DisplayName("Convert tournament into UI model")
    void testPlannedTournamentModelToUIConverter() {
        // given
        var tournament = createPlannedTournament(TOURNAMENT_CATEGORY);

        // when
        var result = sut.convertTo(tournament);

        // then
        assertAll("Grouped assertions for tournament validation",
                () -> assertThat(result.startDate()).isEqualTo(TOURNAMENT_START_DATE),
                () -> assertThat(result.closedDate()).isEqualTo(TOURNAMENT_CLOSING_DATE),
                () -> assertThat(result.tournamentName()).isEqualTo(TOURNAMENT_NAME),
                () -> assertThat(result.categoryName()).isEqualTo(TOURNAMENT_CATEGORY.getDisplayName()),
                () -> assertThat(result.location()).isEqualTo(TOURNAMENT_LOCATION),
                () -> assertThat(result.organizer()).isEqualTo(TOURNAMENT_ORGANIZER),
                () -> assertThat(result.webLinkUrl()).isEqualTo(TOURNAMENT_WEBSITE),
                () -> assertThat(result.pdfLinkUrl()).isEqualTo(TOURNAMENT_PDF_URL),
                () -> assertThat(result.ageClassDisciplines().getFirst().anyDisciplineForThisAgeClass()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().getFirst().ageClass()).isEqualTo(AgeClass.U9),
                () -> assertThat(result.ageClassDisciplines().getFirst().isSingle()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().getFirst().isDouble()).isFalse(),
                () -> assertThat(result.ageClassDisciplines().getFirst().isMixed()).isFalse(),
                () -> assertThat(result.ageClassDisciplines().get(1).ageClass()).isEqualTo(AgeClass.U11),
                () -> assertThat(result.ageClassDisciplines().get(1).anyDisciplineForThisAgeClass()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(1).isSingle()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(1).isMixed()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(1).isDouble()).isFalse(),
                () -> assertThat(result.ageClassDisciplines().get(2).ageClass()).isEqualTo(AgeClass.U13),
                () -> assertThat(result.ageClassDisciplines().get(2).anyDisciplineForThisAgeClass()).isFalse(),
                () -> assertThat(result.ageClassDisciplines().get(3).ageClass()).isEqualTo(AgeClass.U15),
                () -> assertThat(result.ageClassDisciplines().get(3).anyDisciplineForThisAgeClass()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(4).ageClass()).isEqualTo(AgeClass.U17),
                () -> assertThat(result.ageClassDisciplines().get(4).anyDisciplineForThisAgeClass()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(5).ageClass()).isEqualTo(AgeClass.U19),
                () -> assertThat(result.ageClassDisciplines().get(5).anyDisciplineForThisAgeClass()).isFalse(),
                () -> assertThat(result.ageClassDisciplines().get(6).ageClass()).isEqualTo(AgeClass.U22),
                () -> assertThat(result.ageClassDisciplines().get(6).anyDisciplineForThisAgeClass()).isFalse(),
                () -> assertThat(result.ageClassDisciplines().get(7).ageClass()).isEqualTo(AgeClass.O19),
                () -> assertThat(result.ageClassDisciplines().get(7).anyDisciplineForThisAgeClass()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(7).isSingle()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(7).isDouble()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(7).isMixed()).isTrue(),
                () -> assertThat(result.ageClassDisciplines().get(8).ageClass()).isEqualTo(AgeClass.O35),
                () -> assertThat(result.ageClassDisciplines().get(8).anyDisciplineForThisAgeClass()).isFalse()
        );
    }
}
