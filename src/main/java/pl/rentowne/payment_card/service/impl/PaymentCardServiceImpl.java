package pl.rentowne.payment_card.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.payment_card.model.PaymentCard;
import pl.rentowne.payment_card.model.dto.PaymentCardDto;
import pl.rentowne.payment_card.repository.PaymentCardRepository;
import pl.rentowne.payment_card.service.PaymentCardService;
import pl.rentowne.user.model.User;

@Service
@RequiredArgsConstructor
public class PaymentCardServiceImpl implements PaymentCardService {

    private final PaymentCardRepository paymentCardRepository;

    @Override
    public void save(PaymentCardDto paymentCardDto, User user) {
        PaymentCard paymentCard = PaymentCard.builder()
                .id(paymentCardDto.getId())
                .number(paymentCardDto.getNumber())
                .name(paymentCardDto.getName())
                .month(paymentCardDto.getMonth())
                .year(paymentCardDto.getYear())
                .cvv(paymentCardDto.getCvv())
                .user(user)
                .build();
        paymentCardRepository.save(paymentCard);
    }
}
