package pl.rentowne.security.model.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class LostPasswordRequest {
    private String email;
    private Boolean isTenant;
}
