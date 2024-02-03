package pl.rentowne.data_table.filter;

import com.querydsl.core.types.Expression;
import com.querydsl.core.types.dsl.*;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.BooleanUtils;
import org.apache.commons.lang3.math.NumberUtils;
import org.apache.commons.lang3.time.DateUtils;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.enums.DTFilterType;
import pl.rentowne.util.QueryDslUtils;

import java.text.ParseException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * Generator tworzący predykaty na podstawie definicji i wartości filtra
 */
public class DTFilterPredicateGenerator {

    private static final String[] DATE_PATTERNS = {"yyyy-MM-dd", "dd.MM.yyyy", "dd-MM-yyyy", "yyyy.MM.dd"};

    private DTFilterPredicateGenerator() {
    }

    public static BooleanExpression getExpressionForInType(DTFilterColumnDefinition columnDefinition,
                                                           List<?> inValues) {
        Expression filterColumnExpression = columnDefinition.getColumn();
        DTFilterType filterType = columnDefinition.getFilterType();
        if (filterColumnExpression instanceof EnumExpression) {
            return genExprForEnumPath((EnumExpression) filterColumnExpression, (List<String>) inValues, filterType);
        } else if (filterColumnExpression instanceof NumberExpression) {
            return buildNumericInExpr(filterColumnExpression, inValues, filterType);
        } else if (filterColumnExpression instanceof StringExpression) {
            return buildStringExpr(filterColumnExpression, inValues, filterType);
        } else if (filterColumnExpression instanceof DateExpression || filterColumnExpression instanceof DateTimeExpression) {
            return buildDateInExpr(filterColumnExpression, inValues, filterType);
        } else if (filterColumnExpression instanceof Coalesce) {
            return buildStringExpr(((Coalesce) filterColumnExpression).asString(), inValues, filterType);
        }

        BooleanExpression expression = ((SimpleExpression) filterColumnExpression).in(inValues);
        return checkNotInExpression(expression, filterType);
    }

    private static BooleanExpression buildStringExpr(Expression filterColumnExpression, List<?> inValues, DTFilterType filterType) {
        final StringExpression columnExpression = (StringExpression) filterColumnExpression;
        BooleanExpression booleanExpression = null;
        List<String> tokens = (List<String>) inValues;
        for (String token : tokens) {
            if (booleanExpression == null) {
                booleanExpression = columnExpression.containsIgnoreCase(token);
            } else {
                booleanExpression = booleanExpression.or(columnExpression.containsIgnoreCase(token));
            }
        }
        return checkNotInExpression(booleanExpression, filterType);
    }

    private static BooleanExpression buildNumericInExpr(Expression filterColumnExpression, List<?> inValues, DTFilterType filterType) {
        if (inValues == null) return null;
        // jeśli lista in pusta i typ filtru IN to wynik zapytania musi być pusty np. where id in () -> []
        // jeśli lista in pusta i typ filtru NOT IN to w wyniku zapytania nie bierz tego warunku pod uwagę (dodawaj prawdę)
        if (inValues.isEmpty()) {
            return (filterType.equals(DTFilterType.IN) || filterType.equals(DTFilterType.IN_ADDITIONAL_OR)) ?
                    Expressions.asBoolean(true).isFalse() : Expressions.asBoolean(true).isTrue();
        }

        List<Number> inNumeric = inValues.stream()
                .map(String::valueOf)
                .map(DTFilterPredicateGenerator::convertToNumber)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        if(CollectionUtils.isEmpty(inNumeric)) return null;

        // przy mapowaniu zapytań musi być zachowany typ
        if ((filterColumnExpression).getType().equals(Long.class)) {
            inNumeric = inNumeric.stream()
                    .map(Number::longValue)
                    .collect(Collectors.toList());
        }

        // rozbicie listy parametrów na grupy po 1000
        BooleanExpression result = QueryDslUtils.safeInPredicate((NumberPath)filterColumnExpression, inNumeric);
        return checkNotInExpression(result, filterType);
    }

    private static BooleanExpression buildDateInExpr(Expression filterColumnExpression, List<?> inValues, DTFilterType filterType) {
        List<Date> dates = new ArrayList<>();
        for (Object inValue : inValues) {
            Date parsed = convertToDate((String) inValue);
            if (parsed != null)
                dates.add(parsed);
        }
        if (CollectionUtils.isNotEmpty(dates)) {
            BooleanExpression expression = ((SimpleExpression) filterColumnExpression).in(dates);
            return checkNotInExpression(expression, filterType);
        } else {
            return null;
        }
    }

