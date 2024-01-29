package pl.rentowne.common.mail;

public interface EmailSender {

    /**
     * Wysyła maila
     * @param to do kogo mail
     * @param subject temat maila
     * @param msg treść maila
     */
    void send(String to, String subject, String msg);
}
