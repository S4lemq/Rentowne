package pl.rentowne.util;

import pl.rentowne.exception.RentowneBusinessException;
import pl.rentowne.exception.RentowneErrorCode;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;


/**
 * Klasa pomocnicza dla metod ułatwiających obsługę dat
 */
public class DateUtils {

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
}