    public static BooleanExpression getExpressionForSingleColumn(DTFilterColumnDefinition columnDefinition,
                                                                 String textToSearch) {
        Expression filterColumnExpression = columnDefinition.getColumn();
        DTFilterType filterType = columnDefinition.getFilterType();
        BooleanExpression expression = null;
        if (filterColumnExpression instanceof StringExpression) {
            expression = genExprForStringPath((StringExpression) filterColumnExpression, filterType, textToSearch);
        } else if (filterColumnExpression instanceof NumberExpression) {
            expression = genExprForNumberPath((NumberExpression) filterColumnExpression, filterType, textToSearch);
        } else if (filterColumnExpression instanceof EnumExpression) {
            expression = genExprForEnumPath((EnumExpression) filterColumnExpression, textToSearch, filterType);
        } else if (filterColumnExpression instanceof DateTimeExpression) {
            expression = genExprForDatetimePath((DateTimeExpression) filterColumnExpression, filterType, textToSearch);
        } else if (filterColumnExpression instanceof BooleanExpression) {
            expression = genExprForBooleanPath((BooleanExpression) filterColumnExpression, filterType, textToSearch);
        } else if (filterColumnExpression instanceof Coalesce) {
            expression = genExprForStringPath(((Coalesce) filterColumnExpression).asString(), filterType, textToSearch);
        }

        return expression;
    }

    private static BooleanExpression genExprForNumberPath(NumberExpression filterColumnExpression, DTFilterType filterType,
                                                          String textToSearch) {
        Number number = convertToNumber(textToSearch);
        if (number != null) {
            BooleanExpression booleanExpression;
            if (filterType == DTFilterType.GOE)
                booleanExpression = filterColumnExpression.goe(number);
            else if (filterType == DTFilterType.LOE)
                booleanExpression = filterColumnExpression.loe(number);
            else if (filterType == DTFilterType.NOT_EQUAL)
                booleanExpression = filterColumnExpression.ne(number);
            else if (filterType == DTFilterType.START_WITH)
                booleanExpression = filterColumnExpression.like(textToSearch+"%");
            else
                booleanExpression = filterColumnExpression.eq(number);

            return checkNotInExpression(booleanExpression, filterType);
        }
        return Expressions.ZERO.eq(Expressions.ONE);
    }

    private static Number convertToNumber(String textToSearch) {
        String replace = textToSearch.replace(",", ".");
        if (NumberUtils.isCreatable(replace)) {
            return NumberUtils.createNumber(replace);
        }

        return null;
    }

