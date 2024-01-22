package pl.rentowne.rentedObject.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import pl.rentowne.meter.model.dto.MeterDto;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@Builder
public class RentedObjectDto {
    private Long id;
    private String rentedObjectName;
    private List<MeterDto> meters;

    @QueryProjection
    public RentedObjectDto(Long id, String rentedObjectName) {
        this.id = id;
        this.rentedObjectName = rentedObjectName;
    }

    @QueryProjection
    public RentedObjectDto(Long id, String rentedObjectName, List<MeterDto> meters) {
        this.id = id;
        this.rentedObjectName = rentedObjectName;
        this.meters = meters;
    }
}

