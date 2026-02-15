package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.user.LoginRequestDto;
import homeless.monkey.com.bankcards.util.JwtUtils;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Slf4j
@RestController
@RequestMapping("/auth")
@Tag(name = "Auth", description = "Авторизация")
public class AuthController {

    private final AuthenticationManager authenticationManager;

    public AuthController(AuthenticationManager authenticationManager) {
        this.authenticationManager = authenticationManager;
    }

    @PostMapping("/login")
    @Operation(summary = "Аутентификация")
    public ResponseEntity<?> login(@RequestBody LoginRequestDto dto){

        log.info("POST /auth/login: попытка аутентификации email={}", dto.email());
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.email(),
                        dto.password()
                )
        );
        var userDetails = (UserDetails) authentication.getPrincipal();
        String token = JwtUtils.generateToken(userDetails);
        log.info("Успешная аутентификация пользователя: email={}", dto.email());
        return ResponseEntity.ok(token);
    }
}
