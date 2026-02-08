package homeless.monkey.com.bankcards.dto.user;

import homeless.monkey.com.bankcards.entity.Role;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record UserCreationRequestDto(

        @NotBlank(message = "Поле обязательное")
        String firstName,

        @NotBlank(message = "Поле обязательное")
        String lastName,

        @Email(message = "некорректный формат email")
        @NotBlank(message = "Поле обязательное")
        String email,

        @NotBlank(message = "Поле обязательное")
        String password,

        @NotNull
        Role role
){}

