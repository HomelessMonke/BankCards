package homeless.monkey.com.bankcards.dto.user;

import homeless.monkey.com.bankcards.entity.Role;

public record UserCreationResponseDto(

        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
){}
