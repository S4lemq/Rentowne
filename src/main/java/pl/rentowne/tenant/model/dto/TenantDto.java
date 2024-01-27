package pl.rentowne.tenant.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.address.model.dto.AddressDto;
import pl.rentowne.leaseAgreement.model.LeaseAgreement;
import pl.rentowne.leaseAgreement.model.dto.LeaseAgreementDto;
import pl.rentowne.tenant.model.Tenant;

@Getter
@Builder
public class TenantDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String accountNumber;
    private String phoneNumber;
    private AddressDto addressDto;
    private LeaseAgreementDto leaseAgreementDto;


    public static Tenant asEntity(TenantDto dto) {
        return Tenant.builder()
                .id(dto.id)
                .firstname(dto.firstname)
                .lastname(dto.lastname)
                .email(dto.email)
                .accountNumber(dto.accountNumber)
                .phoneNumber(dto.phoneNumber)
                .address(AddressDto.asEntity(dto.addressDto))
                .leaseAgreement(LeaseAgreementDto.asEntity(dto.getLeaseAgreementDto()))
                .build();
    }

    public static TenantDto asDto(Tenant entity) {
        return TenantDto.builder()
                .id(entity.getId())
                .firstname(entity.getFirstname())
                .lastname(entity.getLastname())
                .email(entity.getEmail())
                .accountNumber(entity.getAccountNumber())
                .phoneNumber(entity.getPhoneNumber())
                .addressDto(AddressDto.asDto(entity.getAddress()))
                .leaseAgreementDto(LeaseAgreementDto.asDto(entity.getLeaseAgreement()))
                .build();
    }

}
