package pl.rentowne.dataTable.filter;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import pl.rentowne.dataTable.dtDefinition.DTColumnDefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public class DTFilter {
    private Map<DTFilterColumnDefinition, Object> filterValues = new HashMap<>();
    private boolean forFastSearcher;

    public DTFilter(boolean forFastSearcher) {
        this.forFastSearcher = forFastSearcher;
    }

    private void put(DTFilterColumnDefinition dtFilterColumnDefinition, Object value) {
        filterValues.put(dtFilterColumnDefinition, value);
    }

    public Map<DTFilterColumnDefinition, Object> getFilterValues() {
        return filterValues;
    }

    public boolean isForFastSearcher() {
        return forFastSearcher;
    }

    public List<DTColumnDefinition> getFilterColumnDefinitions() {
        List<DTColumnDefinition> dtFilterColumnDefinitions = new ArrayList<>();
        for (Map.Entry<DTFilterColumnDefinition, Object> filterEntry : filterValues.entrySet()) {
            DTFilterColumnDefinition dtFilterColumnDefinition = filterEntry.getKey();
            if (dtFilterColumnDefinition.getName() != null) {
                dtFilterColumnDefinitions.add(dtFilterColumnDefinition);
            } else if (filterEntry.getValue() instanceof DTFilter) {
                DTFilter innerFilter = (DTFilter) filterEntry.getValue();
                dtFilterColumnDefinitions.addAll(innerFilter.getFilterColumnDefinitions());
            }
        }
        return dtFilterColumnDefinitions;
    }

    public static DTFilter buildDTFilter(List<DTFilterColumnDefinition> definitions, Map<String, Object> filters) {
        DTFilter dtFilter = new DTFilter(false);

        List<DTFilterColumnDefinition> requiredFilters = definitions.stream().filter(DTFilterColumnDefinition::isValueRequired).collect(Collectors.toList());

        if (filters == null) {
            if (CollectionUtils.isNotEmpty(requiredFilters)) {
                throw new IllegalArgumentException("DT Bad Request");
            } else {
                return dtFilter;
            }
        }

        for (DTFilterColumnDefinition requiredFilter : requiredFilters) {
            Object filterVal = filters.get(requiredFilter.getName());
            if (filterVal == null || StringUtils.isBlank(filterVal.toString())) {
                throw new IllegalArgumentException("DT Bad Request");
            }
        }

        Map<String, DTFilterColumnDefinition> definitionsMap = definitions.stream()
                .collect(Collectors.toMap(DTFilterColumnDefinition::getName, Function.identity()));

        for (Map.Entry<String, Object> filterEntry : filters.entrySet()) {
            DTFilterColumnDefinition dtFilterColumnDefinition = definitionsMap.get(filterEntry.getKey());
            if (dtFilterColumnDefinition != null && StringUtils.isNotEmpty(filterEntry.getValue().toString())) {
                dtFilter.put(dtFilterColumnDefinition, filterEntry.getValue());
            } else {
                if (filterEntry.getValue() instanceof Map) {
                    Map<String, Object> innerFilter = (Map<String, Object>) filterEntry.getValue();
                    if (innerFilter != null) {
                        dtFilter.put(new DTFilterColumnDefinition(), buildDTFilter(definitions, innerFilter));
                    }
                }
            }
        }
        return dtFilter;
    }

    public static DTFilter buildDTFilterForFastSearcher(List<DTFilterColumnDefinition> definitions, String text) {
        DTFilter dtFilter = new DTFilter(true);

        if (StringUtils.isEmpty(text)) {
            return dtFilter;
        }

        for (DTFilterColumnDefinition definition : definitions) {
            dtFilter.put(definition, text);
        }

        return dtFilter;
    }
}
