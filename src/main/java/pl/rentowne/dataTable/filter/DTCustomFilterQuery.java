package pl.rentowne.dataTable.filter;

import com.querydsl.core.types.dsl.BooleanExpression;

public interface DTCustomFilterQuery {
    BooleanExpression build(Object filterValue);
}
