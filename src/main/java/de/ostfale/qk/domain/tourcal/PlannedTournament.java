package de.ostfale.qk.domain.tourcal;

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
}
