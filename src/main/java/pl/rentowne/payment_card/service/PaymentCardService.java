package pl.rentowne.payment_card.service;

import pl.rentowne.payment_card.model.dto.PaymentCardDto;
import pl.rentowne.user.model.User;

public interface PaymentCardService {
    /**
     * Zapisuje kartę płatniczą
     *
     * @param paymentCardDto dane karty płatniczej
     * @param user encja posiadacza karty
     */
    void save(PaymentCardDto paymentCardDto, User user);
}
