package pl.rentowne.common.mail;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class EmailSimpleService implements EmailSender {

    private final JavaMailSender mailSender;

    @Async
    @Override
    public void send(String to, String subject, String msg) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("Rentowne <rentowneApp@gmail.com>");
        message.setReplyTo("Rentowne <rentowneApp@gmail.com>");
        message.setTo(to);
        message.setSubject(subject);
        message.setText(msg);
        log.info("trying email send to: {}", to);
        mailSender.send(message);
        log.info("email was sent to: {}", to);
    }

}
