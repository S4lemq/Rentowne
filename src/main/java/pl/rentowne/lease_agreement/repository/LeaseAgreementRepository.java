package pl.rentowne.lease_agreement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.lease_agreement.model.LeaseAgreement;
import pl.rentowne.lease_agreement.repository.custom.LeaseAgreementRepositoryCustom;

/**
 * Repozytorium umów najmu
 */
public interface LeaseAgreementRepository extends JpaRepository<LeaseAgreement, Long>, LeaseAgreementRepositoryCustom {

}
