package pl.rentowne.data_table.repository.impl;

import com.querydsl.core.BooleanBuilder;
import com.querydsl.core.types.*;
import com.querydsl.core.types.dsl.*;
import com.querydsl.jpa.JPQLQuery;
import com.querydsl.jpa.impl.JPAQuery;
import jakarta.persistence.EntityManager;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Repository;
import org.springframework.util.CollectionUtils;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.data_table.dt_definition.DTColumnDefinition;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.dt_definition.DTRow;
import pl.rentowne.data_table.enums.DTFilterType;
import pl.rentowne.data_table.enums.DTSortDirection;
import pl.rentowne.data_table.filter.DTCustomFilterQuery;
import pl.rentowne.data_table.filter.DTFilter;
import pl.rentowne.data_table.filter.DTFilterColumnDefinition;
import pl.rentowne.data_table.filter.DTFilterPredicateGenerator;
import pl.rentowne.data_table.repository.custom.DTRepositoryCustom;
import pl.rentowne.user.model.User;
import pl.rentowne.util.DTFilterJoinsUtils;

import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

@Repository
public class DTRepositoryImpl extends BaseRepositoryImpl<User, UUID> implements DTRepositoryCustom {

    public DTRepositoryImpl(EntityManager entityManager) {
        super(User.class, entityManager);
    }

    @Override
    public List<DTRow> getFiles(DTDefinition dtDefinition, long offset, long limit, DTSortDirection sortDirection,
                                String sortColumnName, List<DTFilter> dtFilters) {
        return getFiles(dtDefinition, offset, limit, sortDirection, sortColumnName, dtFilters, dtDefinition.getSelect());
    }

    @Override
    public List<DTRow> getFiles(DTDefinition dtDefinition, long offset, long limit,
                                DTSortDirection sortDirection, String sortColumnName, List<DTFilter> dtFilters,
                                ConstructorExpression constructorExpression) {
        final EntityPathBase from = dtDefinition.getEntity();
        JPAQuery query = queryFactory.from(from);

        query.select(constructorExpression);
        if (dtDefinition.useSelectFromDistinct(dtFilters)) {
            query.distinct();
        }

        addJoinToQuery(dtDefinition, dtFilters, query);

        addPredicatesToQuery(dtDefinition, dtFilters, query);

        addOrderSpecifierToQuery(dtDefinition, sortDirection, sortColumnName, query);

        addGroupByToQuery(dtDefinition, query);

        query.offset(offset);
        if (limit != 0) {
            query.limit(limit);
        }
        return query.fetch();
    }

    private void addJoinToQuery(DTDefinition dtDefinition, List<DTFilter> dtFilters, JPAQuery query) {
        final List<DTColumnDefinition> columnDefinitions = dtDefinition.getColumnDefinitions();
        DTFilterJoinsUtils.addJoins(query, columnDefinitions);

        addFilterJoinToQuery(dtFilters, query);
    }

    private void addFilterJoinToQuery(List<DTFilter> dtFilters, JPAQuery query) {
        Set<DTColumnDefinition> filterColumnDefinitions = new HashSet<>();
        for (DTFilter dtFilter : dtFilters) {
            filterColumnDefinitions.addAll(dtFilter.getFilterColumnDefinitions());
        }
        DTFilterJoinsUtils.addJoins(query, new ArrayList<>(filterColumnDefinitions));
    }

    private void addGroupByToQuery(DTDefinition dtDefinition, JPAQuery query) {
        if (ArrayUtils.isEmpty(dtDefinition.addGroupBy())) {
            return;
        }
        query.groupBy(dtDefinition.addGroupBy());
    }

    private void addOrderSpecifierToQuery(DTDefinition dtDefinition, DTSortDirection sortDirection, String sortColumnName, JPAQuery query) {
        if (noSort(sortColumnName, sortDirection)) {
            if (ArrayUtils.isNotEmpty(dtDefinition.addBaseOrderBy())) {
                query.orderBy(dtDefinition.addBaseOrderBy());
            }
            return;
        }
        final List<DTColumnDefinition> columnDefinitions = dtDefinition.getColumnDefinitions();
        final List<DTColumnDefinition> columnToSort = columnDefinitions.stream().filter(cd -> StringUtils.equals(cd.getName(), sortColumnName)).collect(Collectors.toList());
        if (CollectionUtils.isEmpty(columnToSort))
            throw new IllegalArgumentException("Can not find column into DTDefinition " + sortColumnName);
        if (columnToSort.size() > 1)
            throw new IllegalArgumentException("Name of column is not unique - check DTDefinition");

        final Expression additionalColumnExpression = dtDefinition.getFirstOrderingTargetForOrderBy(sortColumnName);
        if (additionalColumnExpression != null) {
            query.orderBy(getOrderSpecifier(additionalColumnExpression, sortDirection, sortColumnName));
        }

        final Expression columnExpression = columnToSort.get(0).getColumn();
        final OrderSpecifier orderSpecifier = getOrderSpecifier(columnExpression, sortDirection, sortColumnName);

        query.orderBy(orderSpecifier);
    }

