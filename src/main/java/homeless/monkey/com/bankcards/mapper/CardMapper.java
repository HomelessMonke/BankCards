package homeless.monkey.com.bankcards.mapper;

import homeless.monkey.com.bankcards.dto.card.CardResponseDto;
import homeless.monkey.com.bankcards.entity.CardEntity;
import homeless.monkey.com.bankcards.util.CardUtil;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CardMapper {

    @Mapping(target = "ownerId", source = "owner.id")
    @Mapping(target = "maskedCardNumber", source = "cardNumber", qualifiedByName = "maskCardNumber")
    CardResponseDto toResponseDto(CardEntity card);

    @Named("maskCardNumber")
    default String getMaskedNumber(String cardNumber){
        return CardUtil.getMaskedCardNumber(cardNumber);
    }
}
