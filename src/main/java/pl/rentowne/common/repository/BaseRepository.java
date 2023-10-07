package pl.rentowne.common.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.NoRepositoryBean;

/**
 * Bazowe repozytorium
 *
 * @param <T>  encja
 * @param <ID> typ id encji
 */
@NoRepositoryBean
public interface BaseRepository<T, ID> extends JpaRepository<T, ID> {

    /**
     * Zwraca wiersz po id
     *
     * @param id id
     * @return encja
     * @throws IllegalArgumentException
     */
    T findByIdMandatory(ID id) throws IllegalArgumentException;
}
