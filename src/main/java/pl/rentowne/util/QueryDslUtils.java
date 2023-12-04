package pl.rentowne.util;

import com.google.common.collect.Lists;
import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.dsl.BooleanExpression;
import com.querydsl.core.types.dsl.Expressions;
import com.querydsl.core.types.dsl.NumberPath;
import com.querydsl.core.types.dsl.StringExpression;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.lang.NonNull;

import java.util.*;
import java.util.stream.Collectors;

public class QueryDslUtils {

    private static final int MAX_NUMBER_OF_EXPRESSIONS_IN_LIST = 1000;

    /**
     * Generuje bezpieczne zapytanie.
     * Lista wejściowa jest rozbijana na mniejsze spełniające ten warunek.
     *
     * @param path   ścieżka zmiennej
     * @param values wartości
     * @return wygenerowany warunek
     */
    public static BooleanExpression safeInPredicate(@NonNull NumberPath path, @NonNull Collection<Number> values) {
        List<Number> singles = new ArrayList<>();
        List<Pair<Number, Number>> ranges = new ArrayList<>();

        // wartości zmiennoprzecinkowe nie są łączone w 'between'
        if (Arrays.asList(Float.class, Double.class).contains(path.getType())) {
            singles.addAll(values);
        } else {
            separateValues(values, singles, ranges);
        }

        BooleanBuilder result = new BooleanBuilder();

        // dodawanie przedziałów
        ranges.forEach(range -> result.or(path.between(range.getKey(), range.getValue())));
        // dodawanie pojedynczych
        List<List<Number>> partitions = Lists.partition(singles, MAX_NUMBER_OF_EXPRESSIONS_IN_LIST);
        partitions.forEach(partition ->
                result.or(path.in(partition)));

        return result.getValue() == null ? path.in(new ArrayList()) : Expressions.asBoolean(result.getValue());
    }

    /**
     * Generuje bezpieczne zapytanie.
     * Kolekcja wejściowa jest rozbijana na mniejsze listy spełniające ten warunek.
     *
     * @param path   ścieżka zmiennej
     * @param values wartości
     * @return wygenerowany warunek
     */
    public static BooleanExpression safeInPredicate(@NonNull StringExpression path, @NonNull Collection<String> values) {
        if (values.isEmpty()) {
            return Expressions.asBoolean(path.in(Collections.emptyList()));
        }
        List<String> valuesAsList = new ArrayList<>(values);
        final BooleanBuilder result = new BooleanBuilder();
        for (List<String> partition : Lists.partition(valuesAsList, MAX_NUMBER_OF_EXPRESSIONS_IN_LIST)) {
            result.or(path.in(new ArrayList<>(partition)));
        }
        return result.getValue() == null ? path.in(new ArrayList()) : Expressions.asBoolean(result.getValue());
    }

    /**
     * Tylko wartości całkowite mogą być używane
     *
     * @param in      wartości wejściowe
     * @param singles pojedyncze wartości
     * @param ranges  zakresy
     */
    private static void separateValues(Collection<Number> in, List<Number> singles, List<Pair<Number, Number>> ranges) {
        List<Number> inWithoutDuplicates = CollectionUtils.removeDuplicates(in);
        List<Number> inSorted = inWithoutDuplicates.stream()
                .filter(Objects::nonNull)
                .sorted(QueryDslUtils::compareNumber)
                .collect(Collectors.toList());

        int listLength = inSorted.size();
        int indexStart = 0;
        int indexEnd = 0;
        while (indexStart < listLength) {
            indexEnd++;
            while (indexEnd < listLength &&
                    inSorted.get(indexEnd).longValue() - inSorted.get(indexEnd - 1).longValue() == 1L) {
                indexEnd++;
            }
            if (indexEnd - indexStart > 2) {
                ranges.add(Pair.of(inSorted.get(indexStart), inSorted.get(indexEnd - 1)));
                indexStart = indexEnd;
            } else {
                for (; indexStart < indexEnd; indexStart++) {
                    singles.add(inSorted.get(indexStart));
                }
            }
        }
    }

    private static int compareNumber(Number x, Number y) {
        double xx = x.doubleValue();
        double yy = y.doubleValue();
        if (xx < yy) {
            return -1;
        } else if (xx == yy) {
            return 0;
        } else {
            return 1;
        }
    }
}
