package pl.rentowne.settlement.service;

import pl.rentowne.settlement.model.dto.SettlementStats;

public interface SettlementStatsService {
    /**
     * Pobiera dane do wyświetlenia statystyk rozliczeń
     * @return {@link SettlementStats}
     */
    SettlementStats getStatistics();

}
