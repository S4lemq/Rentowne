package pl.rentowne.lease_agreement.repository.custom;

import pl.rentowne.lease_agreement.model.dto.LeaseAgreementDto;

/**
 * Rozszerzone repozytorium umów najmu
 */
public interface LeaseAgreementRepositoryCustom {

    /**
     * Pobiera dane umowy do rozliczeń
     * @param rentedObjectId id obiektu, który jest przypisany do umowy
     * @return dane umowy
     */
    LeaseAgreementDto getData(Long rentedObjectId);
}
