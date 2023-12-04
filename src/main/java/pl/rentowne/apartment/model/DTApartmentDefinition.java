package pl.rentowne.apartment.model;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import pl.rentowne.dataTable.dtDefinition.DTColumnDefinition;
import pl.rentowne.dataTable.dtDefinition.DTDefinition;

import java.util.List;

public class DTApartmentDefinition implements DTDefinition {

    @Override
    public ConstructorExpression getSelect() {
        return null;
    }

    @Override
    public EntityPathBase getEntity() {
        return null;
    }

    @Override
    public List<DTColumnDefinition> getColumnDefinitions() {
        return null;
    }
}