    private OrderSpecifier getOrderSpecifier(Expression columnExpression, DTSortDirection sortDirection, String sortColumnName) {
        final OrderSpecifier orderSpecifier;
        if (columnExpression instanceof Coalesce) {
            final Coalesce coalesce = (Coalesce) columnExpression;
            orderSpecifier = sortDirection.equals(DTSortDirection.ASC) ? coalesce.asc() : coalesce.desc();
        } else if (columnExpression instanceof JPQLQuery) {
            BooleanPath path = Expressions.booleanPath(sortColumnName);
            orderSpecifier = sortDirection.equals(DTSortDirection.ASC) ? path.asc() : path.desc();
        } else {
            ComparableExpressionBase path = (ComparableExpressionBase) columnExpression;
            orderSpecifier = sortDirection.equals(DTSortDirection.ASC) ? path.asc() : path.desc();
        }
        return orderSpecifier;
    }

    private void addPredicatesToQuery(DTDefinition dtDefinition, List<DTFilter> dtFilters, JPAQuery query) {
        Predicate mainWherePredicate = dtDefinition.getMainWherePredicate(dtFilters);
        BooleanBuilder where = new BooleanBuilder();
        for (DTFilter dtFilter : dtFilters) {
            if (dtFilter.isForFastSearcher()) {
                where.and(generatePredicateFromFilter(dtFilter, true));
            } else {
                where.and(generatePredicateFromFilter(dtFilter, dtFilter.isForFastSearcher()));
            }
        }

        if ((where.getValue() instanceof BooleanBuilder && ((BooleanBuilder) where.getValue()).hasValue())
                || (where.getValue() instanceof PredicateOperation && !((PredicateOperation) where.getValue()).getArgs().isEmpty())) {
            addAdditionalOrToQuery(where, dtFilters);
        }

        if (mainWherePredicate != null) {
            query.where(mainWherePredicate);
        }

        query.where(where);
    }

    private void addAdditionalOrToQuery(BooleanBuilder where, List<DTFilter> dtFilters) {
        for (DTFilter dtFilter : dtFilters) {
            if (dtFilter.isForFastSearcher()) {
                continue;
            }
            where.or(generateOrPredicateFromFilter(dtFilter));
        }
    }

    private Predicate generateOrPredicateFromFilter(DTFilter dtFilter) {
        BooleanBuilder filterPredicate = new BooleanBuilder();
        Map<DTFilterColumnDefinition, Object> filterValues = dtFilter.getFilterValues();
        for (Map.Entry<DTFilterColumnDefinition, Object> filterValue : filterValues.entrySet()) {
            final DTFilterType filterType = filterValue.getKey().getFilterType();

            if (!(DTFilterType.IN_ADDITIONAL_OR.equals(filterType)
                    || DTFilterType.NOT_IN_ADDITIONAL_OR.equals(filterType))) {
                continue;
            }

            if (filterValue.getValue() == null) {
                continue;
            }
            if (filterValue.getValue() instanceof List && ((List) filterValue.getValue()).isEmpty()) {
                continue;
            }
            addValuesToPredicate(filterPredicate, dtFilter, filterValue, true);
        }
        return filterPredicate;
    }

    private Predicate generatePredicateFromFilter(DTFilter dtFilter, boolean isInnerFilter) {
        BooleanBuilder filterPredicate = new BooleanBuilder();
        Map<DTFilterColumnDefinition, Object> filterValues = dtFilter.getFilterValues();

        if (generatePredicatesFromPatternColumn(dtFilter, isInnerFilter, filterPredicate, filterValues)) {
            return filterPredicate; //dopasowanie do wzorca ma najwyższy priorytet
        }

        for (Map.Entry<DTFilterColumnDefinition, Object> filterValue : filterValues.entrySet()) {

            final DTFilterType filterType = filterValue.getKey().getFilterType();
            if (DTFilterType.CUSTOM_QUERY.equals(filterType)) {
                addCustomSubquery(filterPredicate, filterValue);
                continue;
            }

            //nie dodajemy dodatkowego OR do query
            if (DTFilterType.IN_ADDITIONAL_OR.equals(filterType)
                    || DTFilterType.NOT_IN_ADDITIONAL_OR.equals(filterType)) {
                continue;
            }
            if (filterValue.getValue() == null) {
                continue;
            }
            addValuesToPredicate(filterPredicate, dtFilter, filterValue, isInnerFilter);
        }
        return filterPredicate;
    }

