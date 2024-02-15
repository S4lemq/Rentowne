package pl.rentowne.settlement.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pl.rentowne.settlement.model.dto.SettlementDateAmountDto;
import pl.rentowne.settlement.model.dto.SettlementStats;
import pl.rentowne.settlement.repository.SettlementRepository;
import pl.rentowne.settlement.service.SettlementStatsService;
import pl.rentowne.user.service.UserService;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.Month;
import java.time.format.TextStyle;
import java.util.*;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SettlementStatsServiceImpl implements SettlementStatsService {

    private final SettlementRepository settlementRepository;
    private final UserService userService;

    @Override
    public SettlementStats getStatistics() {
        Long id = userService.getLoggedUser().getId();
        List<SettlementDateAmountDto> settlements = settlementRepository.getDataForStatistics(id);

        LocalDateTime from = settlements.stream()
                .map(SettlementDateAmountDto::getDate)
                .min(Comparator.naturalOrder()).get();

        LocalDateTime to = settlements.stream()
                .map(SettlementDateAmountDto::getDate)
                .max(Comparator.naturalOrder()).get();

        TreeMap<Integer, SettlementStatsValue> result = new TreeMap<>();
        for (int i = from.getMonthValue(); i <= to.getMonthValue(); i++) {
            result.put(i, aggregateValues(i, settlements));
        }

        return SettlementStats.builder()
                .label(result.keySet().stream()
                        .map(monthValue -> Month.of(monthValue)
                                .getDisplayName(TextStyle.FULL_STANDALONE, new Locale("pl", "PL")))
                        .collect(Collectors.toList()))
                .totalAmount(result.values().stream()
                        .map(o -> o.totalAmount)
                        .collect(Collectors.toList()))
                .tenant(result.values().stream()
                        .map(o -> o.tenants)
                        .collect(Collectors.toList()))
                .build();
    }

    private SettlementStatsValue aggregateValues(int i, List<SettlementDateAmountDto> settlements) {
        BigDecimal totalAmount = BigDecimal.ZERO;
        Long tenantCount = 0L;

        for (SettlementDateAmountDto settlement : settlements) {
            if (i == settlement.getDate().getMonthValue()) {
                totalAmount = totalAmount.add(settlement.getTotalAmount());
                tenantCount++;
            }
        }
        return new SettlementStatsValue(totalAmount, tenantCount);
    }

    private record SettlementStatsValue(BigDecimal totalAmount, Long tenants){}
}
