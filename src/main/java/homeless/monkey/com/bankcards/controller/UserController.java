package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.UserCreationRequestDTO;
import homeless.monkey.com.bankcards.dto.UserCreationResponseDTO;
import homeless.monkey.com.bankcards.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreationResponseDTO createUser(@RequestBody UserCreationRequestDTO userDTO){
        return userService.createUser(userDTO);
    }
}
