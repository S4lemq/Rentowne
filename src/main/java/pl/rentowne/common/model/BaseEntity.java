package pl.rentowne.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@MappedSuperclass
@EntityListeners({AuditingEntityListener.class})
@Getter
@Setter
public class BaseEntity {
    public static final String COL_INSERT_DATE = "INSERT_DATE";
    public static final String COL_UPDATE_DATE = "UPDATE_DATE";
    public static final String COL_INSERT_OPERATOR = "INSERT_OPERATOR";
    public static final String COL_UPDATE_OPERATOR = "UPDATE_OPERATOR";

    @CreatedDate
    @Column(name = COL_INSERT_DATE, nullable = false, updatable = false)
    private LocalDateTime insertDate;

    @LastModifiedDate
    @Column(name = COL_UPDATE_DATE, insertable = false)
    private LocalDateTime updateDate;

    @CreatedBy
    @Column(name = COL_INSERT_OPERATOR, nullable = false, updatable = false, length = 60)
    private String insertOperator;

    @LastModifiedBy
    @Column(name = COL_UPDATE_OPERATOR, insertable = false, length = 60)
    private String updateOperator;

}
