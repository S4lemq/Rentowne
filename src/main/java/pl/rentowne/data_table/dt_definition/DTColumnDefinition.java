package pl.rentowne.data_table.dt_definition;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import pl.rentowne.data_table.enums.DTJoinType;

import java.util.function.Consumer;

/**
 * Klasa, która określa definicję kolumn tabeli z danymi
 */
public class DTColumnDefinition {

    //nazwa kolumny
    private String name;
    //wyrażenie queryDsl do kolumny
    private Expression column;
    //join, który wymusza kolumna
    private EntityPathBase entityPathToJoin;
    //wyrażenie do joina
    private Predicate predicateToJoin;
    //typ joina
    private DTJoinType joinType;
    //rozszerzenie query dla customowych joinów
    private Consumer<JPAQuery> customExpressionForJoins;
    //czy kolumna ma mieć możliwość sortowania/filtrowania
    private boolean excludedFromSearcher;
    protected String pattern;

    public DTColumnDefinition(String name, Expression column, boolean excludedFromSearcher) {
        this.name = name;
        this.column = column;
        this.excludedFromSearcher = excludedFromSearcher;
    }

    public DTColumnDefinition(String name, Expression column) {
        this(name, column, false);
    }

    public DTColumnDefinition(String name, Expression column, String pattern) {
        this(name, column, false);
        this.pattern = pattern;
    }

    public DTColumnDefinition(String name, EntityPathBase entityPathToJoin, Expression column, Predicate predicateToJoin,
                              DTJoinType joinType, boolean excludedFromSearcher) {
        this.name = name;
        this.column = column;
        this.entityPathToJoin = entityPathToJoin;
        this.predicateToJoin = predicateToJoin;
        this.joinType = joinType;
        this.excludedFromSearcher = excludedFromSearcher;
    }

    public DTColumnDefinition(String name, EntityPathBase entityPathToJoin, Expression column, Predicate predicateToJoin, DTJoinType
            joinType) {
        this(name, entityPathToJoin, column, predicateToJoin, joinType, false);
    }

    public DTColumnDefinition(String name, Expression column, Consumer<JPAQuery> customExpressionForJoins) {
        this(name, column, customExpressionForJoins, false);
    }

    public DTColumnDefinition(String name, Expression column, Consumer<JPAQuery> customExpressionForJoins, boolean excludedFromSearcher) {
        this.name = name;
        this.column = column;
        this.customExpressionForJoins = customExpressionForJoins;
        this.excludedFromSearcher = excludedFromSearcher;
    }

    public Expression getColumn() {
        return column;
    }

    public EntityPathBase getEntityPathToJoin() {
        return entityPathToJoin;
    }

    public Predicate getPredicateToJoin() {
        return predicateToJoin;
    }

    public DTJoinType getJoinType() {
        return joinType;
    }

    public String getName() {
        return name;
    }

    public Consumer<JPAQuery> getCustomExpressionForJoins() {
        return customExpressionForJoins;
    }

    public boolean isExcludedFromSearcher() {
        return excludedFromSearcher;
    }

    public String getPattern() {
        return pattern;
    }

    public void setPattern(String pattern) {
        this.pattern = pattern;
    }
}
