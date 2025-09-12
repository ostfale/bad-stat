package de.ostfale.qk.ui.tourcalendar.model;

import java.util.List;

public record TourCalUIModel(
        String startDate,
        String closedDate,
        String tournamentName,
        String categoryName,
        String location,
        String organizer,
        String webLinkUrl,
        String pdfLinkUrl,
        List<TourCalAgeDiscipline>ageClassDisciplines
) {
}
