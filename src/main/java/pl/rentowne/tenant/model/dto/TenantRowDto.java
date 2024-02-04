package pl.rentowne.tenant.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.data_table.dt_definition.DTRow;

import java.time.LocalDateTime;

@Getter
public class TenantRowDto implements DTRow {

    private Long id;
    private String firstname;
    private String lastname;
    private String apartmentName;
    private String rentedObjectName;
    private String phoneNumber;
    private String email;
    private LocalDateTime endContractDate;

    @QueryProjection
    public TenantRowDto(Long id, String firstname, String lastname, String apartmentName, String rentedObjectName, String phoneNumber, String email, LocalDateTime endContractDate) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.apartmentName = apartmentName;
        this.rentedObjectName = rentedObjectName;
        this.phoneNumber = phoneNumber;
        this.email = email;
        this.endContractDate = endContractDate;
    }
}
