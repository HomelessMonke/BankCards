package homeless.monkey.com.bankcards.dto;

import homeless.monkey.com.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardCreationResponseDTO (
        Long id,
        String maskedCardNumber,
        LocalDate expirationDate,
        BigDecimal balance,
        CardStatus status,
        Long ownerId
){}
