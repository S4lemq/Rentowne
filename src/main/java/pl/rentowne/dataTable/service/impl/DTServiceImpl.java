package pl.rentowne.dataTable.service.impl;

import com.querydsl.core.types.ConstructorExpression;
import org.springframework.stereotype.Service;
import pl.rentowne.dataTable.dtDefinition.DTDefinition;
import pl.rentowne.dataTable.dtDefinition.DTRow;
import pl.rentowne.dataTable.enums.DTSortDirection;
import pl.rentowne.dataTable.filter.DTFilter;
import pl.rentowne.dataTable.repository.DTRepository;
import pl.rentowne.dataTable.service.DTService;

import java.util.List;

@Service
public class DTServiceImpl implements DTService {

    private final DTRepository dtRepository;

    public DTServiceImpl(DTRepository dtRepository) {
        this.dtRepository = dtRepository;
    }

    @Override
    public List<DTRow> getFiles(DTDefinition dtDefinition, int offset, int limit, DTSortDirection sortDirection, String sortColumnName,
                                List<DTFilter> dtFilters) {
        return dtRepository.getFiles(dtDefinition, offset, limit, sortDirection, sortColumnName, dtFilters);
    }

    @Override
    public List<DTRow> getFiles(DTDefinition dtDefinition, long offset, long limit, DTSortDirection sortDirection, String sortColumnName,
                                List<DTFilter> dtFilters, ConstructorExpression constructorExpression) {
        return dtRepository.getFiles(dtDefinition, offset, limit, sortDirection, sortColumnName, dtFilters, constructorExpression);
    }

    @Override
    public long count(DTDefinition dtDefinition, List<DTFilter> dtFilters) {
        return dtRepository.count(dtDefinition, dtFilters);
    }
}
