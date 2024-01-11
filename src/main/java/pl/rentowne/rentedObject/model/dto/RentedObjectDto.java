package pl.rentowne.rentedObject.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class RentedObjectDto {
    private String rentedObjectName;

    @QueryProjection
    public RentedObjectDto(String rentedObjectName) {
        this.rentedObjectName = rentedObjectName;
    }
}

