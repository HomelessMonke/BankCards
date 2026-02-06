package homeless.monkey.com.bankcards.dto;

import homeless.monkey.com.bankcards.entity.CardStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCardStatusDTO(
        @NotNull
        CardStatus status
) {}
