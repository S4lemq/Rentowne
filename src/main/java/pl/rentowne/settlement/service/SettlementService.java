package pl.rentowne.settlement.service;

public interface SettlementService {

    void calculate(Long rentedObjectId, boolean waterIncluded, boolean electricityIncluded);
}
