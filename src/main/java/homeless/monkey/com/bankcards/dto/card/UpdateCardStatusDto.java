package homeless.monkey.com.bankcards.dto.card;

import homeless.monkey.com.bankcards.entity.CardStatus;
import jakarta.validation.constraints.NotNull;

public record UpdateCardStatusDto(
        @NotNull
        CardStatus status
) {}
