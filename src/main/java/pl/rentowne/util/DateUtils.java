package pl.rentowne.util;

import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Locale;
import java.util.Map;


/**
 * Klasa pomocnicza dla metod ułatwiających obsługę dat
 */
public class DateUtils {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MMMM yyyy", new Locale("pl", "PL"));
    private static final Map<String, String> monthReplacements = Map.ofEntries(
            Map.entry("stycznia", "styczeń"),
            Map.entry("lutego", "luty"),
            Map.entry("marca", "marzec"),
            Map.entry("kwietnia", "kwiecień"),
            Map.entry("maja", "maj"),
            Map.entry("czerwca", "czerwiec"),
            Map.entry("lipca", "lipiec"),
            Map.entry("sierpnia", "sierpień"),
            Map.entry("września", "wrzesień"),
            Map.entry("października", "październik"),
            Map.entry("listopada", "listopad"),
            Map.entry("grudnia", "grudzień")
    );

    private DateUtils() {}

    public static void compareDates(LocalDateTime readingDate, LocalDateTime previousReadingDate, boolean isTenant) throws RentowneBusinessException {
        if (readingDate.isBefore(previousReadingDate) || readingDate.isEqual(previousReadingDate)) {
            if (isTenant) {
                throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_DATE_TENANT);
            } else {
                throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_DATE);
            }
        }

        long monthsBetween = ChronoUnit.MONTHS.between(
                previousReadingDate.withDayOfMonth(1),
                readingDate.withDayOfMonth(1));
        long yearsBetween = ChronoUnit.YEARS.between(
                previousReadingDate.withDayOfMonth(1),
                readingDate.withDayOfMonth(1));

        if (monthsBetween != 1 || yearsBetween > 0) {
            if (isTenant) {
                throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_DATE_TENANT);
            } else {
                throw new RentowneBusinessException(RentowneErrorCode.BAD_METER_READING_DATE);
            }
        }
    }

    public static String formatPolishDate(LocalDateTime date) {
        String formattedDate = date.format(formatter);
        for (Map.Entry<String, String> entry : monthReplacements.entrySet()) {
            if (formattedDate.contains(entry.getKey())) {
                formattedDate = formattedDate.replace(entry.getKey(), entry.getValue());
                break;
            }
        }
        return formattedDate;
    }
}
