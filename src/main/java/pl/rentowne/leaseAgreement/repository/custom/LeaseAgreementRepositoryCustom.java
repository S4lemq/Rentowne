package pl.rentowne.leaseAgreement.repository.custom;

import pl.rentowne.leaseAgreement.model.LeaseAgreement;

import java.util.Optional;

/**
 * Rozszerzone repozytorium umów najmu
 */
public interface LeaseAgreementRepositoryCustom {
    /**
     * Pobiera optional umowy
     * @param id id umowy
     * @return optional encji umowy
     */
    Optional<LeaseAgreement> getLeaseById(Long id);
}
