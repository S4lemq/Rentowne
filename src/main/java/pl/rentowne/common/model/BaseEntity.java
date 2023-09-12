package pl.rentowne.common.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;


import java.time.LocalDateTime;

@MappedSuperclass
//@EntityListeners({RentowneBaseListener.class})
@Getter
@Setter
public class BaseEntity {

    public static final String COL_ID = "ID";
    public static final String COL_INSERT_DATE = "INSERT_DATE";
    public static final String COL_UPDATE_DATE = "UPDATE_DATE";
    public static final String COL_INSERT_OPERATOR = "INSERT_OPERATOR";
    public static final String COL_UPDATE_OPERATOR = "UPDATE_OPERATOR";

    @Column(name = COL_INSERT_DATE, nullable = false)
    private LocalDateTime insertDate;

    @Column(name = COL_UPDATE_DATE)
    private LocalDateTime updateDate;

    @Column(name = COL_INSERT_OPERATOR, nullable = false)
    private String insertOperator;

    @Column(name = COL_UPDATE_OPERATOR)
    private String updateOperator;


}
