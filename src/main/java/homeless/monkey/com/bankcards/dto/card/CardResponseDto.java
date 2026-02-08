package homeless.monkey.com.bankcards.dto.card;

import homeless.monkey.com.bankcards.entity.CardStatus;

import java.math.BigDecimal;
import java.time.LocalDate;

public record CardResponseDto(
        Long id,
        String maskedCardNumber,
        LocalDate expirationDate,
        BigDecimal balance,
        CardStatus status,
        Long ownerId
){}
