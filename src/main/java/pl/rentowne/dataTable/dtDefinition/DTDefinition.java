package pl.rentowne.dataTable.dtDefinition;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.Expression;
import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.core.types.Predicate;
import com.querydsl.core.types.dsl.ComparableExpressionBase;
import com.querydsl.core.types.dsl.EntityPathBase;
import com.querydsl.core.types.dsl.PathBuilder;
import pl.rentowne.dataTable.filter.DTFilter;
import pl.rentowne.dataTable.filter.DTFilterColumnDefinition;
import pl.rentowne.dataTable.filter.DTFilterPredicateGenerator;

import java.util.ArrayList;
import java.util.List;

/**
 * Interfejs pozwalający tworzyć definicję tabel z danymi w systemie
 */
public interface DTDefinition {

    ConstructorExpression getSelect();

    EntityPathBase getEntity();

    default ComparableExpressionBase getId() {
        EntityPathBase entityPathBase = getEntity();
        PathBuilder pathBuilder = new PathBuilder<>(entityPathBase.getType(), entityPathBase.toString());
        ComparableExpressionBase expressionBase = pathBuilder.getNumber("id", Long.class);
        return expressionBase;
    }

    List<DTColumnDefinition> getColumnDefinitions();

    default Expression getFirstOrderingTargetForOrderBy(String columnName) {
        return null;
    }

    default List<DTFilterColumnDefinition> getUserDefinedFilterColumns() {
        return new ArrayList<>();
    }

    default Predicate getMainWherePredicate(List<DTFilter> filters) {
        return null;
    }

    default List<DTFilterColumnDefinition> getFastSearcherFilterColumns() {
        return DTFilterPredicateGenerator.generateFastSearcherColumnDefinition(getColumnDefinitions());
    }

    /**
     * Sprawdza, czy dla danego zestawu filtrów zastosować DISTINCT w zapytaniu.
     *
     * @param dtFilters - lista {@link DTFilter} reprezentujących użyte filtry
     * @return true gdy zastosowanie filtra wymaga zastosowania joina
     * powodującego wielokrotne wystąpienie pozycji w rezultatach filtrowania.
     */
    default boolean useSelectFromDistinct(List<DTFilter> dtFilters) {
        return false;
    }

    default OrderSpecifier[] addBaseOrderBy() {
        return new OrderSpecifier[0];
    }

    default Expression[] addGroupBy() {
        return new Expression[0];
    }
}
