package hu.szarvas.football_api.controller;


import hu.szarvas.football_api.dto.request.UserRequestDTO;
import hu.szarvas.football_api.dto.response.UserResponseDTO;
import hu.szarvas.football_api.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    @GetMapping("/userinfo")
    public ResponseEntity<UserResponseDTO> userinfo(@RequestBody UserRequestDTO request) {
        return userService.getUserInfo(request);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserResponseDTO> user(@PathVariable Integer userId) {
        return userService.getUser(userId);
    }
}
