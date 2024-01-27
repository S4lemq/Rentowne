package pl.rentowne.leaseAgreement.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;
import pl.rentowne.leaseAgreement.repository.custom.LeaseAgreementRepositoryCustom;

/**
 * Repozytorium umów najmu
 */
public interface LeaseAgreementRepository extends JpaRepository<LeaseAgreement, Long>, LeaseAgreementRepositoryCustom {

}
