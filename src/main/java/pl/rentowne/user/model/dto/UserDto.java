package pl.rentowne.user.model.dto;

import com.querydsl.core.annotations.QueryProjection;
import lombok.Getter;

@Getter
public class UserDto {
    private Long id;
    private String firstname;
    private String lastname;
    private String email;
    private String oldPassword;
    private String password;
    private String repeatPassword;
    private String image;

    @QueryProjection
    public UserDto(Long id, String firstname, String lastname, String email, String image) {
        this.id = id;
        this.firstname = firstname;
        this.lastname = lastname;
        this.email = email;
        this.image = image;
    }
}
