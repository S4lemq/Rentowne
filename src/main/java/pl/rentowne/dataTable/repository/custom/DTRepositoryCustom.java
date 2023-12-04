package pl.rentowne.dataTable.repository.custom;

import com.querydsl.core.types.ConstructorExpression;
import pl.rentowne.dataTable.dtDefinition.DTDefinition;
import pl.rentowne.dataTable.dtDefinition.DTRow;
import pl.rentowne.dataTable.enums.DTSortDirection;
import pl.rentowne.dataTable.filter.DTFilter;

import java.util.List;

public interface DTRepositoryCustom {
    /**
     * Zwraca krotki z bazy danych według argumentów.
     * @param dtDefinition definicja tabeli z danymi
     * @param offset przesuniecie danych
     * @param limit liczba maksymalnie zwróconych danych
     *              Uwaga! jeśli limit = 0 to zwraca wszsytko
     * @param sortDirection kierunek sortowania
     * @param sortColumnName kolumna, po jakiej dane mają być sortowane
     * @param dtFilters lista filtrów (z popupa i z szybkiej wyszukiwarki)
     * @return lista krotek rozszerzająca BaseDto
     */
    List<DTRow> getFiles(DTDefinition dtDefinition, long offset, long limit, DTSortDirection sortDirection,
                         String sortColumnName, List<DTFilter> dtFilters);

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
