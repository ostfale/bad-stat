package de.ostfale.qk.app;

import org.jboss.logging.Logger;

import java.time.LocalDate;
import java.time.temporal.WeekFields;
import java.util.Locale;

public interface TimeHandlerFacade {

    Logger log = Logger.getLogger(TimeHandlerFacade.class);

    default int getActualCalendarWeek() {
        return LocalDate.now().get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }

    default int getLastCalendarWeek() {
        return LocalDate.now().minusWeeks(1).get(WeekFields.of(Locale.getDefault()).weekOfWeekBasedYear());
    }
}
