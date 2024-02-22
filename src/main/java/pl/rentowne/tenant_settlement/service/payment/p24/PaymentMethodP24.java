package pl.rentowne.tenant_settlement.service.payment.p24;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.codec.digest.DigestUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.ExchangeFilterFunctions;
import org.springframework.web.reactive.function.client.WebClient;
import pl.rentowne.tenant_settlement.model.TenantSettlement;
import reactor.core.publisher.Mono;

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
                        .urlReturn(config.isTestMode() ? config.getTestUrlReturn() : config.getUrlReturn())
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
}
