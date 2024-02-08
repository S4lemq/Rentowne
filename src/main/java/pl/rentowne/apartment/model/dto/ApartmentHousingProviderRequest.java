package pl.rentowne.apartment.model.dto;

import lombok.Getter;

import java.util.List;

@Getter
public class ApartmentHousingProviderRequest {
    private List<Long> housingProviderIds;
    private Long apartmentId;
}
