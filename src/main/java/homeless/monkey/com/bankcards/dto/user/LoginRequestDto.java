package homeless.monkey.com.bankcards.dto.user;

import jakarta.validation.constraints.NotBlank;

public record LoginRequestDto(

        @NotBlank
        String email,

        @NotBlank
        String password
) {}
