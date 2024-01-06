package pl.rentowne.user.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserBasicDto {
    private Long id;
    private String email;

    @QueryProjection
    public UserBasicDto(Long id, String email) {
        this.id = id;
        this.email = email;
    }
}
