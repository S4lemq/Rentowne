package pl.rentowne.payment_card.model.dto;

import lombok.Builder;
import lombok.Getter;
import pl.rentowne.payment_card.model.PaymentCard;

@Getter
@Builder
public class PaymentCardDto {
    private Long id;
    private String number;
    private String name;
    private int month;
    private int year;
    private int cvv;

    public static PaymentCardDto asDto(PaymentCard entity) {
        return PaymentCardDto.builder()
                .id(entity.getId())
                .number(entity.getNumber())
                .name(entity.getName())
                .month(entity.getMonth())
                .year(entity.getYear())
                .cvv(entity.getCvv())
                .build();
    }
}
