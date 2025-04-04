package hu.szarvas.football_api.service;

import hu.szarvas.football_api.dto.request.UserRequestDTO;
import hu.szarvas.football_api.dto.response.UserResponseDTO;
import hu.szarvas.football_api.model.User;
import hu.szarvas.football_api.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Service for user-related operations.
 */
@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    /**
     * Retrieves user information by username and email.
     *
     * @param request Contains username and email for lookup
     * @return ResponseEntity with user data if found, BAD_REQUEST otherwise
     */
    public ResponseEntity<UserResponseDTO> getUserInfo(UserRequestDTO request) {
        Optional<User> user = userRepository.findByUsernameAndEmail(request.getUsername(), request.getEmail());

        return user.map(value -> ResponseEntity
                .status(HttpStatus.OK)
                .body(UserResponseDTO.builder()
                        .id(value.getId())
                        .email(value.getEmail())
                        .username(value.getUsername())
                        .roles(value.getRoles())
                        .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }

    /**
     * Retrieves user information by user ID.
     *
     * @param userId The ID of the user to retrieve
     * @return ResponseEntity with user data if found, BAD_REQUEST otherwise
     */
    public ResponseEntity<UserResponseDTO> getUser(Integer userId) {
        Optional<User> user = userRepository.findById(userId);

        return user.map(value -> ResponseEntity
                .status(HttpStatus.OK)
                .body(UserResponseDTO.builder()
                        .id(value.getId())
                        .email(value.getEmail())
                        .username(value.getUsername())
                        .roles(value.getRoles())
                        .build())).orElseGet(() -> ResponseEntity.status(HttpStatus.BAD_REQUEST).build());
    }
}