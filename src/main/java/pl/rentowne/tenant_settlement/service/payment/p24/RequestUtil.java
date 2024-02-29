package pl.rentowne.tenant_settlement.service.payment.p24;

import org.apache.commons.codec.digest.DigestUtils;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import pl.rentowne.tenant_settlement.model.dto.NotificationReceiveDto;

import java.math.BigDecimal;

public class RequestUtil {

    public static TransactionRegisterRequest createRegisterRequest(PaymentMethodP24Config config, TenantSettlement tenantSettlement) {
        return TransactionRegisterRequest.builder()
                .merchantId(config.getMerchantId())
                .posId(config.getPosId())
                .sessionId(createSessionId(tenantSettlement))
                .amount(tenantSettlement.getGrossValue().movePointRight(2).intValue())
                .currency("PLN")
                .description("Płatność id:" + tenantSettlement.getId())
                .email(tenantSettlement.getTenant().getEmail())
                .client(tenantSettlement.getTenant().getFirstname() + " " + tenantSettlement.getTenant().getLastname())
                .country("PL")
                .language("pl")
                .urlReturn(generateReturnUrl(tenantSettlement.getOrderHash(), config))
                .urlStatus(generateStatusUrl(tenantSettlement.getOrderHash(), config))
                .sign(createSign(tenantSettlement, config))
                .encoding("UTF-8")
                .build();
    }

    public static TransactionVerifyRequest createVerifyRequest(PaymentMethodP24Config config, TenantSettlement tenantSettlement, NotificationReceiveDto receiveDto) {
        return TransactionVerifyRequest.builder()
                .merchantId(config.getMerchantId())
                .posId(config.getPosId())
                .sessionId(createSessionId(tenantSettlement))
                .amount(tenantSettlement.getGrossValue().movePointRight(2).intValue())
                .currency("PLN")
                .orderId(receiveDto.getOrderId())
                .sign(createVerifySign(receiveDto, tenantSettlement, config))
                .build();
    }

    public static void validateIpAddress(String remoteAddr, PaymentMethodP24Config config) {
        if (!config.getServers().contains(remoteAddr)) {
            throw new RuntimeException("Niepoprawny adres IP dla potwierdzenia płatności: " + remoteAddr);
        }
    }

    private static String generateStatusUrl(String orderHash, PaymentMethodP24Config config) {
        String baseUrl = config.isTestMode() ? config.getTestUrlStatus() : config.getUrlStatus();
        return baseUrl + "/api/tenant/notification/" + orderHash;
    }

    private static String generateReturnUrl(String orderHash, PaymentMethodP24Config config) {
        String baseUrl = config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn();
        return baseUrl + "/tenant/notification/" + orderHash;
    }

    private static String createSign(TenantSettlement tenantSettlement, PaymentMethodP24Config config) {
        String json = "{\"sessionId\":\"" + createSessionId(tenantSettlement) +
                "\",\"merchantId\":" + config.getMerchantId() +
                ",\"amount\":" + tenantSettlement.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private static String createSessionId(TenantSettlement tenantSettlement) {
        return "tenant_settlement_id_" + tenantSettlement.getId().toString();
    }

    private static String createVerifySign(NotificationReceiveDto receiveDto, TenantSettlement tenantSettlement, PaymentMethodP24Config config) {
        String json = "{\"sessionId\":\"" + createSessionId(tenantSettlement) +
                "\",\"orderId\":" + receiveDto.getOrderId() +
                ",\"amount\":" + tenantSettlement.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    public static void validate(NotificationReceiveDto receiveDto, TenantSettlement tenantSettlement, PaymentMethodP24Config config) {
        validateField(config.getMerchantId().equals(receiveDto.getMerchantId()));
        validateField(config.getPosId().equals(receiveDto.getPosId()));
        validateField(createSessionId(tenantSettlement).equals(receiveDto.getSessionId()));
        validateField(tenantSettlement.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getAmount()).movePointLeft(2)) == 0);
        validateField(tenantSettlement.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getOriginAmount()).movePointLeft(2)) == 0);
        validateField("PLN".equals(receiveDto.getCurrency()));
        validateField(createReceivedSign(receiveDto, tenantSettlement, config).equals(receiveDto.getSign()));
    }

    private static String createReceivedSign(NotificationReceiveDto receiveDto, TenantSettlement tenantSettlement, PaymentMethodP24Config config) {
        String json = "{\"merchantId\":" + config.getMerchantId() +
                ",\"posId\":" + config.getPosId() +
                ",\"sessionId\":\"" + createSessionId(tenantSettlement) +
                "\",\"amount\":" + tenantSettlement.getGrossValue().movePointRight(2).intValue() +
                ",\"originAmount\":" + tenantSettlement.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\"" +
                ",\"orderId\":" + receiveDto.getOrderId() +
                ",\"methodId\":" + receiveDto.getMethodId() +
                ",\"statement\":\"" + receiveDto.getStatement() +
                "\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private static void validateField(boolean condition) {
        if (!condition) {
            throw new RuntimeException("Walidacja niepoprawna");
        }
    }


}
