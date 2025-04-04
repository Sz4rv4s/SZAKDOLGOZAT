package hu.szarvas.football_api.controller;


import hu.szarvas.football_api.dto.request.UserRequestDTO;
import hu.szarvas.football_api.dto.response.UserResponseDTO;
import hu.szarvas.football_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller for accessing and managing user information.
 */
@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    /**
     * Retrieves user information by authentication data.
     * @param request user identification payload (e.g., email or token-based)
     * @return user data
     */
    @GetMapping("/userinfo")
    public ResponseEntity<UserResponseDTO> userinfo(@RequestBody UserRequestDTO request) {
        return userService.getUserInfo(request);
    }

    /**
     * Retrieves user information by user ID.
     * @param userId user ID
     * @return user data
     */
    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> user(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }
}
