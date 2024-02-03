package pl.rentowne.data_table.filter;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.jpa.impl.JPAQuery;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.enums.DTFilterType;
import pl.rentowne.data_table.enums.DTJoinType;

import java.util.function.Consumer;

public class DTFilterColumnDefinition extends DTColumnDefinition {
    private DTFilterType filterType;
    private boolean valueRequired;
    private DTCustomFilterQuery customFilterQuery;

    public DTFilterColumnDefinition() {
        super(null, null);
    }

    public DTFilterColumnDefinition(String name, Expression column, DTFilterType filterType) {
        super(name, column);
        this.filterType = filterType;
    }

    public DTFilterColumnDefinition(String name, Expression column, DTFilterType filterType, String pattern) {
        super(name, column, pattern);
        this.filterType = filterType;
    }

    public DTFilterColumnDefinition(String name, Expression column, DTFilterType filterType, boolean valueRequired) {
        this(name, column, filterType);
        this.valueRequired = valueRequired;
    }

    public DTFilterColumnDefinition(String name, Expression column) {
        super(name, column);
    }

    public DTFilterColumnDefinition(String name, EntityPathBase entityPathToJoin, Expression column, Predicate predicateToJoin, DTJoinType joinType, DTFilterType filterType) {
        super(name, entityPathToJoin, column, predicateToJoin, joinType);
        this.filterType = filterType;
    }

    public DTFilterColumnDefinition(String name, EntityPathBase entityPathToJoin, Expression column, Predicate predicateToJoin,
                                    DTJoinType joinType, DTFilterType filterType, String pattern) {
        super(name, entityPathToJoin, column, predicateToJoin, joinType);
        this.filterType = filterType;
        this.pattern = pattern;
    }

    public DTFilterColumnDefinition(String name, Expression column, Consumer<JPAQuery> customExpressionForJoins, DTFilterType filterType) {
        super(name, column, customExpressionForJoins);
        this.filterType = filterType;
    }

    public DTFilterColumnDefinition(String name, Expression column, Consumer<JPAQuery> customExpressionForJoins, DTFilterType filterType,
                                    boolean valueRequired) {
        super(name, column, customExpressionForJoins);
        this.filterType = filterType;
        this.valueRequired = valueRequired;
    }

    public DTFilterType getFilterType() {
        return filterType;
    }

    public boolean isValueRequired() {
        return valueRequired;
    }

    public DTCustomFilterQuery getCustomFilterQuery() {
        return customFilterQuery;
    }

    public void setFilterType(DTFilterType filterType) {
        this.filterType = filterType;
    }

}
