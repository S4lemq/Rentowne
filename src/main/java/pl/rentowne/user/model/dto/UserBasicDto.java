package pl.rentowne.user.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;
import pl.rentowne.user.model.Role;

@Getter
public class UserBasicDto {
    private Long id;
    private String email;
    private Role role;

    @QueryProjection
    public UserBasicDto(Long id, String email, Role role) {
        this.id = id;
        this.email = email;
        this.role = role;
    }

}
