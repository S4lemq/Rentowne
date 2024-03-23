package pl.rentowne.util;

/**
 * Klasa pomocnicza do obsługi string
 */
public class StringUtils {

    public static boolean isNullOrEmpty(String str) {
        return str == null || str.isEmpty();
    }

    public static boolean isNotNullOrEmpty(String str) {
        return str != null && !str.isEmpty();
    }

}
