package de.ostfale.qk.ui.tourcalendar.model;

public record TourCalUIModel(
        String startDate,
        String closedDate,
        String tournamentName,
        String categoryName,
        String location,
        String organizer,
        String webLinkUrl,
        String pdfLinkUrl
) {
}
