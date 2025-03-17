package hu.szarvas.football_api.model;

import lombok.Builder;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.Transient;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashSet;
import java.util.Set;

@Data
@Builder
@Document(collection = "users")
public class User {
    @Id
    private Integer id;

    @Transient
    public static final String SEQUENCE_NAME = "user_sequence";

    @Indexed(unique = true)
    private String email;

    @Indexed(unique = true)
    private String username;

    private String password;
    private String refreshToken;

    @Builder.Default
    private Set<String> roles = new HashSet<>();
}
