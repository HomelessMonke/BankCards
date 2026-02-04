package homeless.monkey.com.bankcards.dto;

import homeless.monkey.com.bankcards.entity.Role;

public record UserCreationResponseDTO(

        Long id,
        String firstName,
        String lastName,
        String email,
        Role role
){}
