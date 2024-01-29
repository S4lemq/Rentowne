package pl.rentowne.common.mail;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmailClientService {
    @Value("${application.email.sender}")
    private String isFakeProp;
    private final Map<String, EmailSender> senderMap;

    public EmailSender getInstance() {
        if (isFakeProp.equals("fakeEmailService")) {
            return senderMap.get("fakeEmailService");
        }
        return senderMap.get("emailSimpleService");
    }

    public EmailSender getInstance(String beanName) {
        if (isFakeProp.equals("fakeEmailService")) {
            return senderMap.get("fakeEmailService");
        }
        return senderMap.get(beanName);
    }
}
