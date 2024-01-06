package pl.rentowne.security.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationRequest {
    @Schema(description = "email", example = "peter.lubowicz@gmail.com", required = true)
    private String email;
    @Schema(description = "hasło", example = "Rentowne2023!", required = true)
    private String password;
}