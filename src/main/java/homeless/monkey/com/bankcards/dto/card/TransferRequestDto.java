package homeless.monkey.com.bankcards.dto.card;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
@AllArgsConstructor
public class TransferRequestDto {
    @NotNull
    Long fromCardId;

    @NotNull
    Long toCardId;

    @NotNull
    @Positive
    BigDecimal amount;
}

