package hu.szarvas.football_api.service;

import hu.szarvas.football_api.dto.request.UserRequestDTO;
import hu.szarvas.football_api.dto.response.UserResponseDTO;
import hu.szarvas.football_api.model.User;
import hu.szarvas.football_api.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserService userService;

    private User testUser;

    @BeforeEach
    void setUp() {
        testUser = User.builder()
                .id(1)
                .username("testuser")
                .email("test@example.com")
                .roles(Set.of("USER"))
                .build();
    }

    @Test
    void getUserInfo_userFound_returnsUserData() {
        UserRequestDTO request = new UserRequestDTO("testuser", "test@example.com");
        when(userRepository.findByUsernameAndEmail("testuser", "test@example.com"))
                .thenReturn(Optional.of(testUser));

        ResponseEntity<UserResponseDTO> response = userService.getUserInfo(request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getId());
        assertEquals("testuser", body.getUsername());
        assertEquals("test@example.com", body.getEmail());
        assertEquals(Set.of("USER"), body.getRoles());
        verify(userRepository).findByUsernameAndEmail("testuser", "test@example.com");
    }

    @Test
    void getUserInfo_userNotFound_returnsBadRequest() {
        UserRequestDTO request = new UserRequestDTO("testuser", "test@example.com");
        when(userRepository.findByUsernameAndEmail("testuser", "test@example.com"))
                .thenReturn(Optional.empty());

        ResponseEntity<UserResponseDTO> response = userService.getUserInfo(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userRepository).findByUsernameAndEmail("testuser", "test@example.com");
    }

    @Test
    void getUser_userFound_returnsUserData() {
        when(userRepository.findById(1)).thenReturn(Optional.of(testUser));

        ResponseEntity<UserResponseDTO> response = userService.getUser(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        UserResponseDTO body = response.getBody();
        assertNotNull(body);
        assertEquals(1, body.getId());
        assertEquals("testuser", body.getUsername());
        assertEquals("test@example.com", body.getEmail());
        assertEquals(Set.of("USER"), body.getRoles());
        verify(userRepository).findById(1);
    }

    @Test
    void getUser_userNotFound_returnsBadRequest() {
        when(userRepository.findById(1)).thenReturn(Optional.empty());

        ResponseEntity<UserResponseDTO> response = userService.getUser(1);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertNull(response.getBody());
        verify(userRepository).findById(1);
    }
}