package de.ostfale.qk.app;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public interface TimeHandlerFacade {

    default int getActualCalendarWeek() {
        return LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    default int getActualCalendarYear() {
        return LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekBasedYear());
    }

    default int getCalendarYearFromLastWeek() {
        return LocalDate.now().minusWeeks(1).get(WeekFields.of(Locale.getDefault()).weekBasedYear());
    }

    default int getLastCalendarWeek() {
        return LocalDate.now().minusWeeks(1).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }
}
