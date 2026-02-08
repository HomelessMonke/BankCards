package homeless.monkey.com.bankcards.service;

import homeless.monkey.com.bankcards.dto.card.CardCreationRequestDto;
import homeless.monkey.com.bankcards.dto.card.CardResponseDto;
import homeless.monkey.com.bankcards.dto.card.CardSearchRequestDto;
import homeless.monkey.com.bankcards.dto.card.UpdateCardStatusDto;
import homeless.monkey.com.bankcards.entity.CardEntity;
import homeless.monkey.com.bankcards.entity.CardStatus;
import homeless.monkey.com.bankcards.entity.UserEntity;
import homeless.monkey.com.bankcards.repository.CardsRepository;
import homeless.monkey.com.bankcards.repository.UserRepository;
import homeless.monkey.com.bankcards.util.CardFilterSpecifications;
import homeless.monkey.com.bankcards.util.CardUtil;
import homeless.monkey.com.bankcards.util.PageUtil;
import jakarta.transaction.Transactional;
import homeless.monkey.com.bankcards.mapper.CardMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

@Service
public class CardService {

    public final static String BIN = "412550";

    private final CardsRepository cardsRepository;
    private final UserRepository userRepository;
    private final CardMapper cardMapper;

    public CardService(CardsRepository cardsRepository, UserRepository userRepository, CardMapper cardMapper) {
        this.cardsRepository = cardsRepository;
        this.userRepository = userRepository;
        this.cardMapper = cardMapper;
    }

    public Page<CardResponseDto> getAllCards(Pageable pageable){
        Page<CardEntity> cardsPage = cardsRepository.findAll(pageable);
        return cardsPage.map(cardMapper::toResponseDto);
    }

    public Page<CardResponseDto> getUserCards(UserEntity user, CardSearchRequestDto searchDto){

        Pageable pageable = PageUtil.createPageable(searchDto.getPage(),searchDto.getSize(), searchDto.getSort());
        Specification<CardEntity> spec = CardFilterSpecifications.byOwnerId(user.getId());

        var filter = searchDto.getCardFilter();
        if(filter != null){
            spec = spec.and(CardFilterSpecifications.fromFilter(filter));
        }

        return cardsRepository.findAll(spec, pageable).map(cardMapper::toResponseDto);
    }

    @Transactional
    public CardResponseDto createCard(CardCreationRequestDto dto){

        var user = userRepository.findById(dto.getOwnerId())
                .orElseThrow(()-> new IllegalArgumentException(String.format("Пользователя с ID %s не найдено", dto.getOwnerId())));

        var cardNumber = generateCardNumber();
        var card = new CardEntity();
        card.setCardNumber(cardNumber);
        card.setExpirationDate(dto.getExpirationDate());
        card.setBalance(dto.getBalance());
        card.setCardStatus(CardStatus.ACTIVE);
        card.setOwner(user);
        cardsRepository.save(card);

        return cardMapper.toResponseDto(card);
    }

    public void deleteCard(Long cardID){
        var card = getCard(cardID);
        cardsRepository.delete(card);
    }

    public void updateCardStatus(Long cardID, UpdateCardStatusDto dto){
        var card = getCard(cardID);
        card.setCardStatus(dto.status());
        cardsRepository.save(card);
    }

    private CardEntity getCard(Long cardID){
        return cardsRepository.findById(cardID)
                .orElseThrow(()-> new IllegalArgumentException("Карта с id:" + cardID + " не найдена"));
    }

    private String generateCardNumber() {
        int maxAttempts = 5;
        String cardNumber;
        for (int i = 0; i < maxAttempts; i++) {
            cardNumber = CardUtil.generateCardNumber(BIN);
            if(cardsRepository.findBankCardByCardNumber(cardNumber).isEmpty())
                return cardNumber;
        }

        throw new IllegalStateException("Не удалось сгенерировать уникальный номер карты после " + maxAttempts + " попыток");
    }
}
