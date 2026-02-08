package homeless.monkey.com.bankcards.dto.card;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class CardSearchRequestDto {

    @Min(value = 0)
    @JsonSetter(nulls = Nulls.SKIP)
    private Integer page = 0;

    @JsonSetter(nulls = Nulls.SKIP)
    private Integer size = 5;

    @JsonSetter(nulls = Nulls.SKIP)
    private String sort = "id,asc";

    private CardFilter cardFilter;
}

