package pl.rentowne.user.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.address.model.dto.AddressDto;
import pl.rentowne.payment_card.model.dto.PaymentCardDto;

@Getter
@Builder
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String oldPassword;
    private String password;
    private String repeatPassword;
    private String image;
    private String phoneNumber;
    private PaymentCardDto paymentCardDto;
    private AddressDto addressDto;
}