    /**
     * Tworzy predykaty bazując na kolumnach ze zdefiniowanymi wzorcami
     */
    private boolean generatePredicatesFromPatternColumn(DTFilter dtFilter, boolean isInnerFilter, BooleanBuilder filterPredicate, Map<DTFilterColumnDefinition, Object> filterValues) {
        final Optional<Map.Entry<DTFilterColumnDefinition, Object>> dtHasPatternColumn = filterValues.entrySet().stream().filter(f -> f.getKey().getPattern() != null).findAny();
        if(dtHasPatternColumn.isPresent()) {
            final List<Map.Entry<DTFilterColumnDefinition, Object>> columnsWithPattern = filterValues.entrySet().stream().filter(f -> f.getKey().getPattern() != null).collect(Collectors.toList());
            for (Map.Entry<DTFilterColumnDefinition, Object> filterValue : columnsWithPattern) {
                final String pattern = filterValue.getKey().getPattern();
                if(pattern != null && Pattern.matches(pattern, StringUtils.trim(filterValue.getValue().toString()))) {
                    filterValue.getKey().setFilterType(DTFilterType.EQUALS);
                    addValuesToPredicate(filterPredicate, dtFilter, filterValue, isInnerFilter);
                    return true;
                }
            }
        }
        return false;
    }

    private void addCustomSubquery(BooleanBuilder filterPredicate, Map.Entry<DTFilterColumnDefinition, Object> filterValue) {
        final DTCustomFilterQuery customFilterQuery = filterValue.getKey().getCustomFilterQuery();
        filterPredicate.and(customFilterQuery.build(filterValue.getValue()));
    }

    private void addValuesToPredicate(BooleanBuilder filterPredicate,
                                      DTFilter dtFilter,
                                      Map.Entry<DTFilterColumnDefinition, Object> filterValue,
                                      boolean isInnerFilter) {
        try {
            if (filterValue.getValue() instanceof DTFilter) {
                DTFilter innerFilter = (DTFilter) filterValue.getValue();
                Predicate predicate = generatePredicateFromFilter(innerFilter, true);
                filterPredicate.and(predicate);
            } else {
                BooleanExpression expressionForSingleColumn = buildForColumn(filterValue);

                // np. przy próbie parsowania tekstu na datę
                if (expressionForSingleColumn == null)
                    return;

                if (isInnerFilter) {
                    filterPredicate.or(expressionForSingleColumn);
                } else {
                    filterPredicate.and(expressionForSingleColumn);
                }
            }
        } catch (IllegalArgumentException e) {
            //for fast (quick) search we hide parsing error
            if (!dtFilter.isForFastSearcher())
                throw e;
        }
    }

    private BooleanExpression buildForColumn(Map.Entry<DTFilterColumnDefinition, Object> filterValue) {
        BooleanExpression expressionForSingleColumn;
        if (filterValue.getValue() instanceof List) {
            expressionForSingleColumn = DTFilterPredicateGenerator
                    .getExpressionForInType(filterValue.getKey(), (List<?>) filterValue.getValue());
        } else if (filterValue.getValue() instanceof Map
                && ((Map) filterValue.getValue()).size() == 1
                && ((Map) filterValue.getValue()).containsKey("id")) {
            expressionForSingleColumn = DTFilterPredicateGenerator
                    .getExpressionForSingleColumn(filterValue.getKey(), ((Map) filterValue.getValue()).get("id").toString().trim());
        } else {
            expressionForSingleColumn = DTFilterPredicateGenerator
                    .getExpressionForSingleColumn(filterValue.getKey(), filterValue.getValue().toString().trim());
        }

        return expressionForSingleColumn;
    }

    @Override
    public long count(DTDefinition dtDefinition, List<DTFilter> dtFilters) {
        final EntityPathBase from = dtDefinition.getEntity();
        ConstructorExpression select = dtDefinition.getSelect();
        final JPAQuery query = queryFactory.from(from);
        query.select(select);
        if (dtDefinition.useSelectFromDistinct(dtFilters)) {
            query.distinct();
        }
        addJoinToQuery(dtDefinition, dtFilters, query);
        addPredicatesToQuery(dtDefinition, dtFilters, query);
        if (ArrayUtils.isEmpty(dtDefinition.addGroupBy())) {
            return query.fetchCount();
        } else {
            addGroupByToQuery(dtDefinition, query);
            return query.select(dtDefinition.addGroupBy()).fetch().size();
        }
    }

    private boolean noSort(String sortColumnName, DTSortDirection sortDirection) {
        return StringUtils.isEmpty(sortColumnName) || sortDirection.equals(DTSortDirection.NONE);
    }

}
