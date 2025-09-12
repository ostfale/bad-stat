package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.domain.discipline.AgeClass;
import de.ostfale.qk.parser.BaseTournamentTest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertAll;

class PlannedTournamentTest extends BaseTournamentTest {

    @Test
    @DisplayName("Create a valid tournament record")
    void testCreatingTournamentRecord() {
        // when
        var result = new PlannedTournament(
                TOURNAMENT_START_DATE,
                TOURNAMENT_END_DATE,
                TOURNAMENT_NAME,
                TOURNAMENT_TYPE,
                TOURNAMENT_ORD_NO,
                TOURNAMENT_COUNTRY_CODE,
                TOURNAMENT_LOCATION,
                TOURNAMENT_POSTAL_CODE,
                TOURNAMENT_REGION,
                TOURNAMENT_OPEN_NAME,
                TOURNAMENT_ORGANIZER,
                TOURNAMENT_CATEGORY,
                TOURNAMENT_CLOSING_DATE,
                TOURNAMENT_WEBSITE,
                TOURNAMENT_PDF_URL,
                TOURNAMENT_PDF_AVAILABLE,
                TOURNAMENT_CREATION_DATE,
                TOURNAMENT_VISIBLE_DATE,
                TOURNAMENT_INVITATION_CREATION_DATE,
                TOURNAMENT_LINK_CREATION_DATE,
                TOURNAMENT_AK_U09,
                TOURNAMENT_AK_U11,
                TOURNAMENT_AK_U13,
                TOURNAMENT_AK_U15,
                TOURNAMENT_AK_U17,
                TOURNAMENT_AK_U19,
                TOURNAMENT_AK_U22,
                TOURNAMENT_AK_O19,
                TOURNAMENT_AK_O35
        );

        // then
        assertAll("Grouped assertions for valid record test",
                () -> assertThat(result.startDate()).isEqualTo(TOURNAMENT_START_DATE),
                () -> assertThat(result.endDate()).isEqualTo(TOURNAMENT_END_DATE),
                () -> assertThat(result.tournamentName()).isEqualTo(TOURNAMENT_NAME),
                () -> assertThat(result.tournamentType()).isEqualTo(TOURNAMENT_TYPE),
                () -> assertThat(result.tournamentOrderNo()).isEqualTo(TOURNAMENT_ORD_NO),
                () -> assertThat(result.countryCode()).isEqualTo(TOURNAMENT_COUNTRY_CODE),
                () -> assertThat(result.location()).isEqualTo(TOURNAMENT_LOCATION),
                () -> assertThat(result.postalCode()).isEqualTo(TOURNAMENT_POSTAL_CODE),
                () -> assertThat(result.region()).isEqualTo(TOURNAMENT_REGION),
                () -> assertThat(result.openName()).isEqualTo(TOURNAMENT_OPEN_NAME),
                () -> assertThat(result.organizer()).isEqualTo(TOURNAMENT_ORGANIZER),
                () -> assertThat(result.tourCategory()).isEqualTo(TOURNAMENT_CATEGORY),
                () -> assertThat(result.closingDate()).isEqualTo(TOURNAMENT_CLOSING_DATE),
                () -> assertThat(result.webLinkUrl()).isEqualTo(TOURNAMENT_WEBSITE),
                () -> assertThat(result.pdfLinkUrl()).isEqualTo(TOURNAMENT_PDF_URL),
                () -> assertThat(result.pdfAvailable()).isEqualTo(TOURNAMENT_PDF_AVAILABLE),
                () -> assertThat(result.tourCreationDate()).isEqualTo(TOURNAMENT_CREATION_DATE),
                () -> assertThat(result.tourVisibleDate()).isEqualTo(TOURNAMENT_VISIBLE_DATE),
                () -> assertThat(result.invitationCreationDate()).isEqualTo(TOURNAMENT_INVITATION_CREATION_DATE),
                () -> assertThat(result.tourLinkCreationDate()).isEqualTo(TOURNAMENT_LINK_CREATION_DATE),
                () -> assertThat(result.AK_U09()).isEqualTo(TOURNAMENT_AK_U09),
                () -> assertThat(result.AK_U11()).isEqualTo(TOURNAMENT_AK_U11),
                () -> assertThat(result.AK_U15()).isEqualTo(TOURNAMENT_AK_U15),
                () -> assertThat(result.AK_U17()).isEqualTo(TOURNAMENT_AK_U17),
                () -> assertThat(result.AK_U19()).isEqualTo(TOURNAMENT_AK_U19),
                () -> assertThat(result.AK_U22()).isEqualTo(TOURNAMENT_AK_U22),
                () -> assertThat(result.AK_O19()).isEqualTo(TOURNAMENT_AK_O19),
                () -> assertThat(result.AK_O35()).isEqualTo(TOURNAMENT_AK_O35)
        );
    }

    @Test
    @DisplayName("Test validation which age class will be played")
    void testPlayedAgeClass() {
        // given
        var plannedTournament = createPlannedTournament();

        // when
        var ageClass09 = plannedTournament.isForAgeClass(AgeClass.U9);
        var ageClass11 = plannedTournament.isForAgeClass(AgeClass.U11);
        var ageClass13 = plannedTournament.isForAgeClass(AgeClass.U13);
        var ageClass15 = plannedTournament.isForAgeClass(AgeClass.U15);
        var ageClass17 = plannedTournament.isForAgeClass(AgeClass.U17);
        var ageClass19 = plannedTournament.isForAgeClass(AgeClass.U19);
        var ageClass22 = plannedTournament.isForAgeClass(AgeClass.U22);
        var ageClass19o = plannedTournament.isForAgeClass(AgeClass.O19);
        var ageClass35o = plannedTournament.isForAgeClass(AgeClass.O35);

        // then
        assertThat(ageClass09).isTrue();
        assertThat(ageClass11).isTrue();
        assertThat(ageClass13).isFalse();
        assertThat(ageClass15).isTrue();
        assertThat(ageClass17).isTrue();
        assertThat(ageClass19).isFalse();
        assertThat(ageClass22).isFalse();
        assertThat(ageClass19o).isTrue();
        assertThat(ageClass35o).isFalse();

    }
}
