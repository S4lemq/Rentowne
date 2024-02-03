package pl.rentowne.data_table.service;

import com.querydsl.core.types.ConstructorExpression;
import pl.rentowne.data_table.dt_definition.DTDefinition;
import pl.rentowne.data_table.dt_definition.DTRow;
import pl.rentowne.data_table.enums.DTSortDirection;
import pl.rentowne.data_table.filter.DTFilter;

import java.util.List;

/**
 * Serwis dla tabeli z danymi
 */
public interface DTService {

    /**
     * Zwraca krotki z bazy danych według argumentów.
     * @param dtDefinition definicja tabeli z danymi
     * @param offset przesuniecie danych
     * @param limit liczba maksymalnie zwróconych danych
     * @param sortDirection kierunek sortowania
     * @param sortColumnName kolumna, po jakiej dane mają być sortowane
     * @param dtFilters lista filtrów (z popupa i z szybkiej wyszukiwarki)
     * @return lista krotek rozszerzająca BaseDto
     */
    List<DTRow> getFiles(DTDefinition dtDefinition, int offset, int limit, DTSortDirection sortDirection, String sortColumnName,
                         List<DTFilter> dtFilters);

    /**
     * Zwraca krotki z bazy danych według argumentów.
     * @param dtDefinition definicja tabeli z danymi
     * @param offset przesuniecie danych
     * @param limit liczba maksymalnie zwróconych danych
     *              Uwaga! jeśli limit = 0 to zwraca wszsytko
     * @param sortDirection kierunek sortowania
     * @param sortColumnName kolumna, po jakiej dane mają być sortowane
     * @param dtFilters lista filtrów (z popupa i z szybkiej wyszukiwarki)
     * @param constructorExpression wyrażenie tworzące zwracany obiekt, używane w query.select
     * @return lista krotek rozszerzająca BaseDto
     */
    List<DTRow> getFiles(DTDefinition dtDefinition, long offset, long limit,
                         DTSortDirection sortDirection, String sortColumnName, List<DTFilter> dtFilters,
                         ConstructorExpression constructorExpression);

    /**
     * Zwraca liczbę wierszy dostępna w bazie danych według argumentów
     * @param dtDefinition definicja tabeli z danymi
     * @param dtFilters lista filtrów (z popupa i z szybkiej wyszukiwarki)
     * @return liczba wierszy dostępna w bazie danych
     */
    long count(DTDefinition dtDefinition, List<DTFilter> dtFilters);
}
