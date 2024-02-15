package pl.rentowne.settlement.controller;

import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pl.rentowne.common.controler.AbstractController;
import pl.rentowne.settlement.model.dto.SettlementStats;
import pl.rentowne.settlement.service.SettlementStatsService;

@RestController
@RequiredArgsConstructor
@Tag(name = "Statystyki rozliczeń")
@RequestMapping("/api/settlements/stats")
public class SettlementStatsController extends AbstractController {

    private final SettlementStatsService settlementStatsService;

    @GetMapping
    public SettlementStats getSettlementStatistics() {
        return settlementStatsService.getStatistics();
    }
}
