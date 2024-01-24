package pl.rentowne.leaseAgreement.service;

import pl.rentowne.exception.RentowneNotFoundException;
import pl.rentowne.leaseAgreement.model.dto.LeaseAgreementDto;

/**
 * Serwis umów najmu
 */
public interface LeaseAgreementService {

    /**
     * Dodaje nową umowę
     * @param leaseAgreementDto dto umowy
     * @return id zapisanej umowy
     */
    Long addLease(LeaseAgreementDto leaseAgreementDto);

    /**
     * Pobiera umowę po id
     * @param id id umowy
     * @return dto umowy
     * @throws RentowneNotFoundException błąd gdy umowa o podanym id nie istnieje
     */
    LeaseAgreementDto getDto(Long id) throws RentowneNotFoundException;

    /**
     * Aktualizuje dane umowy
     * @param dto zaktualizowana umowa
     */
    void updateLease(LeaseAgreementDto dto);
}
