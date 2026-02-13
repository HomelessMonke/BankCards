package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.user.UserCreationRequestDto;
import homeless.monkey.com.bankcards.dto.user.UserCreationResponseDto;
import homeless.monkey.com.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@SecurityRequirement(name = "bearerAuth")
@Tag(name = "Users", description = "Работа с пользователями")
@RequestMapping("/admin/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @Operation(summary = "Зарегистрировать пользователя (admin)")
    @PostMapping("/registration")
    @PreAuthorize("hasRole('ADMIN')")
    @ResponseStatus(HttpStatus.CREATED)
    public UserCreationResponseDto createUser(@RequestBody UserCreationRequestDto userDTO){
        return userService.createUser(userDTO);
    }

    @Operation(summary = "Удалить пользователя (admin)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
