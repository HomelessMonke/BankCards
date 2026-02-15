package homeless.monkey.com.bankcards.controller;

import homeless.monkey.com.bankcards.dto.user.UserCreationRequestDto;
import homeless.monkey.com.bankcards.dto.user.UserCreationResponseDto;
import homeless.monkey.com.bankcards.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@Slf4j
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
    public ResponseEntity<UserCreationResponseDto> createUser(@RequestBody UserCreationRequestDto userDTO){
        log.info("POST /admin/users/registration: создание пользователя email={}, role={}",
                userDTO.email(), userDTO.role());

        UserCreationResponseDto responseDto = userService.createUser(userDTO);
        log.info("Успешное создание пользователя: id={}, email={}", responseDto.id(), responseDto.email());
        return ResponseEntity.status(HttpStatus.CREATED).body(responseDto);
    }

    @Operation(summary = "Удалить пользователя (admin)")
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteUser(@PathVariable Long id){
        log.info("DELETE /admin/users/{}: запрос удаления пользователя", id);
        userService.deleteUser(id);
        log.info("Успешное удаление пользователя: id={})", id);
        return ResponseEntity.noContent().build();
    }
}
