package hu.szarvas.football_api.dto.response;

import lombok.Builder;
import lombok.Data;

import java.util.Set;

@Data
@Builder
public class UserResponseDTO {
    Integer id;
    String email;
    String username;
    Set<String> roles;
}
