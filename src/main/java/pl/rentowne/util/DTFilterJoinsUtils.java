package pl.rentowne.util;

import com.querydsl.jpa.impl.JPAQuery;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.enums.DTJoinType;

import java.util.List;

/**
 * Klasa pomocnica dla joinowania w zapytaniach, które wykorzystuje tabela z danymi (DT)
 */
public class DTFilterJoinsUtils {

    private DTFilterJoinsUtils() { throw new AssertionError("Invalid constructor call"); }

    /**
     * Dodaje join (zgodnie z definicjami DTColumnDefinition) do zapytania (query)
     * @param query Zapytanie, do którego zostaną dodane joiny
     * @param columns - lista kolumn, które mogą wymagać joinowania do zapytania
     * @return Zmodyfikowane zapytanie
     */
    public static JPAQuery addJoins(JPAQuery query, List<DTColumnDefinition> columns){
        if(columns != null  && !columns.isEmpty()) {
            columns.forEach(join -> {
                if(join.getEntityPathToJoin() != null) {
                    if (join.getJoinType() == null || join.getJoinType().equals(DTJoinType.LEFT_JOIN))
                        addLeftJoin(query, join);
                    else if (join.getJoinType().equals(DTJoinType.INNER_JOIN))
                        addInnerJoin(query, join);
                    else if (join.getJoinType().equals(DTJoinType.RIGHT_JOIN))
                        addRightJoin(query, join);
                } else if (join.getCustomExpressionForJoins() != null) {
                    join.getCustomExpressionForJoins().accept(query);
                }
            });
        }
        return query;
    }

    private static void addInnerJoin(JPAQuery query, DTColumnDefinition join){
        query.innerJoin(join.getEntityPathToJoin()).on(join.getPredicateToJoin());
    }

    private static void addLeftJoin(JPAQuery query, DTColumnDefinition join){
        query.leftJoin(join.getEntityPathToJoin()).on(join.getPredicateToJoin());
    }

    private static void addRightJoin(JPAQuery query, DTColumnDefinition join){
        query.rightJoin(join.getEntityPathToJoin()).on(join.getPredicateToJoin());
    }
}
