package de.ostfale.qk.domain.tourcal;

import java.time.LocalDate;

public record TourCalendarDashboard(
        String fileName,
        LocalDate downloadDate
) {

    public TourCalendarDashboard() {
        this("", null);
    }
}
