package pl.rentowne.tenant_settlement.service.payment.p24;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import pl.rentowne.tenant_settlement.model.dto.NotificationReceiveDto;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@Slf4j
@RequiredArgsConstructor
public class PaymentMethodP24 {

    private final PaymentMethodP24Config config;

    public String initPayment(TenantSettlement tenantSettlement) {
        log.info("Inicjalizacja płatności");

        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(config.getPosId().toString(),
                        config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey()))
                .baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();

        ResponseEntity<TransactionRegisterResponse> result = webClient.post().uri("/transaction/register")
                .bodyValue(TransactionRegisterRequest.builder()
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
                        .urlReturn(generateReturnUrl(tenantSettlement.getOrderHash()))
                        .urlStatus(generateStatusUrl(tenantSettlement.getOrderHash()))
                        .sign(createSign(tenantSettlement))
                        .encoding("UTF-8")
                        .build())
                .retrieve()
                .onStatus(httpStatus -> {
                        log.error("Coś poszło źle: " + httpStatus.toString());
                        return httpStatus.is4xxClientError();
                }, clientResponse -> Mono.empty())
                .toEntity(TransactionRegisterResponse.class)
                .block();
        if (result != null && result.getBody() != null && result.getBody().getData() != null) {
            return (config.isTestMode() ? config.getTestUrl() : config.getUrl()) + "/trnRequest/"
                    + result.getBody().getData().token();
        }
        return null;
    }

    private String generateStatusUrl(String orderHash) {
        String baseUrl = config.isTestMode() ? config.getTestUrlStatus() : config.getUrlStatus();
        String url = baseUrl + "/api/tenant/notification/" + orderHash;
        log.info("generated status url: {}", url);
        return url;
    }

    private String generateReturnUrl(String orderHash) {
        String baseUrl = config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn();
        String url = baseUrl + "/tenant/notification/" + orderHash;

        log.info("generated return url: {}", url);
        return url;
    }


    private String createSign(TenantSettlement tenantSettlement) {
        String json  = "{\"sessionId\":\""+ createSessionId(tenantSettlement) +
                "\",\"merchantId\":"+ config.getMerchantId() +
                ",\"amount\":"+ tenantSettlement.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\""+ (config.isTestMode() ? config.getTestCrc(): config.getCrc())+"\"}";
        return DigestUtils.sha384Hex(json);
    }

    private String createSessionId(TenantSettlement tenantSettlement) {
        return "tenant_settlement_id_" + tenantSettlement.getId().toString();
    }

    public String receiveNotification(TenantSettlement tenantSettlement, NotificationReceiveDto receiveDto) {
        log.info(receiveDto.toString());
        this.validate(receiveDto, tenantSettlement);
        return this.verifyPayment(receiveDto, tenantSettlement);
    }

    private String verifyPayment(NotificationReceiveDto receiveDto, TenantSettlement tenantSettlement) {
        WebClient webClient = WebClient.builder()
                .filter(ExchangeFilterFunctions.basicAuthentication(config.getPosId().toString(),
                        config.isTestMode() ? config.getTestSecretKey() : config.getSecretKey()))
                .baseUrl(config.isTestMode() ? config.getTestApiUrl() : config.getApiUrl())
                .build();

        ResponseEntity<TransactionVerifyResponse> result = webClient.put().uri("transaction/verify")
                .bodyValue(TransactionVerifyRequest.builder()
                        .merchantId(config.getMerchantId())
                        .posId(config.getPosId())
                        .sessionId(createSessionId(tenantSettlement))
                        .amount(tenantSettlement.getGrossValue().movePointRight(2).intValue())
                        .currency("PLN")
                        .orderId(receiveDto.getOrderId())
                        .sign(createVerifySign(receiveDto, tenantSettlement))
                        .build())
                .retrieve()
                .toEntity(TransactionVerifyResponse.class)
                .block();
        log.info("Weryfikacja transakcji status: " + result.getBody().getData().status());
        return result.getBody().getData().status();
    }

    private String createVerifySign(NotificationReceiveDto receiveDto, TenantSettlement tenantSettlement) {
        String json = "{\"sessionId\":\"" + createSessionId(tenantSettlement) +
                "\",\"orderId\":" + receiveDto.getOrderId() +
                ",\"amount\":" + tenantSettlement.getGrossValue().movePointRight(2).intValue() +
                ",\"currency\":\"PLN\",\"crc\":\"" + (config.isTestMode() ? config.getTestCrc() : config.getCrc()) + "\"}";
        return DigestUtils.sha384Hex(json);
    }

    private void validate(NotificationReceiveDto receiveDto, TenantSettlement tenantSettlement) {
        validateField(config.getMerchantId().equals(receiveDto.getMerchantId()));
        validateField(config.getPosId().equals(receiveDto.getPosId()));
        validateField(createSessionId(tenantSettlement).equals(receiveDto.getSessionId()));
        validateField(tenantSettlement.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getAmount()).movePointLeft(2)) == 0);
        validateField(tenantSettlement.getGrossValue().compareTo(BigDecimal.valueOf(receiveDto.getOriginAmount()).movePointLeft(2)) == 0);
        validateField("PLN".equals(receiveDto.getCurrency()));
        validateField(createReceivedSign(receiveDto, tenantSettlement).equals(receiveDto.getSign()));
    }

    private String createReceivedSign(NotificationReceiveDto receiveDto, TenantSettlement tenantSettlement) {
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

    private void validateField(boolean condition) {
        if (!condition) {
            throw new RuntimeException("Walidacja niepoprawna");
        }
    }

}
