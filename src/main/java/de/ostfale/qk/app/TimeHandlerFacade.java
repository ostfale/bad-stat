package de.ostfale.qk.app;

import io.quarkus.logging.Log;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.temporal.WeekFields;
import java.util.Locale;

public interface TimeHandlerFacade {

    String TOURNAMENT_DATE_FILE_FORMAT = "yyyy-MM-dd";
    String TOURNAMENT_DATE_DISPLAY_FORMAT = "dd.MM.yyyy";

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

    default String formatDateToTournamentFormat(String date) {
        var result = LocalDate.parse(date, DateTimeFormatter.ofPattern(TOURNAMENT_DATE_FILE_FORMAT))
                .format(DateTimeFormatter.ofPattern(TOURNAMENT_DATE_DISPLAY_FORMAT));
        Log.debugf("TimeHandlerFacade :: Format date %s to tournament format %s", date, result);
        return result;
    }

    default LocalDate parseDateToTournamentFormat(String date) {
        var result = LocalDate.parse(date, DateTimeFormatter.ofPattern(TOURNAMENT_DATE_DISPLAY_FORMAT))
                .format(DateTimeFormatter.ofPattern(TOURNAMENT_DATE_FILE_FORMAT));
        Log.debugf("TimeHandlerFacade :: Parse date %s to tournament format %s", date, result);
        return LocalDate.parse(result, DateTimeFormatter.ofPattern(TOURNAMENT_DATE_FILE_FORMAT));
    }
}
