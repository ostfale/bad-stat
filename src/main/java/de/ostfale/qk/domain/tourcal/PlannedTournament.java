package de.ostfale.qk.domain.tourcal;

import de.ostfale.qk.domain.discipline.AgeClass;

public record PlannedTournament(
        String startDate,
        String endDate,
        String tournamentName,
        TournamentType tournamentType,
        int tournamentOrderNo,
        String countryCode,
        String location,
        String postalCode,
        String region,
        String openName,
        String organizer,
        TourCategory tourCategory,
        String closingDate,
        String webLinkUrl,
        String pdfLinkUrl,
        String pdfAvailable,
        String tourCreationDate,
        String tourVisibleDate,
        String invitationCreationDate,
        String tourLinkCreationDate,
        String AK_U09,
        String AK_U11,
        String AK_U13,
        String AK_U15,
        String AK_U17,
        String AK_U19,
        String AK_U22,
        String AK_O19,
        String AK_O35)
{
    public boolean isForAgeClass(AgeClass ageClass) {
        return switch (ageClass) {
            case U9 -> hasValidAgeClassData(AK_U09());
            case U11 -> hasValidAgeClassData(AK_U11());
            case U13 -> hasValidAgeClassData(AK_U13());
            case U15 -> hasValidAgeClassData(AK_U15());
            case U17 -> hasValidAgeClassData(AK_U17());
            case U19 -> hasValidAgeClassData(AK_U19());
            case U22 -> hasValidAgeClassData(AK_U22());
            case O19 -> hasValidAgeClassData(AK_O19());
            case O35 -> hasValidAgeClassData(AK_O35());
            default -> false;
        };
    }

    private boolean hasValidAgeClassData(String ageClassData) {
        return ageClassData != null && !ageClassData.isBlank();
    }
}
