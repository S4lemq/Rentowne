package pl.rentowne.common.mail;

import org.springframework.stereotype.Service;

@Service
public class WebServiceEmailService implements EmailSender {

    @Override
    public void send(String to, String subject, String msg) {
        throw new RuntimeException("Not implemented yet!");
    }
}
