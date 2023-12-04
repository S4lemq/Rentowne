package pl.rentowne.util;

import java.util.*;

/**
 * Klasa pomocnicza dla metod ułatwiających obsługę kolekcji
 */
public class CollectionUtils {

    private CollectionUtils() {}

    /**
     * Zwraca listę z odfiltrowanymi duplikatami
     * @param input lista wejściowa
     * @param <T> typ
     * @return lista wyjściowa bez duplikatów
     */
    public static <T> List<T> removeDuplicates(Collection<T> input) {
        Set<T> set = new TreeSet<>(input); // zachowuje kolejność
        return new ArrayList<>(set);
    }
}
