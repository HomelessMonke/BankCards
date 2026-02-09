package homeless.monkey.com.bankcards.dto.card;

import java.math.BigDecimal;

public record TransferResponseDto (
        String fromMaskedCardNumber,
        String toMaskedCardNumber,
        BigDecimal fromCardBalance,
        BigDecimal toCardBalance)
{}
