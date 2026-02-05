package homeless.monkey.com.bankcards.dto;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import lombok.*;
import lombok.extern.jackson.Jacksonized;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CardCreationRequestDTO {

        @NotNull(message = "ID владельца обязательно!")
        private Long ownerId;

        @Min(value = 1)
        @Max(value = 10)
        @JsonSetter(nulls = Nulls.SKIP)
        private Integer validityYears = 1;

        @JsonSetter(nulls = Nulls.SKIP)
        @PositiveOrZero(message = "Баланс не может быть отрицательным")
        private BigDecimal balance = BigDecimal.ZERO;

        public LocalDate getExpirationDate() {return LocalDate.now().plusYears(validityYears);}
}
