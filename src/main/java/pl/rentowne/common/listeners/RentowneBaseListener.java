package pl.rentowne.common.listeners;

import org.hibernate.grammars.hql.HqlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.support.TransactionSynchronizationAdapter;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import pl.rentowne.common.model.BaseEntity;

import java.time.LocalDateTime;
import java.util.Date;

@Component
public class RentowneBaseListener {
    /*private static TrackingChangesService trackingChangesService;

    @Autowired
    @SuppressWarnings("squid:S2696") //hack for handling Spring components into EntityListener
    public void init(TrackingChangesService trackingChangesService) {
        RentowneBaseListener.trackingChangesService = trackingChangesService;
    }

    @PrePersist
    public void setInsertAndUpdateDatesAndOperator(BaseEntity entity) {
        LocalDateTime insertDate = LocalDateTime.now();
        UserProfile creator = getCurrentProfile();
        //nie nadpisujemy jeśli data i/lub operator już jest, np. w przypadku kopiowanych krotek na potrzeby mechanizmu akceptacji
        if(entity.getInsertDate() == null)
            entity.setInsertDate(insertDate);
        if(entity.getInsertOperator() == null)
            entity.setInsertOperator(creator);
        setUpdateDateAndOperator(entity, creator, insertDate);
    }

    @PostPersist
    @PostUpdate
    public void setIntegrationTrackingChange(BaseEntity entity) {
        if (entity instanceof AbstractTrackingChange) {
            return;
        }
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronizationAdapter() {
            @Override
            public void afterCommit() {
                IntegrationType integrationType = TfmsContext.getIntegrationType();
                if (integrationType == null) {
                    return;
                }
                if (entity != null) {
                    Long mainId = entity instanceof AcceptableEntity ? ((AcceptableEntity) entity).getModifiedId() : null;
                    trackingChangesService.registerIntegrationChange(entity.getClass(), entity.getId(), integrationType, mainId);
                }
            }
        });
    }

    @PreUpdate
    public void setUpdateDateAndOperator(BaseEntity entity) {
        setUpdateDateAndOperator(entity, getCurrentProfile(), LocalDateTime.now());
    }

    private void setUpdateDateAndOperator(BaseEntity entity, UserProfile operator, LocalDateTime updateDate) {
        entity.setUpdateDate(updateDate);
        entity.setUpdateOperator(operator.getId());
    }

    private UserProfile getCurrentProfile() {
        final UserProfile userProfile = new UserProfile();
        Long profileId = TfmsContext.getProfileId();
        userProfile.setId(profileId);
        return userProfile;
    }*/
}
