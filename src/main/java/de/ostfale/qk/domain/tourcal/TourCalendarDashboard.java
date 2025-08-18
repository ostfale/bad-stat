package de.ostfale.qk.domain.tourcal;

public record TourCalendarDashboard(
        String downloadDate
) {

    public TourCalendarDashboard() {
        this("");
    }
}
