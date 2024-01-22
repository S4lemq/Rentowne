package pl.rentowne.meter.repository.impl;


import jakarta.persistence.EntityManager;
import org.springframework.stereotype.Repository;
import pl.rentowne.common.repository.impl.BaseRepositoryImpl;
import pl.rentowne.meter.model.Meter;
import pl.rentowne.meter.repository.cutom.MeterRepositoryCustom;

@Repository
public class MeterRepositoryImpl extends BaseRepositoryImpl<Meter, Long> implements MeterRepositoryCustom {

    public MeterRepositoryImpl(EntityManager entityManager) {
        super(Meter.class, entityManager);
    }


}
