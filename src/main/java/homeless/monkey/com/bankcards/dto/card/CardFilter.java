package homeless.monkey.com.bankcards.dto.card;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;

@Getter
@Setter
@NoArgsConstructor
public class CardFilter {

    private String cardNumber;
    private String status;

    private String ownerFirstName;
    private String ownerLastName;

    private BigDecimal minBalance;
    private BigDecimal maxBalance;

    private LocalDate expirationAfter;
    private LocalDate expirationBefore;
}