    private static Date convertToDate(String txt) {
        try {
            return DateUtils.parseDate(txt, DATE_PATTERNS);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    }

    private static BooleanExpression genExprForStringPath(StringExpression filterColumnExpression, DTFilterType filterType, String textToSearch) {
        textToSearch = textToSearch.trim();
        BooleanExpression booleanExpression;
        switch (filterType) {
            case EQUALS:
                booleanExpression = filterColumnExpression.equalsIgnoreCase(textToSearch);
                break;
            case START_WITH:
                booleanExpression = filterColumnExpression.startsWithIgnoreCase(textToSearch);
                break;
            case CONTAINS:
                booleanExpression = filterColumnExpression.containsIgnoreCase(textToSearch);
                break;
            default:
                booleanExpression = filterColumnExpression.containsIgnoreCase(textToSearch);
        }

        return checkNotInExpression(booleanExpression, filterType);
    }

    private static BooleanExpression genExprForEnumPath(EnumExpression filterColumnExpression, List<String> valuesToSearch, DTFilterType filterType) {
        BooleanExpression expression;
        Object[] enums = filterColumnExpression.getType().getEnumConstants();
        Map<String, Enum> enumsMap = new HashMap<>(enums.length);
        for (Object anEnum : enums) {
            Enum e = (Enum) anEnum;
            enumsMap.put(e.name(), e);
        }
        List<Enum> acceptedEnums = getAcceptedEnums(enumsMap, valuesToSearch);
        expression = filterColumnExpression.in(acceptedEnums);
        return checkNotInExpression(expression, filterType);
    }

    private static BooleanExpression genExprForEnumPath(EnumExpression filterColumnExpression, String textToSearch, DTFilterType filterType) {
        return genExprForEnumPath(filterColumnExpression, Collections.singletonList(textToSearch), filterType);
    }

    private static List<Enum> getAcceptedEnums(Map<String, Enum> enums, List<String> valuesToSearch) {
        List<Enum> result = new ArrayList<>(enums.size());
        for (Iterator<String> i = enums.keySet().iterator(); i.hasNext(); ) {
            String enumDisplayRepresentation = i.next();
            String upEnumDisplayRepresentation = enumDisplayRepresentation.toUpperCase();
            Enum e = enums.get(enumDisplayRepresentation);
            for (String value : valuesToSearch) {
                if (upEnumDisplayRepresentation.equals(value)) {
                    result.add(e);
                }
            }
        }
        return result;
    }

    private static BooleanExpression genExprForDatetimePath(DateTimeExpression searchColumnPath, DTFilterType filterType, String textToSearch) {
        BooleanExpression booleanExpression;
        final Date date;
        try {
            date = DateUtils.parseDate(textToSearch, DATE_PATTERNS);
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
        switch (filterType) {
            case DATE_TO:
                booleanExpression = searchColumnPath.loe(setEndOfDayTime(date)).or(searchColumnPath.isNull());
                break;
            case DATE_TO_NOT_NULL:
                booleanExpression = searchColumnPath.loe(setEndOfDayTime(date));
                break;
            case EQUALS:
                booleanExpression = searchColumnPath.between(DateUtils.truncate(date, Calendar.DATE), setEndOfDayTime(date));
                break;
            case DATE_FROM_NOT_NULL:
                booleanExpression = searchColumnPath.goe(DateUtils.truncate(date, Calendar.DATE));
                break;
            case DATE_FROM:
            default:
                booleanExpression = searchColumnPath.goe(DateUtils.truncate(date, Calendar.DATE)).or(searchColumnPath.isNull());
                break;
        }
        return checkNotInExpression(booleanExpression, filterType);
    }

    private static Date setEndOfDayTime(Date date) {
        if (date == null)
            return null;
        Calendar cal = Calendar.getInstance();
        cal.setTime(date);
        cal.set(Calendar.HOUR_OF_DAY, 23);
        cal.set(Calendar.MINUTE, 59);
        cal.set(Calendar.SECOND, 59);
        cal.set(Calendar.MILLISECOND, 999);
        return cal.getTime();
    }

    private static BooleanExpression genExprForBooleanPath(BooleanExpression filterColumnExpression, DTFilterType filterType, String textToSearch) {
        if (filterType == DTFilterType.INDEPENDENT) {
            return filterColumnExpression;
        }

        Boolean parsed = BooleanUtils.toBooleanObject(textToSearch);
        if (parsed == null) {
            return null;
        }
        if (BooleanUtils.isTrue(parsed)) {
            return filterColumnExpression.isTrue();
        }
        if (BooleanUtils.isFalse(parsed)) {
            return filterColumnExpression.isFalse();
        }
        return null;
    }

    /**
     * Metoda generująca listę {@link DTFilterColumnDefinition}
     * na podstawie {@link DTColumnDefinition}.
     * Wykorzystywana jest do szybkiej wyszukiwarki, której założenia co do wyszukiwanych pól są następujące:
     * pole tekstowe filtrowane przez "zaczyna się od" (LIKE ‘[text]%)
     * pole liczbowe filtrowane przez dokładne wyszukiwanie
     * pole typu data filtrowane przez "zaczyna się od"
     *
     * @param columnDefinitions lista definicji kolumn
     * @return lista definicji kolumn filtrujących do szybkiej wyszukiwarki
     */
    public static List<DTFilterColumnDefinition> generateFastSearcherColumnDefinition(List<DTColumnDefinition> columnDefinitions) {
        List<DTFilterColumnDefinition> filterColumnDefinitions = new ArrayList<>();
        for (DTColumnDefinition columnDefinition : columnDefinitions) {
            if (columnDefinition.isExcludedFromSearcher()) {
                continue;
            }
            DTFilterType filterType = getTypeBasedOnExpression(columnDefinition.getColumn());
            if (filterType != null) {
                filterColumnDefinitions.add(new DTFilterColumnDefinition(columnDefinition.getName(),
                        columnDefinition.getEntityPathToJoin(),
                        columnDefinition.getColumn(),
                        columnDefinition.getPredicateToJoin(),
                        columnDefinition.getJoinType(),
                        filterType,
                        columnDefinition.getPattern()
                ));
            }
        }
        return filterColumnDefinitions;
    }

    private static DTFilterType getTypeBasedOnExpression(Expression expression) {
        if (expression instanceof StringExpression) {
            return DTFilterType.CONTAINS;
        } else if (expression instanceof NumberExpression) {
            return DTFilterType.EQUALS;
        } else if (expression instanceof EnumExpression) {
            return DTFilterType.START_WITH;
        } else if (expression instanceof DateTimeExpression) {
            return DTFilterType.EQUALS;
        } else if (expression instanceof Coalesce) {
            return DTFilterType.CONTAINS;
        }
        return null;
    }

    private static BooleanExpression checkNotInExpression(BooleanExpression expression, DTFilterType filterType) {
        if (expression == null || filterType == null) {
            return expression;
        }
        if (filterType == DTFilterType.NOT_IN || filterType == DTFilterType.NOT_IN_ADDITIONAL_OR) {
            expression = expression.not();
        }
        return expression;
    }
}
