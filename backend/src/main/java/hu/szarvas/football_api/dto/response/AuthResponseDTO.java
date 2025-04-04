package hu.szarvas.football_api.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthResponseDTO {
    String accessToken;
    String refreshToken;
    String user;
    Integer userId;
    Set<String> roles;
}
