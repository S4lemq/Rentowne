package pl.rentowne.security.model.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import pl.rentowne.user.model.PreferredLanguage;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_EMPTY)
public class AuthenticationResponse {
    private String accessToken;
    private String refreshToken;
    private boolean mfaEnabled;
    private String secretImageUri;
    private boolean landlordAccess;
    private PreferredLanguage preferredLanguage;
}
