package pl.rentowne.address.repository.impl;

import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.address.model.Address;
import pl.rentowne.address.model.QAddress;
import pl.rentowne.address.repository.custom.AddressRepositoryCustom;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;

@Repository
public class AddressRepositoryImpl extends BaseRepositoryImpl<Address, Long> implements AddressRepositoryCustom {

    private static final QAddress address = QAddress.address;

    public AddressRepositoryImpl(EntityManager entityManager) {
        super(Address.class, entityManager);
    }


}
