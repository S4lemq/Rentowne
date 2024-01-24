package pl.rentowne.rentedObject.model;

import com.querydsl.core.types.ConstructorExpression;
import com.querydsl.core.types.dsl.EntityPathBase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import pl.rentowne.dataTable.dtDefinition.DTColumnDefinition;
import pl.rentowne.dataTable.dtDefinition.DTDefinition;

import java.util.List;

@RequiredArgsConstructor
@Component("RENTED_OBJECT")
public class DTRentedObjectDefinition implements DTDefinition {



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
