package imhong.dowith.auth.controller;

import imhong.dowith.auth.dto.LoginRequest;
import imhong.dowith.auth.dto.RegisterRequest;
import imhong.dowith.auth.dto.TokenResponse;
import imhong.dowith.auth.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public ResponseEntity<TokenResponse> register(@Valid @RequestBody RegisterRequest request) {
        String accessToken = authService.register(request);
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }

    @PostMapping("/login")
    public ResponseEntity<TokenResponse> login(@Valid @RequestBody LoginRequest request) {
        String accessToken = authService.login(request);
        return ResponseEntity.ok(new TokenResponse(accessToken));
    }
}
