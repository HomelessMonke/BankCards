package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.user.UserCreationRequestDto;
import homeless.monkey.com.bankcards.dto.user.UserCreationResponseDto;
import homeless.monkey.com.bankcards.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/registration")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreationResponseDto createUser(@RequestBody UserCreationRequestDto userDTO){
        return userService.createUser(userDTO);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
